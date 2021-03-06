/*
 * Copyright 2016 Kasije Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(function(win, $)
{
    //Maps component id vs component instance
    var components = {};
    var componentsCount = 0;

    // Generic component
    var Component = function(config)
    {
        config = config || {};
        config.id = config.id || 'comp-' + componentsCount;
        config.init = config.init || function(){};

        for (var key in config)
        {
            if ({}.hasOwnProperty.call(config, key))
            {
                this[key] = config[key];
            }
        }

        this.prototype = Component.prototype;

        components[this.id] = this;
        componentsCount++;

        return this;
    };

    Component.prototype.fire = function(event)
    {
        if (typeof(event) === 'string')
        {
            event = {
                id: event,
                data: {}
            };
        }

        var eventId = '_on' + event.id;

        for (var id in components)
        {
            if (!{}.hasOwnProperty.call(components, id))
            {
                continue;
            }

            var component = components[id];
            if (component[eventId] && typeof(component[eventId]) === 'function')
            {
                component[eventId].call(component, event.data);
            }
        }
    };
    Component.prototype.on = function(eventId, handler)
    {
        if (!eventId || !handler || typeof(handler) !== 'function')
        {
            return;
        }

        this['_on' + eventId] = handler;
    };

    win.createComponent = function(config)
    {
        return new Component(config);
    };

    //Form validation
    var isValidForm = function(form)
    {
        var isValid = true;
        form.find('input, select').each(function(index, input)
        {
            if (!input.checkValidity())
            {
                isValid = false;
                $(input).closest('div.form-group').addClass('has-error');
            }
            else
            {
                $(input).closest('div.form-group').removeClass('has-error');
            }
        });

        return isValid;
    };

    //Editor component
    win.createEditorComponent = function(config)
    {
        config.element = null;
        config.files = null;
        config.current = null;

        var preInit = config.init || function() {};

        config.init = function()
        {
            var self = this;
            self.element = $(config.elementSelector);

            var updateCurrent = function(file)
            {
                if (!self.current || file.name === self.current.file.name)
                {
                    return;
                }

                var previousFileId = self.current.file.name.split('.').join('_').toLowerCase();
                self.files[previousFileId].text = self.current.editor.getValue();
            };

            var createEditor = function(file, ws)
            {
                if (self.current.editor)
                {
                    return;
                }

                self.current.editor = win.ace.edit(ws.find('.ace-editor')[0]);
                self.current.editor.setOptions({enableBasicAutocompletion: true});
                if (file.tags)
                {
                    self.current.editor.completers = [{
                        getCompletions: function(editor, session, pos, prefix, callback)
                        {
                            var map = file.tags.map(function(word)
                            {
                                return {
                                    caption: word,
                                    value: word,
                                    meta: "static"
                                };
                            });

                            callback(null, map);

                        }
                    }];
                }

                self.current.editor.commands.addCommand({
                    name: 'save',
                    bindKey: {win: 'Ctrl-S',  mac: 'Command-S'},
                    exec: function()
                    {
                        self.fire('save-current-file');
                    },
                    readOnly: false
                });
                self.current.editor.commands.addCommand({
                    name: 'undo',
                    bindKey: {win: 'Ctrl-Z',  mac: 'Command-Z'},
                    exec: function()
                    {
                        self.fire('reset-current-file');
                    },
                    readOnly: false
                });
            };

            // File selection
            var handleFileSelected = function(event)
            {
                var fileId = event.currentTarget.id;
                var file = self.files[fileId];
                if (!file)
                {
                    self.current = null;
                    return;
                }

                var ws = $(config.editorWSelector);
                ws.find('.not-enabled').removeClass('not-enabled');
                updateCurrent(file);

                self.current = self.current || {};

                $(config.fileNameSelector).html(file.path + file.name);

                createEditor(file, ws);

                self.current.editor.getSession().setMode('ace/mode/' + file.type);
                self.current.editor.setValue(file.text);
                self.current.editor.navigateTo(0, 0);
                self.current.editor.scrollToLine(0, true, true, function () {});
                setTimeout(function()
                {
                    self.current.editor.session.getUndoManager().reset();
                }, 50);

                self.current.file = file;
            };

            // File to add
            var addFileToList = function(data)
            {
                var faIcons = {
                    css: 'css3',
                    sass: 'css3',
                    javascript: 'html5'//check js availability
                };

                var colorIcons = {
                    css: 'text-warning',
                    sass: 'text-warning',
                    javascript: 'text-success',
                    ftl: 'text-primary'
                };

                var name = data.name;
                var nameId = name.split('.').join('_').toLowerCase();
                var faIcon = faIcons[data.type] || 'code';
                var colorIcon = colorIcons[data.type] || '';

                var iElement = '<i class="fa fa-' + faIcon + ' ' + colorIcon + '"></i>';
                var aElement = '<a href="#">' + iElement + name + '</a>';
                var liElement = '<li id="' + nameId + '">' + aElement + '</li>';

                self.element.append(liElement);
                self.files[nameId] = data;
                self.files[nameId].origText = self.files[nameId].text;

                self.element.find('#' + nameId).on('click', handleFileSelected);
            };

            // Initial files loading
            win.showLoading();

            var jqXHR = $.ajax({
                method: 'POST',
                url: config.fetchURL
            });

            jqXHR.done(function(data)
            {
                self.files = {};

                for (var index in data)
                {
                    if ({}.hasOwnProperty.call(data, index))
                    {
                        addFileToList(data[index]);
                    }
                }

                win.hideLoading();
                $('.load-enabled').removeClass('not-enabled');
            });

            jqXHR.fail(function()
            {
                win.hideLoading();
                $('.load-enabled').removeClass('not-enabled');

                console.error(arguments);//TODO
            });

            // Add file handler
            var addFileBtn = $('#addFile');
            var modalDiv = addFileBtn.closest('div.modal-dialog');
            var modalForm = modalDiv.find('form');
            addFileBtn.on('click', function(e)
            {
                if (!isValidForm(modalForm))
                {
                    return;
                }

                win.showLoading();

                var jqXHR = $.ajax({
                    method: 'POST',
                    url: modalForm.data('url'),
                    data: modalForm.serialize()
                });

                jqXHR.done(function(data)
                {
                    addFileToList(data[0]);

                    modalDiv.closest('div.modal').modal('hide');

                    win.hideLoading();

                    setTimeout(function()
                    {
                        handleFileSelected({
                            currentTarget: {
                                id: data[0].name.split('.').join('_').toLowerCase()
                            }
                        });
                    }, 200);
                });

                jqXHR.fail(function()
                {
                    win.hideLoading();

                    console.error(arguments);//TODO
                });

                e.preventDefault();
            });

            preInit.call(self);
        };

        var editorComp = new Component(config);

        editorComp.on('save-current-file', function()
        {
            if (!this.current)
            {
                return;
            }

            win.showLoading();
            console.log('saving...');

            var self = this;
            self.current.file.text = self.current.editor.getValue();

            var jqXHR = $.ajax({
                method: 'POST',
                url: config.saveURL + self.current.file.path + self.current.file.name,
                data: {
                    text: self.current.file.text
                }
            });

            jqXHR.done(function()
            {
                win.hideLoading();
                console.log('ok');
                self.current.file.origText = self.current.file.text;
            });

            jqXHR.fail(function()
            {
                win.hideLoading();
                console.error(arguments);//TODO
            });
        });

        editorComp.on('reset-current-file', function()
        {
            if (!this.current)
            {
                return;
            }

            var undoMgr = this.current.editor.session.getUndoManager();
            if (undoMgr.hasUndo())
            {
                undoMgr.undo();
            }
        });

        return editorComp;
    };

    //Images component
    win.createImagesComponent = function(config)
    {
        config.element = null;
        config.images = null;

        var preInit = config.init || function() {};

        config.init = function()
        {
            var self = this;
            self.element = $(config.elementSelector);

            // Image to add
            var addImageToList = function(data)
            {
                var minUrl = data.type;
                var url = data.path;
                var title = data.name;
                var description = data.text;

                var imgElement = '<img class="img-responsive" src="' + minUrl + '" alt="">';
                var spanElement = '<span class="zoom-icon"></span>';
                var aElement = '<a href="' + url + '" class="b-link-stripe b-animate-go swipebox" title="' + title + '">';
                aElement += imgElement + spanElement + '</a>';
                var imgDivElement = '<div class="gallery-img">' + aElement + '</div>';

                var titleElement = '<div class="text-gallery"><h6>' + description + '</h6></div>';

                var divElement = '<div class="col-md-3">' + imgDivElement + titleElement + '</div>';
                self.element.append(divElement);

                self.images[url] = data;

                return minUrl;
            };

            // Initial images loading
            win.showLoading();

            var jqXHR = $.ajax({
                method: 'POST',
                url: config.fetchURL
            });

            jqXHR.done(function(data)
            {
                self.images = {};

                for (var index in data)
                {
                    if ({}.hasOwnProperty.call(data, index))
                    {
                        addImageToList(data[index]);
                    }
                }

                self.element.append('<div class="clearfix" id="lastGalleryElement"></div>');
                $('.not-enabled').removeClass('not-enabled');
                win.hideLoading();
            });

            jqXHR.fail(function()
            {
                win.hideLoading();

                console.error(arguments);//TODO
            });

            //Upload
            var inputImgBtn = $('#inputImage');
            var realInputImgBtn = $('#realInputImage');
            var selectedImage = $('#selectedImage');
            inputImgBtn.on('click', function(e)
            {
                realInputImgBtn.click();
                e.preventDefault();
            });

            var getImageFile = function()
            {
                var file = null;
                if (realInputImgBtn[0].files && realInputImgBtn[0].files.length)
                {
                    file = realInputImgBtn[0].files[0];
                }

                return file;
            };

            realInputImgBtn.on('change', function(e)
            {
                var file = getImageFile();
                if (!file)
                {
                    selectedImage.html('<p class="bg-danger text-danger">No image selected!</p>');
                    return;
                }

                selectedImage.html('');

                var imgElement = '<img src="' + window.URL.createObjectURL(file) + '" height="60" />';
                var spanElement = '&nbsp;<span>' + file.name + ': ' + file.size + ' bytes</span>';
                selectedImage.append(imgElement + spanElement);

                e.preventDefault();
            });

            var uploadImgBtn = $('#uploadImage');
            var modalDiv = uploadImgBtn.closest('div.modal-dialog');
            var modalForm = modalDiv.find('form');
            uploadImgBtn.on('click', function(e)
            {
                if (!isValidForm(modalForm))
                {
                    return;
                }

                var file = getImageFile();
                if (!file)
                {
                    selectedImage.html('<p class="bg-danger text-danger">No image selected!</p>');
                    return;
                }

                var data = new FormData();
                data.append('imageFile', file);
                var formData = modalForm.serializeArray();
                $.each(formData, function(key, input)
                {
                    data.append(input.name, input.value);
                });

                win.showLoading();

                var jqXHR = $.ajax({
                    method: 'POST',
                    url: modalForm.data('url'),
                    data: data,
                    contentType: false,
                    processData: false
                });

                jqXHR.done(function(data)
                {
                    var url = addImageToList(data[0]);

                    modalDiv.closest('div.modal').modal('hide');

                    win.hideLoading();

                    $('#lastGalleryElement').remove();
                    self.element.append('<div class="clearfix" id="lastGalleryElement"></div>');

                    var imgElement = $('[src="' + url + '"]');
                    $('html, body').animate({
                        scrollTop: imgElement.offset().top - 20
                    }, 500);
                });

                jqXHR.fail(function()
                {
                    win.hideLoading();

                    console.error(arguments);//TODO
                });

                e.preventDefault();
            });

            preInit.call(self);
        };

        var imagesComp = new Component(config);

        return imagesComp;
    };

    //Loading
    var loadingEl = $('#loadingoverlay');
    win.showLoading = function()
    {
        if (loadingEl.length && !loadingEl.hasClass('loadingoverlay'))
        {
            loadingEl.addClass('loadingoverlay');
        }
    };
    win.hideLoading = function()
    {
        if (loadingEl.length && loadingEl.hasClass('loadingoverlay'))
        {
            loadingEl.removeClass('loadingoverlay');
        }
    };

    //Init components on jQuery load
    $(function()
    {
        for (var index in components)
        {
            if ({}.hasOwnProperty.call(components, index))
            {
                var component = components[index];
                component.init.call(component);
            }
        }
        win.hideLoading();
    });
}(window, jQuery));

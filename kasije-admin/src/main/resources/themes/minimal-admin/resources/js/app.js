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
            this[key] = config[key];
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

    win.Component = Component;

    //Form validation
    var isValidForm = function(form)
    {
        var isValid = true
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

        return isValid
    }

    //Editor component
    var EditorComponent = function(config)
    {
        config.element = null;
        config.files = null;
        config.current = null;

        var preInit = config.init || function() {};

        config.init = function()
        {
            var self = this;
            self.element = $(config.elementSelector);

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

                if (self.current)
                {
                    if (file.name === self.current.file.name)
                    {
                        return;
                    }

                    var previousFileId = self.current.file.name.split('.').join('_').toLowerCase();
                    self.files[previousFileId].text = self.current.editor.getValue();
                }
                else
                {
                    self.current = {};
                }

                $(config.fileNameSelector).html(file.path + file.name);

                if (!self.current.editor)
                {
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
                        exec: function(editor)
                        {
                            self.fire('save-current-file');
                        },
                        readOnly: false
                    });
                    self.current.editor.commands.addCommand({
                        name: 'undo',
                        bindKey: {win: 'Ctrl-Z',  mac: 'Command-Z'},
                        exec: function(editor)
                        {
                            self.fire('reset-current-file');
                        },
                        readOnly: false
                    });
                }

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
                    css: 'css3'
                };

                var name = data.name;
                var nameId = name.split('.').join('_').toLowerCase();
                var faIcon = faIcons[data.type] || 'code';

                var iElement = '<i class="fa fa-' + faIcon + '"></i>';
                var aElement = '<a href="#">' + iElement + name + '</a>';
                var liElement = '<li id="' + nameId + '">' + aElement + '</li>';

                self.element.append(liElement);
                self.files[nameId] = data;
                self.files[nameId].origText = self.files[nameId].text;

                self.element.find('#' + nameId).on('click', handleFileSelected);
            };

            // Initial files loading
            win.showLoading()

            var jqXHR = $.ajax({
                method: 'POST',
                url: config.fetchURL
            });

            jqXHR.done(function(data)
            {
                self.files = {};

                for (var index in data)
                {
                    addFileToList(data[index]);
                }

                win.hideLoading();
                $('.load-enabled').removeClass('not-enabled');
            });

            jqXHR.fail(function(data)
            {
                win.hideLoading();
                $('.load-enabled').removeClass('not-enabled');

                console.error(arguments);//TODO
            });

            // Add file handler
            var addFileBtn = $('#addFile');
            var modalDiv = addFileBtn.closest('div.modal-dialog');
            var modalForm = modalDiv.find('form');
            addFileBtn.on('click', function(event)
            {
                if (!isValidForm(modalForm))
                {
                    return
                }

                win.showLoading()

                var jqXHR = $.ajax({
                    method: 'POST',
                    url: modalForm.data('url'),
                    data: modalForm.serialize()
                });

                jqXHR.done(function(data)
                {
                    addFileToList(data[0]);

                    modalDiv.closest('div.modal').modal('hide')

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

                jqXHR.fail(function(data)
                {
                    win.hideLoading();

                    console.error(arguments);//TODO
                });
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
            console.log('saving...')

            var self = this;
            self.current.file.text = self.current.editor.getValue();

            var jqXHR = $.ajax({
                method: 'POST',
                url: config.saveURL + self.current.file.path + self.current.file.name,
                data: {
                    text: self.current.file.text
                }
            });

            jqXHR.done(function(data)
            {
                win.hideLoading();
                console.log('ok');
                self.current.file.origText = self.current.file.text;
            });

            jqXHR.fail(function(data)
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

    win.EditorComponent = EditorComponent;

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
            var component = components[index];
            component.init.call(component);
        }
        win.hideLoading();
    });
})(window, jQuery);

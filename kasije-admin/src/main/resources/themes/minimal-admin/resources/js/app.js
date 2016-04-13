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

    //Editor component
    var EditorComponent = function(config)
    {
        config.element = null;
        config.files = null;
        config.current = null;

        var preInit = config.init || function() {};

        config.init = function()
        {
            win.showLoading();

            var self = this;
            self.element = $(config.elementSelector);

            var jqXHR = $.ajax({
                method: 'POST',
                url: config.fetchURL
            });

            jqXHR.done(function(data)
            {
                self.files = {};

                var faIcons = {
                    css: 'css3'
                };

                for (var index in data)
                {
                    var name = data[index].name;
                    var faIcon = faIcons[data[index].type] || 'code';

                    var iElement = '<i class="fa fa-' + faIcon + '"></i>';
                    var aElement = '<a href="#">' + iElement + name + '</a>';
                    var liCls = (index == 0) ? 'active' : '';
                    var liElement = '<li class="' + liCls + '" id="' + name + '">' + aElement + '</li>';

                    self.element.append(liElement);
                    self.files[name] = data[index];
                    self.files[name].origText = self.files[name].text;
                }

                self.element.find('li').on('click', function(event)
                {
                    var file = self.files[event.currentTarget.id];
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

                        self.files[self.current.file.name].text = self.current.editor.getValue();
                    }
                    else
                    {
                        self.current = {};
                    }

                    $(config.fileNameSelector).html(file.name);

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
                    self.current.editor.gotoLine(1);
                    setTimeout(function()
                    {
                        self.current.editor.session.getUndoManager().reset();
                    }, 50);

                    self.current.file = file;
                });

                win.hideLoading();
            });

            jqXHR.fail(function(data)
            {
                win.hideLoading();
                console.error(arguments);//TODO
            });

            preInit.call(self);
        };

        return new Component(config);
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

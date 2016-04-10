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
    new win.Component({
        id: 'current-file-actions',
        init: function()
        {
            var self = this;

            $('#save-current-file').on('click', function(event)
            {
                self.fire('save-current-file');
            });

            $('#reset-current-file').on('click', function(event)
            {
                self.fire('reset-current-file');
            });
        }
    });

    var pageHandler = new win.Component({
        id: 'site-pages-list',
        element: null,
        files: null,
        current: null,
        init: function()
        {
            win.showLoading();

            var self = this;
            self.element = $('.site-pages-list');

            var jqXHR = $.ajax({
                method: 'POST',
                url: '/admin/pages'
            });

            jqXHR.done(function(data)
            {
                self.files = {};

                for (var index in data)
                {
                    var name = data[index].name;

                    var iElement = '<i class="fa fa-code"></i>';
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

                    var ws = $('#editor-workspace');
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

                    $('#editor-file-name').html(file.name);

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
        }
    });

    pageHandler.on('save-current-file', function()
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
            url: '/admin/save-page/' + self.current.file.name,
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

    pageHandler.on('reset-current-file', function()
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
})(window, jQuery);
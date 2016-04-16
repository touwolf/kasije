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
        id: 'current-theme-actions',
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

    new win.EditorComponent({
        id: 'site-theme-list',
        elementSelector: '.site-theme-list',
        fetchURL: '/admin/themes',
        editorWSelector: '#editor-workspace',
        fileNameSelector: '#editor-file-name',
        saveURL: '/admin/save-theme-resource/'
    });
})(window, jQuery);

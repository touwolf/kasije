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
    win.createComponent({
        id: 'current-resources-actions',
        init: function()
        {
            var self = this;

            $('#save-current-file').on('click', function()
            {
                self.fire('save-current-file');
            });

            $('#reset-current-file').on('click', function()
            {
                self.fire('reset-current-file');
            });
        }
    });

    win.createEditorComponent({
        id: 'site-pages-resources-list',
        elementSelector: '.site-pages-resources-list',
        fetchURL: '/admin/pages-resources',
        editorWSelector: '#editor-workspace',
        fileNameSelector: '#editor-file-name',
        saveURL: '/admin/save-page-resource/'
    });
})(window, jQuery);

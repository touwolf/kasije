jQuery(document).ready(function($)
{
    var initEditorHolder = function($parent, files)
    {
        var $holder = $parent.find('.editor-holder');
        var currentIndex = -1;
        var editor = null;
        if (!$holder.length)
        {
            return;
        }

        var $row = $holder.find('.row-value');
        var $column = $holder.find('.column-value');

        var initEditor = function(index)
        {
            var $editor = $holder.find('.editor');
            if (!$editor.length)
            {
                return;
            }

            editor = ace.edit($editor[0]);
            editor.$blockScrolling = Infinity;
            var file = files[index];
            if (!file || !file.text)
            {
                return;
            }

            var mode = 'ace/mode/' + file.type;
            editor.getSession().setMode(mode);
            editor.setValue(file.text);

            $row.html('1');
            $column.html('1');
            editor.getSession().selection.on('changeCursor', function()
            {
                $row.html(editor.selection.getCursor().row + 1);
                $column.html(editor.selection.getCursor().column + 1);
            });
        };

        var $filesSelect = $parent.find('.file-select');
        if ($filesSelect.length)
        {
            $filesSelect
                .find('option')
                .filter(function(index)
                {
                    return index > 0;
                })
                .remove();

            $.each(files, function(index, file)
            {
                var fileOption = '<option value="' + index + '">' + file.name + '</option>';
                $filesSelect.append(fileOption);
            });

            $filesSelect.removeAttr('disabled');

            $filesSelect.on('change', function()
            {
                if (editor && files[currentIndex])
                {
                    files[currentIndex].text = editor.getValue();
                }

                currentIndex = Number($filesSelect.val());
                initEditor(currentIndex);
            });
        }
    };

    var jqXHR = $.get('/admin/pages');

    jqXHR.done(function(files)
    {
        initEditorHolder($('body'), files);
    });

    jqXHR.fail(function()
    {
        //TODO
        console.error(arguments)
    });
});

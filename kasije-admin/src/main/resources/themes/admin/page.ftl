<#ftl encoding="UTF-8">

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${page.@title}</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/skeleton/2.0.4/skeleton.min.css" />
    <link rel="stylesheet" href="/resources/css/styles.css" />
</head>
<body>
    <div class="row">
        <div class="six columns editor-holder">
            <div class="row">
                <div class="six columns">
                    <label for="page-selector">Page</label>
                    <select class="u-full-width file-select" id="page-selector" disabled>
                        <option disabled selected>Select...</option>
                    </select>
                </div>
            </div>
            <div class="editor"></div>
            <div class="row editor-status">
                <div class="one column"></div>
                <div class="two columns">
                    <span class="row-value"></span>:<span class="column-value"></span>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-2.2.1.min.js"
            integrity="sha256-gvQgAFzTH6trSrAWoH1iPo9Xc96QxSZ3feW6kem+O00="
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/ace/1.2.3/noconflict/ace.js" charset="utf-8"></script>
    <script src="/resources/js/app.js"></script>
</body>

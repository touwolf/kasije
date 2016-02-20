<#ftl encoding="UTF-8">

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${page.@title}</title>
</head>
<body>
    <#list page.section as s>
        <div>
            <h1>${s.title}</h1>
            <p>
                ${s.content}
            </p>
        </div>
    </#list>
</body>

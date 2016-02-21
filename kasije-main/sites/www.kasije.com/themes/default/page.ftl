<#ftl encoding="UTF-8">

<#macro renderPage>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="stylesheet" href="/resources/style.css" />
        <title>${page.@title}</title>
    </head>
    <body>
        <@renderComponents page.* />
    </body>
</#macro>

<#macro renderSection section >
    <div class="section">
        <h1>${section.title}</h1>
        <p>
            ${section.content}
        </p>
    </div>
</#macro>

<#macro renderPanel panel >
    <div class="panel">
        ${panel}
    </div>
</#macro>

<#macro renderComponents components>
    <#list components as c>
        <#if c?node_type == "element">
            <#if c?node_name == "section">
                <@renderSection c />
            <#elseif c?node_name == "panel">
                <@renderPanel c />
            </#if>
        </#if>
    </#list>
</#macro>

 <@renderPage />
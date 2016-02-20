<#ftl encoding="UTF-8">

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Touwolf</title>
</head>
<body>
    <h1>Hi ${xmlData.name}</h1>
    <#list xmlData.place as place>
        ${place}<br/>
    </#list>
</body>

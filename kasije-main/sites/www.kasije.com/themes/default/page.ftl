<#ftl encoding="UTF-8">

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="author" content="${page.meta.author}">
        <meta name="description" content="${page.meta.description}">

        <title>${page.@title}</title>

        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" />
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet" />
        <link href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.1/animate.min.css" rel="stylesheet" />
        <link href="https://cdnjs.cloudflare.com/ajax/libs/owl-carousel/1.3.3/owl.carousel.min.css" rel="stylesheet" />
        <link href="/resources/css/styles.min.css" rel="stylesheet" />

        <!--[if lt IE 9]>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->

        <link rel="shortcut icon" href="/resources/images/ico/favicon.ico" />
        <link rel="apple-touch-icon-precomposed" sizes="144x144" href="/resources/images/ico/icon-144.png" />
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="/resources/images/ico/icon-114.png" />
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="/resources/images/ico/icon-72.png" />
        <link rel="apple-touch-icon-precomposed" href="/resources/images/ico/icon-57.png" />
    </head>
    <body id="home" class="homepage">
        <@header />
        <@content />
        <@footer />

        <script src="https://code.jquery.com/jquery-1.12.0.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <script src="https://maps.google.com/maps/api/js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/owl-carousel/1.3.3/owl.carousel.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/smoothscroll/1.4.1/SmoothScroll.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/prettyPhoto/3.1.6/js/jquery.prettyPhoto.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.isotope/2.2.2/isotope.pkgd.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/wow/1.1.2/wow.min.js"></script>
        <script src="https://cdn.rawgit.com/google/code-prettify/master/loader/run_prettify.js"></script>
        <script src="/resources/js/script.min.js"></script>
    </body>
</html>

<#macro header>
    <header id="header">
        <nav id="main-menu" class="navbar navbar-default navbar-fixed-top" role="banner">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>

                    <a class="navbar-brand" href="/">
                        <img class="img-responsive" alt="" width="160" height="50"
                             src="/resources/images/home/koding_hackathon_badge.png">
                    </a>
                </div>

                <div class="collapse navbar-collapse navbar-right">
                    <ul class="nav navbar-nav">
                        <#list page.section as section>
                            <li class="scroll<#if section?is_first> active</#if>">
                                <a href="#${section.@id}">${section.@title}</a>
                            </li>
                        </#list>
                    </ul>
                </div>
            </div>
        </nav>
    </header>
</#macro>

<#macro content>
    <#list page.section as section>
    <#import "./sections/" + section.@id + ".ftl" as sectionTpl />
    <@sectionTpl.content section />
    </#list>
</#macro>

<#macro footer>
    <footer id="footer">
        <div class="container">
            <div class="row">
                <div class="col-sm-6">
                    &copy; ${.now?string["yyyy"]} ${page.meta.author}.
                </div>

                <div class="col-sm-6">
                    <ul class="social-icons">
                        <li><a href="https://github.com/touwolf/kasije" target="_blank">
                            <i class="fa fa-github"></i>
                        </a></li>
                    </ul>
                </div>
            </div>
        </div>
    </footer>
</#macro>

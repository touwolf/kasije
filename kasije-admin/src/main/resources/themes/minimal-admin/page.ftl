<!DOCTYPE HTML>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <title>${page.@title}</title>

    <link href="/admin/resources/css/bootstrap.css" rel='stylesheet' type='text/css'/><#--TODO: replace by CDN -->
    <link href="/admin/resources/css/style.css" rel='stylesheet' type='text/css'/>
    <link href="/admin/resources/css/font-awesome.css" rel="stylesheet"><#--TODO: replace by CDN -->
    <link href="/admin/resources/css/custom.css" rel="stylesheet">
</head>
<body>
    <#if !(page.__user??) || !hasRole("admin")>
        <@unauthContent />
    <#else>
        <@userContent />
    </#if>
    <div id="loadingoverlay" class="loadingoverlay"></div>

    <script src="/admin/resources/js/jquery.js"></script><#--TODO: replace by CDN -->
    <script src="/admin/resources/js/bootstrap.js"></script><#--TODO: replace by CDN -->
    <script src="/admin/resources/js/jquery.metisMenu.js"></script><#--TODO: replace by CDN -->
    <script src="/admin/resources/js/jquery.slimscroll.js"></script><#--TODO: replace by CDN -->
    <script src="/admin/resources/js/screenfull.js"></script>
    <script src="/admin/resources/js/jquery.nicescroll.js"></script><#--TODO: replace by CDN -->
    <script src="/admin/resources/js/custom.js"></script>
    <script src="/admin/resources/js/app.js"></script>
    <#if page.resources?? && page.resources.js??>
    <#list page.resources.js as js>
    <script src="${js}.js"></script>
    </#list>
    </#if>
</body>
</html>

<#macro unauthContent>
    <div id="wrapper">
        <div class="four">
            <img src="/admin/resources/images/401.png" alt="" />
            <p>Unauthorized!</p>
            <a href="/" class="hvr-shutter-in-horizontal">Back to site</a>
        </div>

        <@footer />
    </div>
</#macro>

<#macro userContent>
    <div id="wrapper">
        <nav class="navbar-default navbar-static-top" role="navigation">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <h1><a class="navbar-brand">Admin</a></h1>
            </div>

            <div class=" border-bottom">
                <@topMenu />
                <@sideMenu />
            </div>
        </nav>

        <div id="page-wrapper" class="gray-bg dashbard-1">
            <div class="content-main">
                <div class="banner">
                    <h2>
                        <#list page.breadcrumb as breadcrumb>
                        <#if breadcrumb?index != 0>
                        <i class="fa fa-angle-right"></i>
                        </#if>
                        <span>${breadcrumb}</span>
                        </#list>
                    </h2>
                </div>

                <@content />
            </div>

            <@footer />
        </div>
    </div>
    <@clearfix />
</#macro>

<#macro topMenu>
    <div class="full-left">
        <section class="full-top">
            <button id="toggle"><i class="fa fa-arrows-alt"></i></button>
        </section>
        <@clearfix />
    </div>

    <div class="drop-men">
        <ul class=" nav_1">
            <li class="dropdown">
                <a href="#" class="dropdown-toggle dropdown-at" data-toggle="dropdown">
                    <span class="name-caret">${page.__user.@name} (${page.__user.@email})</span>
                    <img src="http://www.gravatar.com/avatar/${page.__user.@gravatarHash}?s=60"
                         onerror="this.src='/admin/resources/images/user.jpg'"/>
                </a>
            </li>

        </ul>
    </div>

    <@clearfix />
</#macro>

<#macro sideMenu>
    <div class="navbar-default sidebar" role="navigation">
        <div class="sidebar-nav navbar-collapse">
            <ul class="nav" id="side-menu">
                <li>
                    <a href="/admin" class=" hvr-bounce-to-right">
                        <i class="fa fa-home nav_icon "></i>
                        <span class="nav-label">Home</span>
                    </a>
                </li>

                <#if hasRole("pages")>
                <li>
                    <a href="#" class=" hvr-bounce-to-right">
                        <i class="fa fa-edit nav_icon"></i>
                        <span class="nav-label">Site</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <li>
                            <a href="/admin/site-pages" class=" hvr-bounce-to-right">
                                <i class="fa fa-code nav_icon"></i>Pages
                            </a>
                        </li>
                    </ul>
                </li>
                </#if>
            </ul>
        </div>
    </div>
</#macro>

<#macro content>
    <#if page.@id == "site-pages" && hasRole("pages")>
        <@pagesContent />
    <#else>
        <@homeContent />
    </#if>
</#macro>

<#macro homeContent>
    <div class="content-bottom">
        <div class="col-md-6 post-top">
            <div class="post-bottom">
                <div class="post-bottom-1">
                    <a href="#"><i class="fa fa-facebook"></i></a>
                    <p>15k <label>Likes</label></p>
                </div>
                <div class="post-bottom-2">
                    <a href="#"><i class="fa fa-twitter"></i></a>
                    <p>20M <label>Followers</label></p>
                </div>
                <@clearfix />
            </div>
        </div>

        <div class="col-md-6">
            <div class="weather">
                <div class="weather-top">
                    <div class="weather-top-left">
                        <div class="degree">
                            <figure class="icons">
                                <canvas id="partly-cloudy-day" width="64" height="64">
                                </canvas>
                            </figure>
                            <span>37<sup>o</sup></span>
                            <@clearfix />
                        </div>
                        <script src="/admin/resources/js/skycons.js"></script>
                        <p>
                            ${.now?string["EEEE"]}
                            <#assign dayNumber = .now?string["dd"] />
                            <label>${dayNumber}</label>
                            <#if dayNumber == "01" || dayNumber == "21" || dayNumber == "31">
                            <sup>st</sup>
                            <#elseif dayNumber == "02" || dayNumber == "22">
                            <sup>nd</sup>
                            <#elseif dayNumber == "03" || dayNumber == "23">
                            <sup>rd</sup>
                            <#else>
                            <sup>th</sup>
                            </#if>
                            ${.now?string["MMMM"]}
                        </p>
                    </div>

                    <div class="weather-top-right">
                        <p><i class="fa fa-map-marker"></i>Quito</p>
                        <label>Ecuador</label>
                    </div>
                    <@clearfix />
                </div>
            </div>
        </div>
        <@clearfix />
    </div>
</#macro>

<#macro pagesContent>
    <div class="inbox-mail">
        <div class="col-md-4 compose">
            <div class="input-group input-group-in">
                <input type="text" name="search" class="form-control2 input-search" placeholder="Search...">
                    <span class="input-group-btn">
                        <button class="btn btn-success" type="button">
                            <i class="fa fa-search"></i>
                        </button>
                    </span>
            </div>

            <h2>Pages</h2>
            <nav class="nav-sidebar">
                <ul class="nav tabs site-pages-list"></ul>
            </nav>
        </div>

        <div class="col-md-8 tab-content tab-content-in">
            <div class="tab-pane active text-style" id="tab2">
                <div class="inbox-right">
                    <div class="mailbox-content" id="editor-workspace">
                        <div class="mail-toolbar clearfix not-enabled">
                            <div class="float-left">
                                <h3 id="editor-file-name"></h3>
                            </div>

                            <div class="float-right">
                                <div class="dropdown">
                                    <a href="#" title="" class="btn btn-default" data-toggle="dropdown"
                                       aria-expanded="false">
                                        <i class="fa fa-file-o icon_8"></i>
                                        <i class="fa fa-chevron-down icon_8"></i>
                                        <div class="ripple-wrapper"></div>
                                    </a>

                                    <ul class="dropdown-menu float-right">
                                        <li>
                                            <a href="#" title="" id="save-current-file">
                                                <i class="fa fa-save icon_9"></i>
                                                Save
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#" title="" id="reset-current-file">
                                                <i class="fa fa-undo icon_9"></i>
                                                Undo
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <div class="form-control ace-editor not-enabled"></div>
                    </div>
                </div>
            </div>
        </div>
        <@clearfix />
    </div>
</#macro>

<#macro footer>
    <div class="copy">
        <h4>
            <img src="/admin/resources/touwolf-ico-144.png" alt=""/>
            Powered by <a href="http://www.touwolf.com" target="_blank">Touwolf</a>
        </h4>
    </div>
</#macro>

<#macro clearfix>
    <div class="clearfix"></div>
</#macro>

<#function hasRole role>
    <#if !(page.__user.role??)>
        <#return false />
    </#if>

    <#list page.__user.role as r>
        <#if role == r>
            <#return true />
        </#if>
    </#list>

    <#return false />
</#function>

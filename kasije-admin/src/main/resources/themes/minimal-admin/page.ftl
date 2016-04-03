<!DOCTYPE HTML>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <title>Admin</title>

    <link href="/admin/resources/css/bootstrap.css" rel='stylesheet' type='text/css'/><#--TODO: replace by CDN -->
    <link href="/admin/resources/css/style.css" rel='stylesheet' type='text/css'/>
    <link href="/admin/resources/css/font-awesome.css" rel="stylesheet"><#--TODO: replace by CDN -->
    <link href="/admin/resources/css/custom.css" rel="stylesheet">
</head>
<body>
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
                        <a href="index.html">Site</a>
                        <i class="fa fa-angle-right"></i>
                        <span>Pages</span>
                    </h2>
                </div>

                <@content />
            </div>

            <div class="copy">
                <h4>
                    <img src="/admin/resources/touwolf-ico-144.png" alt=""/>
                    Powered by <a href="http://www.touwolf.com" target="_blank">Touwolf</a>
                </h4>
            </div>
        </div>
    </div>
    <@clearfix />

    <script src="/admin/resources/js/jquery.js"></script><#--TODO: replace by CDN -->
    <script src="/admin/resources/js/bootstrap.js"></script><#--TODO: replace by CDN -->
    <script src="/admin/resources/js/jquery.metisMenu.js"></script><#--TODO: replace by CDN -->
    <script src="/admin/resources/js/jquery.slimscroll.js"></script><#--TODO: replace by CDN -->
    <script src="/admin/resources/js/screenfull.js"></script>
    <script src="/admin/resources/js/jquery.nicescroll.js"></script><#--TODO: replace by CDN -->
    <script src="https://cdn.jsdelivr.net/ace/1.2.3/min/ace.js"></script>
    <script src="https://cdn.jsdelivr.net/ace/1.2.3/min/ext-language_tools.js"></script>
    <script src="/admin/resources/js/custom.js"></script>
    <script src="/admin/resources/js/app.js"></script>
</body>
</html>

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
                    <a href="index.html" class=" hvr-bounce-to-right">
                        <i class="fa fa-dashboard nav_icon "></i>
                        <span class="nav-label">Dashboards</span>
                    </a>
                </li>

                <li>
                    <a href="#" class=" hvr-bounce-to-right">
                        <i class="fa fa-edit nav_icon"></i>
                        <span class="nav-label">Site</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <li>
                            <a href="graphs.html" class=" hvr-bounce-to-right">
                                <i class="fa fa-code nav_icon"></i>Pages
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</#macro>

<#macro content>
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

<#macro clearfix>
    <div class="clearfix"></div>
</#macro>

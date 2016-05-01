<!DOCTYPE HTML>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <title>${page.@title}</title>

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="/admin/resources/css/style.css" rel='stylesheet' type='text/css'/>
    <link href="/admin/resources/css/custom.css" rel="stylesheet">
    <#if page.resources?? && page.resources.css??>
    <#list page.resources.css as css>
    <link href="${css}.css" rel="stylesheet">
    </#list>
    </#if>
</head>
<body>
    <#if !(page.__user??) || !hasRole("admin")>
        <@unauthContent />
    <#else>
        <@userContent />
    </#if>

    <script src="https://code.jquery.com/jquery-1.12.0.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/metisMenu/2.5.0/metisMenu.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jQuery-slimScroll/1.3.7/jquery.slimscroll.min.js"></script>
    <script src="/admin/resources/js/screenfull.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.nicescroll/3.6.8-fix/jquery.nicescroll.min.js"></script>
    <script src="/admin/resources/js/custom.js"></script>
    <#if page.__user?? && hasRole("admin")>
    <script src="/admin/resources/js/app.js"></script>
    <#if page.resources?? && page.resources.js??>
    <#list page.resources.js as js>
    <script src="${js}.js"></script>
    </#list>
    </#if>
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
                        <#if breadcrumb.@href?? && breadcrumb.@href?has_content>
                        <a href="${breadcrumb.@href}">${breadcrumb}</a>
                        <#else>
                        <span>${breadcrumb}</span>
                        </#if>
                        </#list>
                    </h2>
                </div>

                <@content />
            </div>

            <@footer />
        </div>
    </div>
    <@clearfix />
    <div id="loadingoverlay" class="loadingoverlay"></div>
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
                        <i class="fa fa-home nav_icon"></i>
                        <span class="nav-label<#if page.@id == "home"> text-success</#if>">
                            Home
                        </span>
                    </a>
                </li>

                <#if hasRole("pages") || hasRole("themes")>
                <li>
                    <a href="#" class=" hvr-bounce-to-right">
                        <i class="fa fa-edit nav_icon"></i>
                        <span class="nav-label">Site</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <#if hasRole("pages")>
                        <li>
                            <a href="/admin/site-pages" class="hvr-bounce-to-right">
                                <i class="fa fa-code nav_icon"></i>
                                <span class="nav-label<#if page.@id == "site-pages"> text-success</#if>">
                                    Pages
                                </span>
                            </a>
                        </li>
                        <li>
                            <a href="/admin/site-pages-resources" class="hvr-bounce-to-right">
                                <i class="fa fa-wrench nav_icon"></i>
                                <span class="nav-label<#if page.@id == "site-pages-resources"> text-success</#if>">
                                    Pages Resources
                                </span>
                            </a>
                        </li>
                        <li>
                            <a href="/admin/site-pages-images" class="hvr-bounce-to-right">
                                <i class="fa fa-image nav_icon"></i>
                                <span class="nav-label<#if page.@id == "site-pages-images"> text-success</#if>">
                                    Pages Images
                                </span>
                            </a>
                        </li>
                        </#if>
                        <#if hasRole("themes")>
                        <li>
                            <a href="/admin/site-themes" class="hvr-bounce-to-right">
                                <i class="fa fa-paint-brush nav_icon"></i>
                                <span class="nav-label<#if page.@id == "site-themes"> text-success</#if>">
                                    Theme
                                </span>
                            </a>
                        </li>
                        </#if>
                    </ul>
                </li>
                </#if>
            </ul>
        </div>
    </div>
</#macro>

<#macro content>
    <#if page.@id == "site-pages" && hasRole("pages")>
        <@filesContent title="Pages" listSelector="site-pages-list"
                       fileType="page" fileTypes=["xml"] />
    <#elseif page.@id == "site-pages-resources" && hasRole("pages")>
        <@filesContent title="Pages resources" listSelector="site-pages-resources-list"
                       fileType="page-resource" fileTypes=["javascript", "css", "sass"] />
    <#elseif page.@id == "site-pages-images" && hasRole("pages")>
        <@imagesContent location="page"/>
    <#elseif page.@id == "site-themes" && hasRole("themes")>
        <@filesContent title="Theme resources" listSelector="site-theme-list"
                       fileType="resource" fileTypes=["ftl", "css", "sass", "javascript"] />
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

<#macro filesContent title listSelector fileType fileTypes>
    <div class="inbox-mail">
        <div class="col-md-4 compose">
            <div class="input-group input-group-in">
                <span class="">
                    <button class="btn btn-success not-enabled load-enabled"
                            type="button" data-toggle="modal" data-target="#addModal">
                        <i class="fa fa-plus"></i>
                    </button>
                </span>
            </div>

            <h2>${title}</h2>

            <nav class="nav-sidebar">
                <ul class="nav tabs ${listSelector}"></ul>
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

    <div class="modal fade" id="addModal" tabindex="-1" role="dialog"
         aria-labelledby="addModalLabel" aria-hidden="true"
         style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Add ${fileType}</h4>
                </div>

                <div class="modal-body">
                    <form data-url="/admin/add-${fileType}/">
                        <div class="form-group">
                            <label for="fileName">Name of ${fileType}</label>
                            <input type="text" class="form-control1" id="fileName" required
                                   name="fileName" placeholder="Enter name of ${fileType}" />
                        </div>

                        <div class="form-group">
                            <label for="filePath">Path for ${fileType}</label>
                            <input type="text" class="form-control1" id="filePath"
                                   name="filePath" placeholder="Enter path for ${fileType}" />
                        </div>

                        <div class="form-group form-group2 group-mail">
                            <label>Select file type</label>
                            <select name="fileType" class="form-control1" required>
                                <#list fileTypes as fType>
                                <option value="${fType}">${fType?upper_case}</option>
                                </#list>
                            </select>
                        </div>
                    </form>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-success" id="addFile">Add</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro imagesContent location>
    <div class="banner">
        <span class="">
            <button class="btn btn-primary not-enabled"
                    type="button" data-toggle="modal" data-target="#uploadModal">
                <i class="fa fa-upload"></i>
            </button>
        </span>
    </div>

    <div class="gallery" id="images-container">
        <@clearfix />
    </div>

    <div class="modal fade" id="uploadModal" tabindex="-1" role="dialog"
         aria-labelledby="uploadModalLabel" aria-hidden="true"
         style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Upload image</h4>
                </div>

                <div class="modal-body">
                    <form data-url="/admin/upload-${location}-image/">
                        <div class="form-group">
                            <button class="btn btn-primary" type="button" id="inputImage">
                                <i class="fa fa-upload"></i>
                                Select image
                            </button>
                            <input type="file" accept="image/*"
                                   style="display:none" id="realInputImage">
                        </div>
                        <div id="selectedImage" class="form-group">
                            <p class="bg-danger text-danger">No image selected!</p>
                        </div>

                        <div class="form-group">
                            <label for="imagePath">Path for image</label>
                            <input type="text" class="form-control1" id="imagePath" required
                                   name="imagePath" placeholder="Enter path for image" />
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" id="uploadImage">Upload</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#macro footer>
    <div class="copy">
        <h4>
            <img src="/admin/resources/images/touwolf-ico-144.png" alt=""/>
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

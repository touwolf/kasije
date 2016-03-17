<#ftl encoding="UTF-8">

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="${page.meta.author}" />
    <meta name="description" content="${page.meta.description}" />

    <title>${page.@title}</title>

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.1/animate.min.css" rel="stylesheet">
    <link href="/resources/css/owl.carousel.css" rel="stylesheet">
    <link href="/resources/css/owl.transitions.css" rel="stylesheet">
    <link href="/resources/css/prettyPhoto.css" rel="stylesheet">
    <link href="/resources/css/main.css" rel="stylesheet">
    <link href="/resources/css/responsive.css" rel="stylesheet">

    <#if page.resources??>
    <#list page.resources.css![] as css>
    <link href="${css}" rel="stylesheet" />
    </#list>
    </#if>

    <!--[if lt IE 9]>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <link rel="shortcut icon" href="/resources/images/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="/resources/images/ico/icon-144.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="/resources/images/ico/icon-114.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="/resources/images/ico/icon-72.png">
    <link rel="apple-touch-icon-precomposed" href="/resources/images/ico/icon-57.png">
</head>

<body id="home" class="homepage">
    <#assign teamDisabled = (page.teamDisabled?? && page.teamDisabled?boolean) />
    <#assign blogDisabled = (page.blogDisabled?? && page.blogDisabled?boolean) />
    <#assign pricingDisabled = (page.pricingDisabled?? && page.pricingDisabled?boolean) />
    <#assign twitterDisabled = (page.twitterDisabled?? && page.twitterDisabled?boolean) />

    <@header />
    <@features />
    <@services />
    <@portfolio />
    <@about />
    <#if !teamDisabled>
    <@team />
    </#if>
    <#if !pricingDisabled>
    <@pricing />
    </#if>
    <#if !blogDisabled>
    <@blog />
    </#if>
    <@contact />
    <@footer />

    <script src="https://code.jquery.com/jquery-1.12.0.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <script src="http://maps.google.com/maps/api/js?key=${page.contact.geolocation.@apiKey}"></script>
    <script src="/resources/js/owl.carousel.js"></script>
    <script src="/resources/js/mousescroll.js"></script>
    <script src="/resources/js/smoothscroll.js"></script>
    <script src="/resources/js/jquery.prettyPhoto.js"></script>
    <script src="/resources/js/jquery.isotope.js"></script>
    <script src="/resources/js/jquery.inview.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/wow/1.1.2/wow.min.js"></script>
    <script src="/resources/js/main.js"></script>
    <#if page.resources??>
    <#list page.resources.js![] as js>
    <script src="${js}"></script>
    </#list>
    </#if>
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

                    <a class="navbar-brand" href="#">
                        <img src="${page.resources.logo}" alt="logo">
                    </a>
                </div>

                <div class="collapse navbar-collapse navbar-right">
                    <ul class="nav navbar-nav">
                        <li class="scroll active"><a href="#home">${page.text.home!"Home"}</a></li>
                        <li class="scroll"><a href="#features">${page.text.features!"Features"}</a></li>
                        <li class="scroll"><a href="#services">${page.text.services!"Services"}</a></li>
                        <li class="scroll"><a href="#portfolio">${page.text.portfolio!"Portfolio"}</a></li>
                        <li class="scroll"><a href="#about">${page.text.about!"About"}</a></li>
                        <#if !teamDisabled>
                        <li class="scroll"><a href="#meet-team">${page.text.team!"Team"}</a></li>
                        </#if>
                        <#if !pricingDisabled>
                        <li class="scroll"><a href="#pricing">${page.text.pricing!"Pricing"}</a></li>
                        </#if>
                        <#if !blogDisabled>
                        <li class="scroll"><a href="#blog">${page.text.blog!"Blog"}</a></li>
                        </#if>
                        <li class="scroll"><a href="#get-in-touch">${page.text.contact!"Contact"}</a></li>
                    </ul>
                </div>
            </div>
        </nav>
    </header>

    <section id="main-slider">
        <div class="owl-carousel">
            <#list page.carousel.slide as slide>
            <div class="item" style="background-image: url(${slide.image});">
                <div class="slider-inner">
                    <div class="container">
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="carousel-content">
                                    <h2>${slide.title}</h2>
                                    <p>${slide.description}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            </#list>
        </div>
    </section>
</#macro>

<#macro features>
    <section id="features">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title text-center wow fadeInDown">${page.features.title}</h2>
                <p class="text-center wow fadeInDown">${page.features.description}</p>
            </div>

            <div class="row">
                <div class="col-sm-6 wow fadeInLeft">
                    <img class="img-responsive" src="${page.features.image}" alt="">
                </div>

                <div class="col-sm-6">
                    <#list page.features.item as item>
                    <div class="media service-box wow fadeInRight">
                        <div class="pull-left">
                            <i class="fa ${item.faIcon}"></i>
                        </div>
                        <div class="media-body">
                            <h4 class="media-heading">${item.title}</h4>
                            <p>${item.description}</p>
                        </div>
                    </div>
                    </#list>
                </div>
            </div>
        </div>
    </section>

    <section id="cta2">
        <div class="container">
            <div class="text-center">
                <h2 class="wow fadeInUp" data-wow-duration="300ms" data-wow-delay="0ms">
                    ${page.features.banner.title}
                </h2>
                <p class="wow fadeInUp" data-wow-duration="300ms" data-wow-delay="100ms">
                    ${page.features.banner.description}
                </p>

                <img class="img-responsive wow fadeIn" src="${page.features.banner.image}" alt="" data-wow-duration="300ms" data-wow-delay="300ms">
            </div>
        </div>
    </section>
</#macro>

<#macro services>
    <section id="services" >
        <div class="container">
            <div class="section-header">
                <h2 class="section-title text-center wow fadeInDown">
                    ${page.services.title}
                </h2>
                <p class="text-center wow fadeInDown">
                    ${page.services.description}
                </p>
            </div>

            <div class="row">
                <div class="features">
                    <#list page.services.item?chunk(3) as items>
                    <#list items as item>
                    <div class="col-md-4 col-sm-6 wow fadeInUp" data-wow-duration="300ms" data-wow-delay="0ms">
                        <div class="media service-box">
                            <div class="pull-left">
                                <i class="fa ${item.faIcon}"></i>
                            </div>
                            <div class="media-body">
                                <h4 class="media-heading">${item.title}</h4>
                                <p>${item.description}</p>
                            </div>
                        </div>
                    </div>
                    </#list>
                    </#list>
                </div>
            </div>
        </div>
    </section>
</#macro>

<#macro portfolio>
    <section id="portfolio">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title text-center wow fadeInDown">
                    ${page.portfolio.title}
                </h2>
                <p class="text-center wow fadeInDown">
                    ${page.portfolio.description}
                </p>
            </div>

            <div class="text-center">
                <ul class="portfolio-filter">
                    <li><a class="active" href="#" data-filter="*">${page.text.allWorks!"All Works"}</a></li>
                    <#assign categories = {} />
                    <#list page.portfolio.item as item>
                    <#if !(categories[item.category]??)>
                        <#assign categories = categories + {item.category: true} />
                        <li><a href="#" data-filter=".${item.category}">${item.category?cap_first}</a></li>
                    </#if>
                    </#list>
                </ul>
            </div>

            <div class="portfolio-items">
                <#list page.portfolio.item as item>
                <div class="portfolio-item ${item.category}">
                    <div class="portfolio-item-inner">
                        <img class="img-responsive" src="${item.image}" alt="">
                        <div class="portfolio-info">
                            <h3>${item.title}</h3>
                            ${item.description}
                            <a class="preview" href="${item.url}" target="_blank" rel="prettyPhoto"><i class="fa fa-eye"></i></a>
                        </div>
                    </div>
                </div>
                </#list>
            </div>
        </div>
    </section>
</#macro>

<#macro about>
    <section id="about">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title text-center wow fadeInDown">${page.about.title}</h2>
                <#list page.about.description as description>
                <p class="text-center wow fadeInDown">${description}</p>
                </#list>
            </div>

            <divclass="row">
                <#--div class="col-sm-6 wow fadeInLeft">
                    <h3 class="column-title">Video Intro</h3>

                    <div class="embed-responsive embed-responsive-16by9">
                        <iframe src="//player.vimeo.com/video/58093852?title=0&amp;byline=0&amp;portrait=0&amp;color=e79b39" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>
                    </div>
                </div-->
                <div class="col-sm-6">
                    <h3 class="column-title">${page.text.skills!"Our Skills"}</h3>
                    <#list page.about.skill as skill>
                    <strong>${skill}</strong>
                    <div class="progress">
                        <div class="progress-bar progress-bar-primary" role="progressbar" data-width="${skill.@value}">${skill.@value}%</div>
                    </div>
                    </#list>
                </div>

                <div class="col-sm-6 wow fadeInRight">
                    <h3 class="column-title">Multi Capability</h3>
                    <p>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.</p>

                    <p>Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.</p>

                    <div class="row">
                        <div class="col-sm-6">
                            <ul class="nostyle">
                                <li><i class="fa fa-check-square"></i> Ipsum is simply dummy</li>
                                <li><i class="fa fa-check-square"></i> When an unknown</li>
                            </ul>
                        </div>

                        <div class="col-sm-6">
                            <ul class="nostyle">
                                <li><i class="fa fa-check-square"></i> The printing and typesetting</li>
                                <li><i class="fa fa-check-square"></i> Lorem Ipsum has been</li>
                            </ul>
                        </div>
                    </div>

                    <a class="btn btn-primary" href="#">Learn More</a>
                </div>
            </div>
        </div>
    </section>

    <section id="work-process">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title text-center wow fadeInDown">${page.process.title}</h2>
                <p class="text-center wow fadeInDown">${page.process.description}</p>
            </div>

            <div class="row text-center">
                <#list page.process.item as item>
                <div class="col-md-2 col-md-4 col-xs-6">
                    <div class="wow fadeInUp" data-wow-duration="400ms" data-wow-delay="0ms">
                        <div class="icon-circle">
                            <span>${item?index + 1}</span>
                            <i class="fa ${item.faIcon} fa-2x"></i>
                        </div>
                        <h3>${item.title}</h3>
                    </div>
                </div>
                </#list>
            </div>
        </div>
    </section>
</#macro>

<#macro team>
    <section id="meet-team">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title text-center wow fadeInDown">${page.team.title}</h2>
                <p class="text-center wow fadeInDown">${page.team.description}</p>
            </div>

            <div class="row">
                <#list page.team.item as item>
                <div class="col-sm-6 col-md-3">
                    <div class="team-member wow fadeInUp" data-wow-duration="400ms" data-wow-delay="0ms">
                        <div class="team-img">
                            <img class="img-responsive" src="${item.image}" alt="">
                        </div>
                        <div class="team-info">
                            <h3>${item.name}</h3>
                            <span>${item.title}</span>
                        </div>
                        <p>${item.description}</p>
                        <ul class="social-icons">
                            <#list item.social?children as social>
                            <#if social?node_type != "text">
                            <li>
                                <a href="${social}" target="_blank">
                                    <i class="fa fa-${social?node_name}"></i>
                                </a>
                            </li>
                            </#if>
                            </#list>
                        </ul>
                    </div>
                </div>
                </#list>
            </div>
        </div>
    </section>

    <section id="animated-number">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title text-center wow fadeInDown">Fun Facts</h2>
                <p class="text-center wow fadeInDown">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut <br> et dolore magna aliqua. Ut enim ad minim veniam</p>
            </div>

            <div class="row text-center">
                <div class="col-sm-3 col-xs-6">
                    <div class="wow fadeInUp" data-wow-duration="400ms" data-wow-delay="0ms">
                        <div class="animated-number" data-digit="2305" data-duration="1000"></div>
                        <strong>CUPS OF COFFEE CONSUMED</strong>
                    </div>
                </div>
                <div class="col-sm-3 col-xs-6">
                    <div class="wow fadeInUp" data-wow-duration="400ms" data-wow-delay="100ms">
                        <div class="animated-number" data-digit="1231" data-duration="1000"></div>
                        <strong>CLIENT WORKED WITH</strong>
                    </div>
                </div>
                <div class="col-sm-3 col-xs-6">
                    <div class="wow fadeInUp" data-wow-duration="400ms" data-wow-delay="200ms">
                        <div class="animated-number" data-digit="3025" data-duration="1000"></div>
                        <strong>PROJECT COMPLETED</strong>
                    </div>
                </div>
                <div class="col-sm-3 col-xs-6">
                    <div class="wow fadeInUp" data-wow-duration="400ms" data-wow-delay="300ms">
                        <div class="animated-number" data-digit="1199" data-duration="1000"></div>
                        <strong>QUESTIONS ANSWERED</strong>
                    </div>
                </div>
            </div>
        </div>
    </section>
</#macro>

<#macro pricing>
    <section id="pricing">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title text-center wow fadeInDown">Pricing Table</h2>
                <p class="text-center wow fadeInDown">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut <br> et dolore magna aliqua. Ut enim ad minim veniam</p>
            </div>

            <div class="row">
                <div class="col-sm-6 col-md-3">
                    <div class="wow zoomIn" data-wow-duration="400ms" data-wow-delay="0ms">
                        <ul class="pricing">
                            <li class="plan-header">
                                <div class="price-duration">
                                            <span class="price">
                                                $39
                                            </span>
                                            <span class="duration">
                                                per month
                                            </span>
                                </div>

                                <div class="plan-name">
                                    Starter
                                </div>
                            </li>
                            <li><strong>1</strong> DOMAIN</li>
                            <li><strong>100GB</strong> DISK SPACE</li>
                            <li><strong>UNLIMITED</strong> BANDWIDTH</li>
                            <li>SHARED SSL CERTIFICATE</li>
                            <li><strong>10</strong> EMAIL ADDRESS</li>
                            <li><strong>24/7</strong> SUPPORT</li>
                            <li class="plan-purchase"><a class="btn btn-primary" href="#">ORDER NOW</a></li>
                        </ul>
                    </div>
                </div>
                <div class="col-sm-6 col-md-3">
                    <div class="wow zoomIn" data-wow-duration="400ms" data-wow-delay="200ms">
                        <ul class="pricing featured">
                            <li class="plan-header">
                                <div class="price-duration">
                                            <span class="price">
                                                $69
                                            </span>
                                            <span class="duration">
                                                per month
                                            </span>
                                </div>

                                <div class="plan-name">
                                    Business
                                </div>
                            </li>
                            <li><strong>3</strong> DOMAIN</li>
                            <li><strong>300GB</strong> DISK SPACE</li>
                            <li><strong>UNLIMITED</strong> BANDWIDTH</li>
                            <li>SHARED SSL CERTIFICATE</li>
                            <li><strong>30</strong> EMAIL ADDRESS</li>
                            <li><strong>24/7</strong> SUPPORT</li>
                            <li class="plan-purchase"><a class="btn btn-default" href="#">ORDER NOW</a></li>
                        </ul>
                    </div>
                </div>
                <div class="col-sm-6 col-md-3">
                    <div class="wow zoomIn" data-wow-duration="400ms" data-wow-delay="400ms">
                        <ul class="pricing">
                            <li class="plan-header">
                                <div class="price-duration">
                                            <span class="price">
                                                $99
                                            </span>
                                            <span class="duration">
                                                per month
                                            </span>
                                </div>

                                <div class="plan-name">
                                    Pro
                                </div>
                            </li>
                            <li><strong>5</strong> DOMAIN</li>
                            <li><strong>500GB</strong> DISK SPACE</li>
                            <li><strong>UNLIMITED</strong> BANDWIDTH</li>
                            <li>SHARED SSL CERTIFICATE</li>
                            <li><strong>50</strong> EMAIL ADDRESS</li>
                            <li><strong>24/7</strong> SUPPORT</li>
                            <li class="plan-purchase"><a class="btn btn-primary" href="#">ORDER NOW</a></li>
                        </ul>
                    </div>
                </div>
                <div class="col-sm-6 col-md-3">
                    <div class="wow zoomIn" data-wow-duration="400ms" data-wow-delay="600ms">
                        <ul class="pricing">
                            <li class="plan-header">
                                <div class="price-duration">
                                            <span class="price">
                                                $199
                                            </span>
                                            <span class="duration">
                                                per month
                                            </span>
                                </div>

                                <div class="plan-name">
                                    Ultra
                                </div>
                            </li>
                            <li><strong>10</strong> DOMAIN</li>
                            <li><strong>1000GB</strong> DISK SPACE</li>
                            <li><strong>UNLIMITED</strong> BANDWIDTH</li>
                            <li>SHARED SSL CERTIFICATE</li>
                            <li><strong>100</strong> EMAIL ADDRESS</li>
                            <li><strong>24/7</strong> SUPPORT</li>
                            <li class="plan-purchase"><a class="btn btn-primary" href="#">ORDER NOW</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </section>
</#macro>

<#macro blog>
    <section id="blog">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title text-center wow fadeInDown">Latest Blogs</h2>
                <p class="text-center wow fadeInDown">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut <br> et dolore magna aliqua. Ut enim ad minim veniam</p>
            </div>

            <div class="row">
                <div class="col-sm-6">
                    <div class="blog-post blog-large wow fadeInLeft" data-wow-duration="300ms" data-wow-delay="0ms">
                        <article>
                            <header class="entry-header">
                                <div class="entry-thumbnail">
                                    <img class="img-responsive" src="/resources/images/blog/01.jpg" alt="">
                                    <span class="post-format post-format-video"><i class="fa fa-film"></i></span>
                                </div>
                                <div class="entry-date">25 November 2014</div>
                                <h2 class="entry-title"><a href="#">While now the fated Pequod had been so long afloat this</a></h2>
                            </header>

                            <div class="entry-content">
                                <P>With a blow from the top-maul Ahab knocked off the steel head of the lance, and then handing to the mate the long iron rod remaining, bade him hold it upright, without its touching off the steel head of the lance, and then handing to the mate the long iron rod remaining. without its touching off the steel without its touching off the steel</P>
                                <a class="btn btn-primary" href="#">Read More</a>
                            </div>

                            <footer class="entry-meta">
                                <span class="entry-author"><i class="fa fa-pencil"></i> <a href="#">Victor</a></span>
                                <span class="entry-category"><i class="fa fa-folder-o"></i> <a href="#">Tutorial</a></span>
                                <span class="entry-comments"><i class="fa fa-comments-o"></i> <a href="#">15</a></span>
                            </footer>
                        </article>
                    </div>
                </div><!--/.col-sm-6-->
                <div class="col-sm-6">
                    <div class="blog-post blog-media wow fadeInRight" data-wow-duration="300ms" data-wow-delay="100ms">
                        <article class="media clearfix">
                            <div class="entry-thumbnail pull-left">
                                <img class="img-responsive" src="/resources/images/blog/02.jpg" alt="">
                                <span class="post-format post-format-gallery"><i class="fa fa-image"></i></span>
                            </div>
                            <div class="media-body">
                                <header class="entry-header">
                                    <div class="entry-date">01 December 2014</div>
                                    <h2 class="entry-title"><a href="#">BeReviews was a awesome envent in dhaka</a></h2>
                                </header>

                                <div class="entry-content">
                                    <P>With a blow from the top-maul Ahab knocked off the steel head of the lance, and then handing to the steel</P>
                                    <a class="btn btn-primary" href="#">Read More</a>
                                </div>

                                <footer class="entry-meta">
                                    <span class="entry-author"><i class="fa fa-pencil"></i> <a href="#">Campbell</a></span>
                                    <span class="entry-category"><i class="fa fa-folder-o"></i> <a href="#">Tutorial</a></span>
                                    <span class="entry-comments"><i class="fa fa-comments-o"></i> <a href="#">15</a></span>
                                </footer>
                            </div>
                        </article>
                    </div>
                    <div class="blog-post blog-media wow fadeInRight" data-wow-duration="300ms" data-wow-delay="200ms">
                        <article class="media clearfix">
                            <div class="entry-thumbnail pull-left">
                                <img class="img-responsive" src="/resources/images/blog/03.jpg" alt="">
                                <span class="post-format post-format-audio"><i class="fa fa-music"></i></span>
                            </div>
                            <div class="media-body">
                                <header class="entry-header">
                                    <div class="entry-date">03 November 2014</div>
                                    <h2 class="entry-title"><a href="#">Play list of old bangle  music and gajal</a></h2>
                                </header>

                                <div class="entry-content">
                                    <P>With a blow from the top-maul Ahab knocked off the steel head of the lance, and then handing to the steel</P>
                                    <a class="btn btn-primary" href="#">Read More</a>
                                </div>

                                <footer class="entry-meta">
                                    <span class="entry-author"><i class="fa fa-pencil"></i> <a href="#">Ruth</a></span>
                                    <span class="entry-category"><i class="fa fa-folder-o"></i> <a href="#">Tutorial</a></span>
                                    <span class="entry-comments"><i class="fa fa-comments-o"></i> <a href="#">15</a></span>
                                </footer>
                            </div>
                        </article>
                    </div>
                </div>
            </div>

        </div>
    </section>
</#macro>

<#macro contact>
    <section id="get-in-touch">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title text-center wow fadeInDown">${page.contact.title}</h2>
                <p class="text-center wow fadeInDown">${page.contact.description}</p>
            </div>
        </div>
    </section>

    <section id="contact">
        <div id="google-map" style="height:650px"
             data-latitude="${page.contact.geolocation.@latitude}"
             data-longitude="${page.contact.geolocation.@longitude}"></div>

        <div class="container-wrapper">
            <div class="container">
                <div class="row">
                    <div class="col-sm-4 col-sm-offset-8">
                        <div class="contact-form">
                            <address>
                                <strong>Twitter, Inc.</strong><br>
                                ${page.contact.address}<br>
                                ${page.contact.email}<br>
                                ${page.contact.phone}
                            </address>

                            <form id="main-contact-form" name="contact-form" method="post" action="#">
                                <div class="form-group">
                                    <input type="text" name="name" class="form-control" placeholder="${page.text.name}" required>
                                </div>
                                <div class="form-group">
                                    <input type="email" name="email" class="form-control" placeholder="${page.text.email}" required>
                                </div>
                                <div class="form-group">
                                    <input type="text" name="subject" class="form-control" placeholder="${page.text.subject}" required>
                                </div>
                                <div class="form-group">
                                    <textarea name="message" class="form-control" rows="8"
                                              placeholder="${page.text.enterMessage}" required></textarea>
                                </div>

                                <button type="submit" class="btn btn-primary">${page.text.sendNow}</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</#macro>

<#macro footer>
    <footer id="footer">
        <div class="container">
            <div class="row">
                <div class="col-sm-6">
                    &copy; ${.now?string["yyyy"]} ${page.meta.author}
                </div>

                <div class="col-sm-6">
                    <ul class="social-icons">
                        <#list page.social?children as social>
                            <#if social?node_type != "text">
                                <li><a href="${social}" target="_blank">
                                    <i class="fa fa-${social?node_name}"></i>
                                </a></li>
                            </#if>
                        </#list>
                    </ul>
                </div>
            </div>
        </div>
    </footer>
</#macro>

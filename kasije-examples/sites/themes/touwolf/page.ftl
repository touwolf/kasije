<#ftl encoding="UTF-8">

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="author" content="${page.meta.author}" />
    <meta name="description" content="${page.meta.description}" />

    <title>${page.@title}</title>

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" />
    <link href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.1/animate.min.css" rel="stylesheet" />
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet" />
    <link href="https://cdnjs.cloudflare.com/ajax/libs/lightbox2/2.8.2/css/lightbox.min.css" rel="stylesheet" />
    <link href="/resources/css/main.css" rel="stylesheet" />

    <link href="/resources/css/presets/preset${random(1, 4)}.css" rel="stylesheet" />
    <link href="/resources/css/responsive.css" rel="stylesheet" />

    <#if page.resources??>
    <#list page.resources.css![] as css>
    <link href="${css}" rel="stylesheet" />
    </#list>
    </#if>

    <!--[if lt IE 9]>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <link href="http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700" rel="stylesheet" type="text/css" />
    <link rel="shortcut icon" href="${page.resources.favicon!}" />
</head>
<body>
    <div class="preloader">
        <i class="fa fa-circle-o-notch fa-spin"></i>
    </div>

    <#assign teamDisabled = (page.teamDisabled?? && page.teamDisabled?boolean) />
    <#assign blogDisabled = (page.blogDisabled?? && page.blogDisabled?boolean) />
    <#assign pricingDisabled = (page.pricingDisabled?? && page.pricingDisabled?boolean) />
    <#assign twitterDisabled = (page.twitterDisabled?? && page.twitterDisabled?boolean) />

    <@header />
    <@services />
    <@about />
    <@portfolio />
    <#if !teamDisabled>
    <@team />
    </#if>
    <@features />
    <#if !pricingDisabled>
    <@pricing />
    </#if>
    <#if !twitterDisabled>
    <@twitter />
    </#if>
    <#if !blogDisabled>
    <@blog />
    </#if>
    <@contact />
    <@footer />

    <script src="https://code.jquery.com/jquery-1.12.0.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <script src="https://maps.google.com/maps/api/js?key=${page.contact.geolocation.@apiKey}"></script>
    <script src="https://cdn.jsdelivr.net/jquery.inview/0.2/jquery.inview.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/wow/1.1.2/wow.min.js"></script>
    <script src="/resources/js/mousescroll.js"></script>
    <script src="/resources/js/smoothscroll.js"></script>
    <script src="/resources/js/jquery.countTo.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/lightbox2/2.8.2/js/lightbox.min.js"></script>
    <script src="/resources/js/main.js"></script>
    <#if page.resources??>
    <#list page.resources.js![] as js>
    <script src="${js}"></script>
    </#list>
    </#if>
</body>
</html>

<#macro header>
    <header id="home">
        <div id="home-slider" class="carousel slide carousel-fade" data-ride="carousel">
            <div class="carousel-inner">
                <#list page.carousel.slide as slide>
                <div class="item<#if slide?is_first> active</#if>" style="background-image: url(${slide.image})">
                    <div class="caption">
                        <h1 class="animated fadeInLeftBig">${slide.title}</h1>
                        <p class="animated fadeInRightBig">${slide.description}</p>
                        <a data-scroll class="btn btn-start animated fadeInUpBig" href="#services">
                            ${page.text.start!"Start now"}
                        </a>
                    </div>
                </div>
                </#list>
            </div>

            <a class="left-control" href="#home-slider" data-slide="prev"><i class="fa fa-angle-left"></i></a>
            <a class="right-control" href="#home-slider" data-slide="next"><i class="fa fa-angle-right"></i></a>

            <a id="tohash" href="#services"><i class="fa fa-angle-down"></i></a>
        </div>

        <div class="main-nav">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>

                    <a class="navbar-brand" href="#">
                        <h1><img class="img-responsive" src="${page.resources.logo}" alt="logo"></h1>
                    </a>
                </div>
                <div class="collapse navbar-collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <li class="scroll active"><a href="#home">${page.text.home!"Home"}</a></li>
                        <li class="scroll"><a href="#services">${page.text.services!"Service"}</a></li>
                        <li class="scroll"><a href="#about-us">${page.text.about!"About Us"}</a></li>
                        <li class="scroll"><a href="#portfolio">${page.text.portfolio!"Portfolio"}</a></li>
                        <#if !teamDisabled>
                        <li class="scroll"><a href="#team">${page.text.team!"Team"}</a></li>
                        </#if>
                        <#if !blogDisabled>
                        <li class="scroll"><a href="#blog">${page.text.blog!"Blog"}</a></li>
                        </#if>
                        <li class="scroll"><a href="#contact">${page.text.contact!"Contact"}</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </header>
</#macro>

<#macro services>
    <section id="services">
        <div class="container">
            <div class="heading wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="300ms">
                <div class="row">
                    <div class="text-center col-sm-8 col-sm-offset-2">
                        <h2>${page.services.title}</h2>
                        <p>${page.services.description}</p>
                    </div>
                </div>
            </div>

            <div class="text-center our-services">
                <div class="row">
                    <#assign delayBase = 300 />
                    <#assign delayStep = 100 />
                    <#list page.services.item?chunk(3) as items>
                        <#list items as item>
                            <div class="col-sm-4 wow fadeInDown" data-wow-duration="1000ms"
                                 data-wow-delay="${delayBase + (item?index * delayStep)}ms">
                                <div class="service-icon">
                                    <i class="fa fa-${item.faIcon}"></i>
                                </div>
                                <div class="service-info">
                                    <h3>${item.title}</h3>
                                    <p>${item.description}</p>
                                </div>
                            </div>
                        </#list>
                    </#list>
                </div>
            </div>
        </div>
    </section>
</#macro>

<#macro about>
    <section id="about-us" class="parallax">
        <div class="container">
            <div class="row">
                <div class="col-sm-6">
                    <div class="about-info wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="300ms">
                        <h2>${page.about.title}</h2>
                        <#list page.about.description as description>
                        <p>${description}</p>
                        </#list>
                    </div>
                </div>

                <div class="col-sm-6">
                    <#assign delayBase = 300 />
                    <#assign delayStep = 100 />
                    <div class="our-skills wow fadeInDown" data-wow-duration="1000ms" data-wow-delay="300ms">
                        <#list page.about.skill as skill>
                        <div class="single-skill wow fadeInDown" data-wow-duration="1000ms"
                             data-wow-delay="${delayBase + (skill?index * delayStep)}ms">
                            <p class="lead">${skill}</p>
                            <div class="progress">
                                <div class="progress-bar progress-bar-primary six-sec-ease-in-out" role="progressbar"
                                     aria-valuetransitiongoal="${skill.@value}">${skill.@value}%</div>
                            </div>
                        </div>
                        </#list>
                    </div>
                </div>
            </div>
        </div>
    </section>
</#macro>

<#macro portfolio>
    <section id="portfolio">
        <div class="container">
            <div class="row">
                <div class="heading text-center col-sm-8 col-sm-offset-2 wow fadeInUp"
                     data-wow-duration="1000ms" data-wow-delay="300ms">
                    <h2>${page.portfolio.title}</h2>
                    <p>${page.portfolio.description}</p>
                </div>
            </div>
        </div>

        <div class="container-fluid">
            <div class="row">
                <#assign delayBase = 300 />
                <#assign delayStep = 100 />
                <#list page.portfolio.item as item>
                <div class="col-sm-3">
                    <div class="folio-item wow <#if item?is_even_item>fadeInRightBig<#else>fadeInLeftBig</#if>"
                         data-wow-duration="1000ms" data-wow-delay="${delayBase + (item?index * delayStep)}ms">
                        <div class="folio-image">
                            <img class="img-responsive" src="${item.image}" alt="">
                        </div>

                        <div class="overlay">
                            <div class="overlay-content">
                                <div class="overlay-text">
                                    <div class="folio-info">
                                        <h3>${item.title}</h3>
                                        <p>${item.description}</p>
                                    </div>

                                    <div class="folio-overview">
                                        <span class="folio-link">
                                            <a href="${item.url}" target="_blank" >
                                                <i class="fa fa-link"></i>
                                            </a>
                                        </span>

                                        <span class="folio-expand">
                                            <a href="${item.detailImage}" data-lightbox="portfolio">
                                                <i class="fa fa-search-plus"></i>
                                            </a>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </#list>
            </div>
        </div>

        <div id="portfolio-single-wrap">
            <div id="portfolio-single"></div>
        </div>
    </section>
</#macro>

<#macro team>
    <section id="team">
        <div class="container">
            <div class="row">
                <div class="heading text-center col-sm-8 col-sm-offset-2 wow fadeInUp"
                     data-wow-duration="1200ms" data-wow-delay="300ms">
                    <h2>${page.team.title}</h2>
                    <p>${page.team.description}</p>
                </div>
            </div>

            <div class="team-members">
                <div class="row">
                    <#assign delayBase = 300 />
                    <#assign delayStep = 300 />
                    <#list page.team.item as item>
                    <div class="col-sm-3">
                        <div class="team-member wow flipInY" data-wow-duration="1000ms"
                             data-wow-delay="${delayBase + (item?index * delayStep)}ms">
                            <div class="member-image">
                                <img class="img-responsive" src="${item.image}" alt="">
                            </div>

                            <div class="member-info">
                                <h3>${item.name}</h3>
                                <h4>${item.title}</h4>
                                <p>${item.description}</p>
                            </div>

                            <div class="social-icons">
                                <ul>
                                    <#list item.social?children as social>
                                    <#if social?node_type != "text">
                                    <li>
                                        <a class="${social?node_name}" href="${social}">
                                            <i class="fa fa-${social?node_name}"></i>
                                        </a>
                                    </li>
                                    </#if>
                                    </#list>
                                </ul>
                            </div>
                        </div>
                    </div>
                    </#list>
                </div>
            </div>
        </div>
    </section>
</#macro>

<#macro features>
    <section id="features" class="parallax">
        <div class="container">
            <div class="row count">
                <#assign delayBase = 300 />
                <#assign delayStep = 200 />
                <#list page.features.item as item>
                <div class="col-sm-3 col-xs-6 wow fadeInLeft" data-wow-duration="1000ms"
                     data-wow-delay="${delayBase + (item?index * delayStep)}ms">
                    <i class="fa fa-${item.faIcon}"></i>
                    <h3<#if item.value?is_number> class="timer"</#if>>${item.value}</h3>
                    <p>${item.title}</p>
                </div>
                </#list>
            </div>
        </div>
    </section>
</#macro>

<#macro pricing>
    <section id="pricing">
        <div class="container">
            <div class="row">
                <div class="heading text-center col-sm-8 col-sm-offset-2 wow fadeInUp"
                     data-wow-duration="1200ms" data-wow-delay="300ms">
                    <h2>Pricing Table</h2>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua ut enim ad minim veniam</p>
                </div>
            </div>
            <div class="pricing-table">
                <div class="row">
                    <div class="col-sm-3">
                        <div class="single-table wow flipInY" data-wow-duration="1000ms" data-wow-delay="300ms">
                            <h3>Basic</h3>
                            <div class="price">
                                $9<span>/Month</span>
                            </div>
                            <ul>
                                <li>Free Setup</li>
                                <li>10GB Storage</li>
                                <li>100GB Bandwith</li>
                                <li>5 Products</li>
                            </ul>
                            <a href="#" class="btn btn-lg btn-primary">Sign up</a>
                        </div>
                    </div>
                    <div class="col-sm-3">
                        <div class="single-table wow flipInY" data-wow-duration="1000ms" data-wow-delay="500ms">
                            <h3>Standard</h3>
                            <div class="price">
                                $19<span>/Month</span>
                            </div>
                            <ul>
                                <li>Free Setup</li>
                                <li>10GB Storage</li>
                                <li>100GB Bandwith</li>
                                <li>5 Products</li>
                            </ul>
                            <a href="#" class="btn btn-lg btn-primary">Sign up</a>
                        </div>
                    </div>
                    <div class="col-sm-3">
                        <div class="single-table featured wow flipInY" data-wow-duration="1000ms" data-wow-delay="800ms">
                            <h3>Featured</h3>
                            <div class="price">
                                $29<span>/Month</span>
                            </div>
                            <ul>
                                <li>Free Setup</li>
                                <li>10GB Storage</li>
                                <li>100GB Bandwith</li>
                                <li>5 Products</li>
                            </ul>
                            <a href="#" class="btn btn-lg btn-primary">Sign up</a>
                        </div>
                    </div>
                    <div class="col-sm-3">
                        <div class="single-table wow flipInY" data-wow-duration="1000ms" data-wow-delay="1100ms">
                            <h3>Professional</h3>
                            <div class="price">
                                $49<span>/Month</span>
                            </div>
                            <ul>
                                <li>Free Setup</li>
                                <li>10GB Storage</li>
                                <li>100GB Bandwith</li>
                                <li>5 Products</li>
                            </ul>
                            <a href="#" class="btn btn-lg btn-primary">Sign up</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</#macro>

<#macro twitter>
    <section id="twitter" class="parallax">
        <div>
            <a class="twitter-left-control" href="#twitter-carousel" role="button" data-slide="prev"><i class="fa fa-angle-left"></i></a>
            <a class="twitter-right-control" href="#twitter-carousel" role="button" data-slide="next"><i class="fa fa-angle-right"></i></a>
            <div class="container">
                <div class="row">
                    <div class="col-sm-8 col-sm-offset-2">
                        <div class="twitter-icon text-center">
                            <i class="fa fa-twitter"></i>
                            <h4>Themeum</h4>
                        </div>
                        <div id="twitter-carousel" class="carousel slide" data-ride="carousel">
                            <div class="carousel-inner">
                                <div class="item active wow fadeIn" data-wow-duration="1000ms" data-wow-delay="300ms">
                                    <p>Introducing Shortcode generator for Helix V2 based templates <a href="#"><span>#helixframework #joomla</span> http://bit.ly/1qlgwav</a></p>
                                </div>
                                <div class="item">
                                    <p>Introducing Shortcode generator for Helix V2 based templates <a href="#"><span>#helixframework #joomla</span> http://bit.ly/1qlgwav</a></p>
                                </div>
                                <div class="item">
                                    <p>Introducing Shortcode generator for Helix V2 based templates <a href="#"><span>#helixframework #joomla</span> http://bit.ly/1qlgwav</a></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</#macro>

<#macro blog>
    <section id="blog">
        <div class="container">
            <div class="row">
                <div class="heading text-center col-sm-8 col-sm-offset-2 wow fadeInUp" data-wow-duration="1200ms" data-wow-delay="300ms">
                    <h2>Blog Posts</h2>
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua ut enim ad minim veniam</p>
                </div>
            </div>
            <div class="blog-posts">
                <div class="row">
                    <div class="col-sm-4 wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="400ms">
                        <div class="post-thumb">
                            <a href="#"><img class="img-responsive" src="/resources/images/blog/1.jpg" alt=""></a>
                            <div class="post-meta">
                                <span><i class="fa fa-comments-o"></i> 3 Comments</span>
                                <span><i class="fa fa-heart"></i> 0 Likes</span>
                            </div>
                            <div class="post-icon">
                                <i class="fa fa-pencil"></i>
                            </div>
                        </div>
                        <div class="entry-header">
                            <h3><a href="#">Lorem ipsum dolor sit amet consectetur adipisicing elit</a></h3>
                            <span class="date">June 26, 2014</span>
                            <span class="cetagory">in <strong>Photography</strong></span>
                        </div>
                        <div class="entry-content">
                            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. </p>
                        </div>
                    </div>
                    <div class="col-sm-4 wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="600ms">
                        <div class="post-thumb">
                            <div id="post-carousel"  class="carousel slide" data-ride="carousel">
                                <ol class="carousel-indicators">
                                    <li data-target="#post-carousel" data-slide-to="0" class="active"></li>
                                    <li data-target="#post-carousel" data-slide-to="1"></li>
                                    <li data-target="#post-carousel" data-slide-to="2"></li>
                                </ol>
                                <div class="carousel-inner">
                                    <div class="item active">
                                        <a href="#"><img class="img-responsive" src="/resources/images/blog/2.jpg" alt=""></a>
                                    </div>
                                    <div class="item">
                                        <a href="#"><img class="img-responsive" src="/resources/images/blog/1.jpg" alt=""></a>
                                    </div>
                                    <div class="item">
                                        <a href="#"><img class="img-responsive" src="/resources/images/blog/3.jpg" alt=""></a>
                                    </div>
                                </div>
                                <a class="blog-left-control" href="#post-carousel" role="button" data-slide="prev"><i class="fa fa-angle-left"></i></a>
                                <a class="blog-right-control" href="#post-carousel" role="button" data-slide="next"><i class="fa fa-angle-right"></i></a>
                            </div>
                            <div class="post-meta">
                                <span><i class="fa fa-comments-o"></i> 3 Comments</span>
                                <span><i class="fa fa-heart"></i> 0 Likes</span>
                            </div>
                            <div class="post-icon">
                                <i class="fa fa-picture-o"></i>
                            </div>
                        </div>
                        <div class="entry-header">
                            <h3><a href="#">Lorem ipsum dolor sit amet consectetur adipisicing elit</a></h3>
                            <span class="date">June 26, 2014</span>
                            <span class="cetagory">in <strong>Photography</strong></span>
                        </div>
                        <div class="entry-content">
                            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. </p>
                        </div>
                    </div>
                    <div class="col-sm-4 wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="800ms">
                        <div class="post-thumb">
                            <a href="#"><img class="img-responsive" src="/resources/images/blog/3.jpg" alt=""></a>
                            <div class="post-meta">
                                <span><i class="fa fa-comments-o"></i> 3 Comments</span>
                                <span><i class="fa fa-heart"></i> 0 Likes</span>
                            </div>
                            <div class="post-icon">
                                <i class="fa fa-video-camera"></i>
                            </div>
                        </div>
                        <div class="entry-header">
                            <h3><a href="#">Lorem ipsum dolor sit amet consectetur adipisicing elit</a></h3>
                            <span class="date">June 26, 2014</span>
                            <span class="cetagory">in <strong>Photography</strong></span>
                        </div>
                        <div class="entry-content">
                            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. </p>
                        </div>
                    </div>
                </div>
                <div class="load-more wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="500ms">
                    <a href="#" class="btn-loadmore"><i class="fa fa-repeat"></i> Load More</a>
                </div>
            </div>
        </div>
    </section>
</#macro>

<#macro contact>
    <section id="contact">
        <div id="google-map" class="wow fadeIn"
             data-zoom="${page.contact.geolocation.@zoom}"
             data-latitude="${page.contact.geolocation.@latitude}"
             data-longitude="${page.contact.geolocation.@longitude}"
             data-wow-duration="1000ms" data-wow-delay="400ms">
        </div>

        <div id="contact-us" class="parallax">
            <div class="container">
                <div class="row">
                    <div class="heading text-center col-sm-8 col-sm-offset-2 wow fadeInUp"
                         data-wow-duration="1000ms" data-wow-delay="300ms">
                        <h2>${page.contact.title}</h2>
                        <p>${page.contact.description}</p>
                    </div>
                </div>

                <div class="contact-form wow fadeIn" data-wow-duration="1000ms" data-wow-delay="600ms">
                    <div class="row">
                        <div class="col-sm-6">
                            <form id="main-contact-form" name="contact-form" method="post" action="#">
                                <div class="row  wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="300ms">
                                    <div class="col-sm-6">
                                        <div class="form-group">
                                            <input type="text" name="name" class="form-control"
                                                   placeholder='${page.text.name!"Name"}' required="required">
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group">
                                            <input type="email" name="email" class="form-control"
                                                   placeholder='${page.text.email!"Email Address"}' required="required">
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <input type="text" name="subject" class="form-control"
                                           placeholder='${page.text.subject!"Subject"}' required="required">
                                </div>

                                <div class="form-group">
                                    <textarea name="message" id="message" class="form-control" rows="4"
                                              placeholder='${page.text.enterMessage!"Enter your message"}'
                                              required="required"></textarea>
                                </div>
                                <div class="form-group">
                                    <button type="submit" class="btn-submit">${page.text.sendNow!"Send Now"}</button>
                                </div>
                            </form>
                        </div>

                        <div class="col-sm-6">
                            <div class="contact-info wow fadeInUp" data-wow-duration="1000ms"
                                 data-wow-delay="300ms">
                                <ul class="address">
                                    <li>
                                        <i class="fa fa-map-marker"></i>
                                        <span> ${page.text.address!"Address"}:</span> ${page.contact.address}
                                    </li>
                                    <li>
                                        <i class="fa fa-phone"></i>
                                        <span> ${page.text.phone!"Phone"}:</span> ${page.contact.phone}
                                    </li>
                                    <li>
                                        <i class="fa fa-envelope"></i>
                                        <span> ${page.text.email!"Email"}:</span>
                                        <a href="mailto:${page.contact.email}"> ${page.contact.email}</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</#macro>

<#macro footer>
    <footer id="footer">
        <div class="footer-top wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="300ms">
            <div class="container text-center">
                <div class="footer-logo">
                    <a href="#"><img class="img-responsive" src="${page.resources.logo}" alt=""></a>
                </div>

                <div class="social-icons">
                    <ul>
                        <#list page.social?children as social>
                        <#if social?node_type != "text">
                        <li><a class="${social?node_name}" href="${social}" target="_blank">
                            <i class="fa fa-${social?node_name}"></i>
                        </a></li>
                        </#if>
                        </#list>
                    </ul>
                </div>
            </div>
        </div>

        <div class="footer-bottom">
            <div class="container">
                <div class="row">
                    <div class="col-sm-6">
                        <p>&copy; ${.now?string["yyyy"]} ${page.meta.author}</p>
                    </div>

                    <div class="col-sm-6"></div>
                </div>
            </div>
        </div>
    </footer>
</#macro>

<#function random lo hi>
    <#local n = (.now?string["m"])[0]?number />
    <#if n &lt; lo>
        <#local n = lo />
    </#if>
    <#if n &gt; hi>
        <#local n = hi />
    </#if>

    <#return n />
</#function>
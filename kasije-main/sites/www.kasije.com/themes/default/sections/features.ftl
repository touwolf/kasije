<#ftl encoding="UTF-8">

<#macro content data>
    <section id="features" >
        <div class="container">
            <div class="section-header">
                <h2 class="section-title text-center wow fadeInDown">Que brindamos</h2>
                <#--p class="text-center wow fadeInDown">
                    Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut <br> et dolore magna aliqua. Ut enim ad minim veniam
                </p-->
            </div>

            <#list data.feature?chunk(2) as row>
            <div class="row">
                <div class="features">
                    <#list row as feature>
                    <div class="col-md-6 col-sm-6 wow fadeInUp" data-wow-duration="300ms" data-wow-delay="0ms">
                        <div class="media service-box">
                            <div class="pull-left">
                                <i class="fa ${feature.@faIcon}"></i>
                            </div>

                            <div class="media-body">
                                <h4 class="media-heading">${feature.@title}</h4>
                                <p style="text-align: justify">${feature.description}</p>
                            </div>
                        </div>
                    </div>
                    </#list>
                </div>
            </div>
            </#list>
        </div>
    </section>
</#macro>

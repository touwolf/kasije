<#ftl encoding="UTF-8">

<#macro content data>
    <section id="${data.@id}">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title text-center wow fadeInDown">${data.@title}</h2>
                <p class="text-center wow fadeInDown">${data.intro}</p>
            </div>
        </div>
    </section>
    <section>
        <div class="section-header"></div>
        <#list data.step as stepData>
        <div class="container">
            <div class="section-header">
                <div class="row">
                    <div class="col-sm-12">
                        <@step stepData />
                    </div>
                </div>
            </div>
        </div>
        </#list>
    </section>
</#macro>

<#macro step step>
    <div class="blog-post blog-large wow fadeInLeft animated" data-wow-duration="300ms" data-wow-delay="0ms"
         style="visibility: visible; animation-duration: 300ms; animation-delay: 0ms; animation-name: fadeInLeft;">
        <article>
            <header class="entry-header">
                <h2 class="entry-title">
                    <span class="post-format post-format-video"><i class="fa ${step.@faIcon}"></i></span>
                    ${step.@title}
                </h2>
            </header>

            <div class="entry-content">
                <#list step.* as element>
                <#if element?node_name == "content">
                <p>${element}</p>
                <#elseif element?node_name == "code">
                <pre class="prettyprint"><code class=" language-${element.@lang}">${element}</code></pre>
                </#if>
                <#--div class="entry-thumbnail">
                    <img class="img-responsive" src="images/blog/01.jpg" alt="">
                </div-->
                </#list>
            </div>

            <footer class="entry-meta">
            </footer>
        </article>
    </div>
</#macro>

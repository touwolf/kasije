<#ftl encoding="UTF-8">

<#macro content data>
    <section>
        <div class="container">
            <div class="section-header">
                <h2 class="section-title text-center wow fadeInDown">${page.@title}</h2>
                <p class="text-center wow fadeInDown">${data.header}</p>
            </div>
        </div>
    </section>

    <#if data.@banner?boolean>
    <section id="cta2">
        <div class="container">
            <div class="text-center">
                <h2 class="wow fadeInUp" data-wow-duration="300ms" data-wow-delay="0ms">
                    <span>${page.@title}</span> ${data.tip.@title}
                </h2>
                <#list data.tip.line as line>
                <p class="wow fadeInUp" data-wow-duration="${line.@duration}ms" data-wow-delay="${line.@delay}ms">
                    ${line}
                </p>
                </#list>
                <p class="wow fadeInUp animated" data-wow-duration="${data.tip.link.@duration}ms"
                   data-wow-delay="${data.tip.link.@delay}ms"
                   style="visibility: visible; animation-duration: ${data.tip.link.@duration}ms; animation-delay: ${data.tip.link.@delay}ms; animation-name: fadeInUp;">
                    <a class="btn btn-primary btn-lg" href="${data.tip.link}">
                        ${data.tip.link.@text}
                    </a>
                </p>
            </div>
        </div>
    </section>
    </#if>
</#macro>

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
                <p></p>
            </div>
        </div>
    </section>
</#macro>

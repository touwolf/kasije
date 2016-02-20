package com.kasije.core.impl.page;

import com.kasije.core.WebPage;
import com.kasije.core.WebPageRouter;
import com.kasije.core.WebSite;
import org.bridje.ioc.Component;
import org.bridje.ioc.Priority;

@Component
@Priority(value = Integer.MAX_VALUE)
public class WebPageRouterImpl implements WebPageRouter
{
    @Override
    public WebPage findWebPage(WebSite site, String pageName)
    {
        return site.findPage(pageName);
    }
}

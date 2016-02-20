package com.kasije.core.impl.site;

import com.kasije.core.WebSite;
import com.kasije.core.WebSiteRouter;
import org.bridje.ioc.Component;
import org.bridje.ioc.Priority;

import java.io.IOException;

@Component
@Priority(value = Integer.MAX_VALUE)
public class WebSiteRouterImpl implements WebSiteRouter
{
    @Override
    public WebSite findWebSite(String serverName) throws IOException
    {
        return new WebSiteImpl("./sites/" + serverName);
    }
}

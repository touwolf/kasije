package com.kasije.core.impl.site;

import com.kasije.core.WebSite;
import com.kasije.core.WebSiteRoute;
import org.bridje.ioc.Component;
import org.bridje.ioc.Priority;

import java.io.IOException;

@Component
@Priority(value = Integer.MAX_VALUE)
public class WebSiteRouteImpl implements WebSiteRoute
{
    @Override
    public WebSite findWebSite(String serverName) throws IOException
    {
        return new WebSiteImpl("./sites/" + serverName);
    }
}

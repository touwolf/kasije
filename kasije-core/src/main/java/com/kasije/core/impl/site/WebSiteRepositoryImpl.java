package com.kasije.core.impl.site;

import com.kasije.core.*;
import java.io.IOException;
import org.bridje.ioc.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Inject;

@Component
public class WebSiteRepositoryImpl implements WebSiteRepository
{
    private static final Logger LOG = Logger.getLogger(WebSiteRepositoryImpl.class.getName());

    @Inject
    private WebSiteRouter siteRouter;

    @Inject
    private WebSiteVirtual siteVirtual;

    private final Map<String, WebSite> mapWebSite = new HashMap<>();

    @Override
    public WebSite find(RequestContext reqCtx, String siteName) throws IOException
    {
        /* it's cache */
        WebSite webSite = mapWebSite.get(siteName);
        if(null != webSite)
        {
            return webSite;
        }

        siteName = siteVirtual.findRealSiteName(siteName);
        webSite = siteRouter.findWebSite(siteName);
        if(null != webSite)
        {
            mapWebSite.put(siteName, webSite);
            return webSite;
        }
        return null;
    }
}

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
    
    private final Map<String, WebSite> mapWebSite = new HashMap<>();

    @Override
    public WebSite find(String siteName) throws IOException
    {
        /* it's cache */
        WebSite webSite = mapWebSite.get(siteName);
        if(null != webSite)
        {
            return webSite;
        }

        webSite = siteRouter.findWebSite(siteName);
        if(null != webSite)
        {
            mapWebSite.put(siteName, webSite);
            return webSite;
        }
        return null;
    }

    @Override
    public boolean exists(String siteName)
    {
        try
        {
            return find(siteName) != null;
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }
}

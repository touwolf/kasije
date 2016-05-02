package com.kasije.core.impl.site;

import com.kasije.core.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;

@Component
public class WebSiteRepositoryImpl implements WebSiteRepository
{
    @Inject
    private WebSiteRouter siteRouter;

    @Inject
    private WebSiteVirtual siteVirtual;

    private final Map<String, WebSite> mapWebSite = new HashMap<>();

    @Override
    public WebSite find(RequestContext reqCtx, String sName, boolean acceptAdmin)
    {
        String siteName = sName;
        /* it's cache */
        if (mapWebSite.containsKey(siteName))
        {
            WebSite cache = mapWebSite.get(siteName);
            if (null != cache)
            {
                return cache;
            }
        }

        HttpServletRequest req = reqCtx.get(HttpServletRequest.class);
        siteName = siteVirtual.findRealSiteName(siteName);

        if (acceptAdmin)
        {
            WebSite adminWebSite = siteRouter.findAdminWebSite(req.getPathInfo());
            if (adminWebSite != null)
            {
                mapWebSite.put(adminWebSite.getName(), adminWebSite);
                return adminWebSite;
            }
        }

        WebSite webSite = siteRouter.findWebSite(siteName);
        if(null != webSite)
        {
            mapWebSite.put(webSite.getName(), webSite);
        }

        return webSite;
    }
}

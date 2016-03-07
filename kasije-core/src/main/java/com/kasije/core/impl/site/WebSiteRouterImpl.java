package com.kasije.core.impl.site;

import com.kasije.core.KasijeConfigRepo;
import com.kasije.core.WebSite;
import com.kasije.core.WebSiteRouter;
import com.kasije.core.config.global.Router;
import com.kasije.core.config.global.RouterConfig;
import org.apache.commons.lang.StringUtils;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.Priority;

import java.io.IOException;

@Component
@Priority(value = Integer.MAX_VALUE)
public class WebSiteRouterImpl implements WebSiteRouter
{
    @Inject
    private KasijeConfigRepo configRepo;

    @Override
    public WebSite findWebSite(String serverName) throws IOException
    {
        /* the routerConfig.xml is into ./sites/etc/ */
        RouterConfig config = configRepo.findConfig("./sites/", RouterConfig.class);

        Router router = config.getRouters().stream()
                .filter(r -> serverName.equals(r.getUri()))
                .findAny()
                .orElse(null);

         /* by default in the site is into de ./sites/ */
        String relativePath = "./sites/";

        /* i try to resolved the location where it is hosting by configuration */
        if(null != router && StringUtils.isNotBlank(router.getPath()))
        {
            relativePath = router.getPath();
        }

        return new WebSiteImpl(relativePath + serverName);
    }
}

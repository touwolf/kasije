package com.kasije.core.impl.site;

import com.kasije.core.KasijeConfigRepo;
import com.kasije.core.WebSite;
import com.kasije.core.WebSiteRouter;
import com.kasije.core.config.Router;
import com.kasije.core.config.RouterConfig;
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
        RouterConfig config = configRepo.findConfig("./sites/", RouterConfig.class);

        Router router = config.getRouters().stream()
                .filter(r -> serverName.equals(r.getUri()))
                .findAny()
                .orElse(null);

        /* i try to resolved the location where it is hosting by configuration */

        if(null != router && StringUtils.isNotBlank(router.getPath()))
        {
            return new WebSiteImpl(router.getPath() + serverName);
        }

        /* by default in the current folder, into de sites */
        return new WebSiteImpl("./sites/" + serverName);
    }
}

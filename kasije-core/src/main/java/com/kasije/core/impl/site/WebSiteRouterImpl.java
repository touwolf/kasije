package com.kasije.core.impl.site;

import com.kasije.core.KasijeConfigRepo;
import com.kasije.core.WebSite;
import com.kasije.core.WebSiteRouter;
import com.kasije.core.config.RouterSiteConfig;
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
        RouterSiteConfig config = configRepo.findConfig(".", RouterSiteConfig.class);
        return new WebSiteImpl("./sites/" + serverName);
    }
}

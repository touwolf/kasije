package com.kasije.core.impl.site;

import com.kasije.core.WebSite;
import com.kasije.core.WebSiteRouter;
import com.kasije.core.config.global.Router;
import com.kasije.core.config.global.RouterConfig;
import org.apache.commons.lang.StringUtils;
import org.bridje.cfg.ConfigRepositoryContext;
import org.bridje.cfg.ConfigService;
import org.bridje.ioc.Component;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.Priority;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Priority(value = Integer.MAX_VALUE)
public class WebSiteRouterImpl implements WebSiteRouter
{
    private static final Logger LOG = Logger.getLogger(WebSiteRouterImpl.class.getName());

    private RouterConfig config;

    @Override
    public WebSite findWebSite(String serverName) throws IOException
    {
        Router router = getConfig().getRouters().stream()
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

    public RouterConfig getConfig()
    {
        try
        {
            if (null == config)
            {
                ConfigService configService = Ioc.context().find(ConfigService.class);
                ConfigRepositoryContext configContext = configService.createRepoContext("global");

                config = configContext.findConfig(RouterConfig.class);
            }
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return config;
    }

    public void setConfig(RouterConfig config) {
        this.config = config;
    }
}

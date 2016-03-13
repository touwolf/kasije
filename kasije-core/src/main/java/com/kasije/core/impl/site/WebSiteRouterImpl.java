package com.kasije.core.impl.site;

import com.kasije.core.WebSite;
import com.kasije.core.WebSiteRouter;
import com.kasije.core.config.SiteConfig;
import com.kasije.core.config.server.Router;
import com.kasije.core.config.RouterConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bridje.cfg.ConfigRepositoryContext;
import org.bridje.cfg.ConfigService;
import org.bridje.ioc.Component;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.Priority;

@Component
@Priority(value = Integer.MAX_VALUE)
public class WebSiteRouterImpl implements WebSiteRouter
{
    private static final Logger LOG = Logger.getLogger(WebSiteRouterImpl.class.getName());

    private RouterConfig config;

    private Map<String, SiteConfig> siteConfigs = new HashMap<>();

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

        SiteConfig siteConfig = getSiteConfig(relativePath + serverName);
        return new WebSiteImpl(relativePath + serverName, siteConfig);
    }

    public RouterConfig getConfig()
    {
        try
        {
            if (null == config)
            {
                ConfigService configService = Ioc.context().find(ConfigService.class);
                ConfigRepositoryContext configContext = configService.createRepoContext("server");

                config = configContext.findConfig(RouterConfig.class);
            }
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return config;
    }

    private SiteConfig getSiteConfig(String absolutePath)
    {
        SiteConfig siteConfig = siteConfigs.get(absolutePath);
        try
        {
            if(null == siteConfig)
            {
                ConfigService configService = Ioc.context().find(ConfigService.class);
                ConfigRepositoryContext configContext = configService.createRepoContext(absolutePath + "/etc/");
                siteConfig = configContext.findConfig(SiteConfig.class);

                siteConfigs.put(absolutePath, siteConfig);
            }
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return siteConfig == null ? new SiteConfig() : siteConfig;
    }

    public void setConfig(RouterConfig config) {
        this.config = config;
    }
}

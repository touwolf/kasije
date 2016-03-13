package com.kasije.core.impl.site;

import com.kasije.core.WebSite;
import com.kasije.core.WebSiteRouter;
import com.kasije.core.WebSiteVirtual;
import com.kasije.core.config.RouterConfig;
import com.kasije.core.config.SiteConfig;
import com.kasije.core.config.server.Router;
import com.kasije.core.config.server.Virtual;
import org.apache.commons.lang.StringUtils;
import org.bridje.cfg.ConfigRepositoryContext;
import org.bridje.cfg.ConfigService;
import org.bridje.ioc.Component;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.Priority;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Priority(value = Integer.MAX_VALUE)
public class WebSiteVirtualImpl implements WebSiteVirtual
{
    private static final Logger LOG = Logger.getLogger(WebSiteVirtualImpl.class.getName());

    private RouterConfig config;

    @Override
    public String findRealSiteName(String serverName) throws IOException
    {
        Virtual virtual = getConfig().getVirtuals().stream()
                .filter(r -> serverName.equals(r.getUri()))
                .findAny()
                .orElse(null);

        return null == virtual ? serverName : virtual.getReal();
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
}

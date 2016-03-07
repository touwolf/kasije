package com.kasije.core.impl.page;

import com.kasije.core.*;
import com.kasije.core.config.global.RouterConfig;
import com.kasije.core.config.sites.Alias;
import com.kasije.core.config.sites.AliasConfig;
import org.bridje.cfg.ConfigRepositoryContext;
import org.bridje.cfg.ConfigService;
import org.bridje.ioc.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Priority(Integer.MIN_VALUE + 190)
public class WebAliasHandler implements RequestHandler
{
    private static final Logger LOG = Logger.getLogger(WebAliasHandler.class.getName());

    @InjectNext
    private RequestHandler handler;

    private Map<String, AliasConfig> configs = new HashMap<>();

    @Override
    public boolean handle(RequestContext reqCtx) throws IOException
    {
        WebSite webSite = reqCtx.get(WebSite.class);
        if(null == webSite)
        {
            return handler.handle(reqCtx);
        }

        AliasConfig aliasConfig = getConfig(webSite.getFile().getAbsolutePath() + "/etc/");
        if(null == aliasConfig)
        {
            return handler.handle(reqCtx);
        }

        WebPageRef ref = reqCtx.get(WebPageRef.class);
        if(ref == null)
        {
            String pathInfo = reqCtx.get(HttpServletRequest.class).getPathInfo();

            Alias alias = aliasConfig.getAlias().stream().filter(a -> pathInfo.equals(a.getPath()))
                    .findFirst().orElse(null);

            if(null != alias)
            {
                ref = new WebPageRef();
                ref.setPage(alias.getRealPath());
                reqCtx.put(WebPageRef.class, ref);
            }
        }
        return handler.handle(reqCtx);
    }

    private AliasConfig getConfig(String key)
    {
        AliasConfig aliasConfig = configs.get(key);
        try
        {
            if (null == aliasConfig)
            {
                ConfigService configService = Ioc.context().find(ConfigService.class);
                ConfigRepositoryContext configContext = configService.createRepoContext(key);

                aliasConfig = configContext.findConfig(AliasConfig.class);
                configs.put(key, aliasConfig);
            }
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return aliasConfig;
    }
}

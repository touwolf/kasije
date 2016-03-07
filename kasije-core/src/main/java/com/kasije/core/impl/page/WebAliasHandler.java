package com.kasije.core.impl.page;

import com.kasije.core.*;
import com.kasije.core.config.sites.Alias;
import com.kasije.core.config.sites.AliasConfig;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Priority(Integer.MIN_VALUE + 190)
public class WebAliasHandler implements RequestHandler
{
    @InjectNext
    private RequestHandler handler;

    @Inject
    private KasijeConfigRepo configRepo;

    @Override
    public boolean handle(RequestContext reqCtx) throws IOException
    {
        WebSite webSite = reqCtx.get(WebSite.class);
        if(null == webSite)
        {
            return handler.handle(reqCtx);
        }

        AliasConfig aliasConfig = configRepo.findConfig(webSite.getFile().getAbsolutePath(), AliasConfig.class);
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
}

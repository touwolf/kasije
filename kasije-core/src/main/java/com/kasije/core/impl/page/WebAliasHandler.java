package com.kasije.core.impl.page;

import com.kasije.core.RequestContext;
import com.kasije.core.RequestHandler;
import com.kasije.core.WebPageRef;
import com.kasije.core.WebSite;
import com.kasije.core.config.sites.Alias;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.bridje.ioc.Component;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

@Component
@Priority(Integer.MIN_VALUE + 190)
public class WebAliasHandler implements RequestHandler
{
    @InjectNext
    private RequestHandler handler;

    @Override
    public boolean handle(RequestContext reqCtx) throws IOException
    {
        WebSite webSite = reqCtx.get(WebSite.class);
        if(null == webSite)
        {
            return handler.handle(reqCtx);
        }

        String pathInfo = reqCtx.get(HttpServletRequest.class).getPathInfo();
        Alias alias = webSite.findAlias(pathInfo);
        if(null == alias)
        {
            return handler.handle(reqCtx);
        }

        WebPageRef ref = reqCtx.get(WebPageRef.class);
        if(ref == null)
        {
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

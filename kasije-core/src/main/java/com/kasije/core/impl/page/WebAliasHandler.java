package com.kasije.core.impl.page;

import com.kasije.core.WebPageRef;
import com.kasije.core.WebSite;
import com.kasije.core.config.sites.model.Alias;
import java.io.IOException;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerHandler;
import org.bridje.http.HttpServerRequest;
import org.bridje.ioc.Component;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

@Component
@Priority(Integer.MIN_VALUE + 190)
public class WebAliasHandler implements HttpServerHandler
{
    @InjectNext
    private HttpServerHandler handler;

    @Override
    public boolean handle(HttpServerContext reqCtx) throws IOException
    {
        WebSite webSite = reqCtx.get(WebSite.class);
        if(null == webSite)
        {
            return handler.handle(reqCtx);
        }

        String path = reqCtx.get(HttpServerRequest.class).getPath();
        Alias alias = webSite.findAlias(path);
        if(null == alias)
        {
            return handler.handle(reqCtx);
        }

        WebPageRef ref = reqCtx.get(WebPageRef.class);
        if(ref == null)
        {
            ref = new WebPageRef();
            ref.setPage(alias.getRealPath());
            reqCtx.set(WebPageRef.class, ref);
        }

        return handler.handle(reqCtx);
    }
}

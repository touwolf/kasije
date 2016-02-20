/*
 * Copyright 2016 Kasije Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kasije.core.impl.page;

import com.kasije.core.*;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

/**
 *
 */
@Component
@Priority(Integer.MIN_VALUE + 200)
class WebPageHandler implements RequestHandler
{
    @InjectNext
    private RequestHandler handler;

    @Inject
    private WebPageRouter pageRouter;

    @Override
    public boolean handle(RequestContext reqCtx) throws IOException
    {
        if(null == handler)
        {
            return false;
        }

        WebSite site = reqCtx.get(WebSite.class);
        if(null == site)
        {
            return false;
        }

         /* was it handler */
        WebPage page = reqCtx.get(WebPage.class);
        if(null == page)
        {
            return handler.handle(reqCtx);
        }

        HttpServletRequest req = reqCtx.get(HttpServletRequest.class);
        String pathInfo = req.getPathInfo();

        /* find the page using the router */
        page = pageRouter.findWebPage(site, findPageName(reqCtx));
        if(null != page)
        {
            reqCtx.put(WebPage.class, page);
            return handler.handle(reqCtx);
        }
        return false;
    }

    private String findPageName(RequestContext reqCtx)
    {
        WebPageRef ref = reqCtx.get(WebPageRef.class);
        if(ref != null)
        {
            return ref.getPage();
        }
        else
        {
            HttpServletRequest req = reqCtx.get(HttpServletRequest.class);
            return req.getPathInfo();
        }
    }
    
}

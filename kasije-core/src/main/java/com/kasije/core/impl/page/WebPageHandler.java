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

    @Override
    public boolean handle(RequestContext reqCtx) throws IOException
    {
        if(null == handler)
        {
            return false;
        }

        /* was it handled */
        WebPage webPage = reqCtx.get(WebPage.class);
        if (null == webPage)
        {
            WebSite site = reqCtx.get(WebSite.class);
            if(null != site)
            {
                String pageName = findPageName(reqCtx);
                /* find the page using the router */
                webPage = site.findPage(pageName);
                if (null != webPage)
                {
                    reqCtx.put(WebPage.class, webPage);
                }
            }
        }

        return handler.handle(reqCtx);
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

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

package com.kasije.core.impl.site;

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
@Priority(Integer.MIN_VALUE + 100)
class WebSiteHandler implements RequestHandler
{
    @InjectNext
    private RequestHandler handler;

    @Inject
    private WebSiteRepository siteRepo;

    @Override
    public boolean handle(RequestContext reqCtx) throws IOException
    {
        if(handler == null)
        {
            return false;
        }

        /* was it handled? */
        WebSite webSite = reqCtx.get(WebSite.class);
        if(null == webSite)
        {
            String siteName = findSiteName(reqCtx);
            webSite = siteRepo.find(siteName);
            if(webSite != null)
            {
                reqCtx.put(WebSite.class, webSite);
            }
        }
        return handler.handle(reqCtx);
    }

    private String findSiteName(RequestContext reqCtx)
    {
        WebSiteRef ref = reqCtx.get(WebSiteRef.class);
        if(ref != null)
        {
            return ref.getSite();
        }
        else
        {
            HttpServletRequest req = reqCtx.get(HttpServletRequest.class);
            return req.getServerName();
        }
    }
}

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

package com.kasije.core.impl;

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
@Priority(Integer.MIN_VALUE + 150)
class HomeHandler implements RequestHandler
{
    @InjectNext
    private RequestHandler handler;

    @Inject
    private WebSiteRouter router;

    @Override
    public boolean handle(RequestContext reqCtx) throws IOException
    {
        WebPageRef ref = reqCtx.get(WebPageRef.class);
        if(ref == null)
        {
            String pathInfo = reqCtx.get(HttpServletRequest.class).getPathInfo();
            WebSite site = reqCtx.get(WebSite.class);
            pathInfo = router.findPathInfo(site, pathInfo);

            if (pathInfo.equalsIgnoreCase("/") || pathInfo.isEmpty())
            {
                ref = new WebPageRef();
                ref.setPage("/index");
                reqCtx.put(WebPageRef.class, ref);
            }
        }

        return handler.handle(reqCtx);
    }
}

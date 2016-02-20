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

import com.kasije.core.RequestContext;
import com.kasije.core.RequestHandler;
import com.kasije.core.WebPageRef;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bridje.ioc.Component;
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
    
    @Override
    public boolean handle(RequestContext reqCtx) throws IOException
    {
        WebPageRef ref = reqCtx.get(WebPageRef.class);
        if(ref == null)
        {
            String pathInfo = reqCtx.get(HttpServletRequest.class).getPathInfo();
            if(pathInfo.equalsIgnoreCase("/"))
            {
                ref = new WebPageRef();
                ref.setPage("/index");
                reqCtx.put(WebPageRef.class, ref);
            }
        }
        if(!handler.handle(reqCtx))
        {
            HttpServletResponse resp = reqCtx.get(HttpServletResponse.class);
            resp.setStatus(404);
            reqCtx.get(HttpServletResponse.class).getWriter().print("<h1>404 - Not Found</h1>");
        }
        return true;
    }
    
}

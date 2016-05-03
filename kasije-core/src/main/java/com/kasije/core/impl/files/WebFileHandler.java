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

package com.kasije.core.impl.files;

import com.kasije.core.WebFile;
import com.kasije.core.WebSite;
import java.io.IOException;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerHandler;
import org.bridje.http.HttpServerRequest;
import org.bridje.ioc.Component;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

/**
 *
 */
@Component
@Priority(Integer.MIN_VALUE + 250)
class WebFileHandler implements HttpServerHandler
{
    @InjectNext
    private HttpServerHandler handler;

    @Override
    public boolean handle(HttpServerContext reqCtx) throws IOException
    {
        if(null == handler)
        {
            return false;
        }

        /* was it handled */
        WebFile webFile = reqCtx.get(WebFile.class);
        if(null == webFile)
        {
            WebSite site = reqCtx.get(WebSite.class);
            if(null != site)
            {
                String fileName = findFileName(reqCtx);
                /* find the page using the router */
                webFile = site.findFile(fileName);
                if(null != webFile)
                {
                    reqCtx.set(WebFile.class, webFile);
                }
            }
        }

        return handler.handle(reqCtx);
    }

    private String findFileName(HttpServerContext reqCtx)
    {
        return reqCtx.get(HttpServerRequest.class).getPath();
    }
}

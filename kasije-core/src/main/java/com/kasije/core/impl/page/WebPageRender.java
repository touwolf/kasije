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

import com.kasije.core.RequestContext;
import com.kasije.core.RequestHandler;
import com.kasije.core.WebPage;
import com.kasije.core.WebSiteTheme;
import java.io.IOException;
import org.bridje.ioc.Component;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

/**
 *
 */
@Component
@Priority(Integer.MIN_VALUE + 1000)
class WebPageRender implements RequestHandler
{
    @InjectNext
    private RequestHandler handler;

    @Override
    public boolean handle(RequestContext reqCtx) throws IOException
    {
        if (reqCtx.get(WebPage.class) != null)
        {
            WebSiteTheme webSiteTheme = reqCtx.get(WebSiteTheme.class);
            if (webSiteTheme != null && webSiteTheme.getFile() != null)
            {
                return webSiteTheme.render(reqCtx);
            }
        }

        return handler != null && handler.handle(reqCtx);
    }
}


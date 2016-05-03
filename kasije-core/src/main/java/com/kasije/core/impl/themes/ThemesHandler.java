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

package com.kasije.core.impl.themes;

import com.kasije.core.ThemesManager;
import com.kasije.core.WebSite;
import com.kasije.core.WebSiteTheme;
import java.io.IOException;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerHandler;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

/**
 *
 */
@Component
@Priority(Integer.MIN_VALUE + 120)
class ThemesHandler implements HttpServerHandler
{
    @InjectNext
    private HttpServerHandler handler;

    @Inject
    private ThemesManager themesManag;

    @Override
    public boolean handle(HttpServerContext reqCtx) throws IOException
    {
        WebSiteTheme webSiteTheme = reqCtx.get(WebSiteTheme.class);
        if (webSiteTheme == null)
        {
            WebSite webSite = reqCtx.get(WebSite.class);
            if(webSite != null)
            {
                WebSiteTheme theme = themesManag.findTheme(webSite);
                if(theme != null)
                {
                    reqCtx.set(WebSiteTheme.class, theme);
                }
            }
        }

        return handler.handle(reqCtx);
    }
}


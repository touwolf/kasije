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

import com.kasije.core.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

/**
 *
 */
@Component
@Priority(Integer.MIN_VALUE + 175)
public class ResourcesHandler implements RequestHandler
{
    @Inject
    private ResourcesManager resMgr;

    @InjectNext
    private RequestHandler handler;

    @Inject
    private WebSiteRouter router;

    @Override
    public boolean handle(RequestContext reqCtx) throws IOException
    {
        HttpServletRequest req = reqCtx.get(HttpServletRequest.class);
        if(req == null)
        {
            return false;
        }

        WebSite site = reqCtx.get(WebSite.class);
        String pathInfo = site != null ? router.findPathInfo(site, req.getPathInfo()) : req.getPathInfo();

        if (pathInfo.startsWith("/resources"))
        {
            String[] resPathArr = pathInfo.split("/");
            String[] realPathArr = Arrays.copyOfRange(resPathArr, 2, resPathArr.length);
            String realPath = String.join("/", (CharSequence[]) realPathArr);

            String resourcesRelativePath = "resources/";//TODO: config

            File themeFile = reqCtx.get(WebSiteTheme.class).getFile();
            File resFile = new File(themeFile, resourcesRelativePath + realPath);
            if ((!resFile.exists() || !resFile.isFile()) && site != null)
            {
                resourcesRelativePath = "resources/";//TODO: config
                resFile = new File(site.getFile().getAbsolutePath(), resourcesRelativePath + realPath);
            }

            if (resFile.exists() && resFile.isFile())
            {
                HttpServletResponse resp = reqCtx.get(HttpServletResponse.class);
                resp.setContentType(resMgr.getMime(realPath));
                resMgr.processResource(resFile, resp.getOutputStream());

                return true;
            }

            return false;
        }

        return handler.handle(reqCtx);
    }
}

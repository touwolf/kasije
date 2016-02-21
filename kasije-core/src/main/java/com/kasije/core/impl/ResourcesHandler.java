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

    @Override
    public boolean handle(RequestContext reqCtx) throws IOException
    {
        HttpServletRequest req = reqCtx.get(HttpServletRequest.class);
        if(req == null)
        {
            return false;
        }

        if(req.getPathInfo().startsWith("/resources"))
        {
            String pathInfo = req.getPathInfo();
            String[] resPathArr = pathInfo.split("/");
            String[] realPathArr = Arrays.copyOfRange(resPathArr, 2, resPathArr.length);
            String realPath = String.join("/", realPathArr);
            HttpServletResponse resp = reqCtx.get(HttpServletResponse.class);

            WebSite site = reqCtx.get(WebSite.class);
            if(site != null)
            {
                File resFile = new File(reqCtx.get(WebSiteTheme.class).getFile().getAbsolutePath() + "/resources/" + realPath);
                if (resFile.exists() && resFile.isFile())
                {
                    resMgr.processResource(resFile, resp.getOutputStream());
                    resp.setContentType(resMgr.getMime(realPath));

                    return true;
                }
            }

            return false;
        }

        return handler.handle(reqCtx);
    }
}

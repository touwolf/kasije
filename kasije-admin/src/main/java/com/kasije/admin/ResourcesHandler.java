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

package com.kasije.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kasije.core.RequestContext;
import com.kasije.core.RequestHandler;
import com.kasije.core.WebSite;
import com.kasije.core.WebSiteRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

/**
 *
 */
@Component
@Priority(Integer.MIN_VALUE + 275)
public class ResourcesHandler implements RequestHandler
{
    private static final Gson GSON = new GsonBuilder().create();

    @Inject
    private WebSiteRepository siteRepo;

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

        if(req.getPathInfo().startsWith("/admin"))
        {
            String pathInfo = req.getPathInfo();
            String[] resPathArr = pathInfo.split("/");
            String[] realPathArr = Arrays.copyOfRange(resPathArr, 2, resPathArr.length);
            String realPath = String.join("/", (CharSequence[]) realPathArr);

            if (isAuthorized(reqCtx, realPath))
            {
                WebSite adminSite = siteRepo.find(reqCtx, req.getServerName(), false);
                if (adminSite != null && !adminSite.isAdmin())
                {
                    List<Resource> resources = null;

                    switch (realPath)
                    {
                        case "pages":
                            resources = handlePagesResponse(adminSite);
                            break;
                    }

                    if (resources != null)
                    {
                        HttpServletResponse resp = reqCtx.get(HttpServletResponse.class);
                        resp.setStatus(200);
                        resp.setContentType("text/json");

                        String jsonResources = GSON.toJson(resources);

                        PrintWriter writer = resp.getWriter();
                        writer.println(jsonResources);

                        return true;
                    }
                }
            }
        }

        return handler.handle(reqCtx);
    }

    private List<Resource> handlePagesResponse(WebSite webSite) throws IOException
    {
        File pagesFolder = new File(webSite.getFile().getAbsoluteFile(), "pages");
        if (!pagesFolder.exists() || !pagesFolder.isDirectory() || !pagesFolder.canRead())
        {
            return null;
        }

        File[] pages = pagesFolder.listFiles(file ->
        {
            return file.getName().endsWith(".xml");
        });

        List<Resource> resources = new ArrayList<>(pages.length);
        for (File page : pages)
        {
            List<String> lines = IOUtils.readLines(new FileInputStream(page));
            String content = String.join("\n", lines);

            Resource resource = new Resource(page.getName(), "xml", content);
            resources.add(resource);
        }

        return resources;
    }

    private boolean isAuthorized(RequestContext reqCtx, String path)
    {
        return true;//TODO
    }
}

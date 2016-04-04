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
import com.kasije.core.auth.AuthUser;
import java.io.*;
import java.util.*;
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
                    File pagesFolder = new File(adminSite.getFile().getAbsoluteFile(), "pages");
                    if (!pagesFolder.exists() || !pagesFolder.isDirectory() || !pagesFolder.canRead())
                    {
                        return handler.handle(reqCtx);
                    }

                    if ("pages".equalsIgnoreCase(realPath))
                    {
                        resources = handlePagesResponse(pagesFolder);
                    }
                    else if (realPath.startsWith("save-page"))
                    {
                        Map<String, String[]> params = req.getParameterMap();
                        resources = handleSavePageResponse(pagesFolder, realPath.substring("save-page/".length()), params);
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

    private List<Resource> handlePagesResponse(File pagesFolder) throws IOException
    {
        File[] pages = pagesFolder.listFiles(file ->
        {
            return file.getName().endsWith(".xml");//FIXME: only XML?
        });

        List<Resource> resources = new ArrayList<>(pages.length);
        for (File page : pages)
        {
            List<String> lines = IOUtils.readLines(new FileInputStream(page));
            String content = String.join("\n", lines);

            String name = page.getName();
            int lastDotIndex = name.lastIndexOf(".");
            String ext = name.substring(lastDotIndex + 1);

            Resource resource = new Resource(name, ext, content);
            resources.add(resource);
        }

        return resources;
    }

    private List<Resource> handleSavePageResponse(File pagesFolder, String pageName, Map<String, String[]> params) throws IOException
    {
        if (!params.containsKey("text"))
        {
            throw new IOException("Not valid request!");
        }

        File pageFile = new File(pagesFolder, pageName);
        if (!pageFile.exists() || !pageFile.canWrite())
        {
            throw new IOException("Can not write file: " + pageName);
        }

        FileOutputStream fileOutStream = new FileOutputStream(pageFile);

        String text = params.get("text")[0];
        IOUtils.write(text, fileOutStream);

        return Collections.emptyList();
    }

    private boolean isAuthorized(RequestContext reqCtx, String path)
    {
        WebSite site = reqCtx.get(WebSite.class);
        if (site == null || !site.isAdmin())
        {
            return false;
        }

        return reqCtx.get(AuthUser.class) != null;
    }
}

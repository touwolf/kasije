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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
                        try
                        {
                            String jsonResources = GSON.toJson(resources);

                            HttpServletResponse resp = reqCtx.get(HttpServletResponse.class);
                            resp.setStatus(200);
                            resp.setContentType("text/json");

                            PrintWriter writer = resp.getWriter();
                            writer.println(jsonResources);

                            return true;
                        }
                        catch(Exception ex)
                        {
                            return false;
                        }
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
            resource.setTags(findTags(ext, lines));

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

        AuthUser authUser = reqCtx.get(AuthUser.class);
        if (authUser == null)
        {
            return false;
        }

        return authUser.getRoles() != null && authUser.getRoles().contains("admin");
    }

    private List<String> findTags(String ext, List<String> lines)
    {
        if ("xml".equalsIgnoreCase(ext))
        {
            Pattern tagPattern = Pattern.compile("\\w+");

            List<String> tags = new LinkedList<>();
            lines.parallelStream().forEach(line ->
            {
                int startIndex = line.indexOf("<");
                if (startIndex < 0)
                {
                    return;
                }

                int spaceIndex = line.indexOf(" ", startIndex);
                if (spaceIndex < 0)
                {
                    spaceIndex = line.length();
                }

                int closeIndex = line.indexOf(">", startIndex);
                if (closeIndex < 0)
                {
                    closeIndex = line.length();
                }

                int endIndex = Math.min(spaceIndex, closeIndex);
                String tag = line.substring(startIndex + 1, endIndex);

                if (!tag.isEmpty() && !tags.contains(tag))
                {
                    Matcher matcher = tagPattern.matcher(tag);

                    if (matcher.matches())
                    {
                        tags.add(tag);
                    }
                }
            });

            return tags;
        }

        return null;
    }
}

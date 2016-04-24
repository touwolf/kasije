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
import com.kasije.core.*;
import com.kasije.core.auth.AuthUser;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
@Priority(Integer.MIN_VALUE + 275)
public class ResourcesHandler implements RequestHandler
{
    private static final Gson GSON = new GsonBuilder().create();

    @Inject
    private WebSiteRepository siteRepo;

    @Inject
    private ThemesManager themesManager;

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
                    else if ("themes".equalsIgnoreCase(realPath))
                    {
                        resources = handleThemesResponse(adminSite);
                    }
                    else if (realPath.startsWith("save-page"))
                    {
                        Map<String, String[]> params = req.getParameterMap();
                        resources = ResourcesHelper.handleSavePageResponse(pagesFolder, realPath.substring("save-page/".length()), params);
                    }
                    else if (realPath.startsWith("add-page"))
                    {
                        Map<String, String[]> params = req.getParameterMap();
                        try
                        {
                            Resource resource = ResourcesHelper.createResource(pagesFolder, params);
                            if (resource != null)
                            {
                                resources = Collections.singletonList(resource);
                            }
                        }
                        catch (Exception ex)
                        {
                            return false;
                        }
                    }
                    else if (realPath.startsWith("save-theme-resource"))
                    {
                        WebSiteTheme theme = themesManager.findTheme(adminSite);
                        if (theme != null)
                        {
                            Map<String, String[]> params = req.getParameterMap();
                            resources = ResourcesHelper.handleSaveThemeResponse(theme.getFile(), realPath.substring("save-theme-resource/".length()), params);
                        }
                    }
                    else if (realPath.startsWith("add-resource"))
                    {
                        WebSiteTheme theme = themesManager.findTheme(adminSite);
                        if (theme != null)
                        {
                            Map<String, String[]> params = req.getParameterMap();
                            try
                            {
                                Resource resource = ResourcesHelper.createResource(theme.getFile(), params);
                                if (resource != null)
                                {
                                    resources = Collections.singletonList(resource);
                                }
                            }
                            catch (Exception ex)
                            {
                                return false;
                            }
                        }
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
        List<File> pages = ResourcesHelper.findFiles(pagesFolder, Arrays.asList("xml", "json"));

        return pages
                .parallelStream()
                .map(file -> ResourcesHelper.buildResource(pagesFolder, file))
                .filter(resource -> resource != null)
                .collect(Collectors.toList());
    }

    private List<Resource> handleThemesResponse(WebSite site) throws IOException
    {
        WebSiteTheme theme = themesManager.findTheme(site);
        if (theme == null)
        {
            return Collections.EMPTY_LIST;
        }

        List<File> files = ResourcesHelper.findFiles(theme.getFile(), Arrays.asList("ftl", "css"));

        return files
                .parallelStream()
                .map(file -> ResourcesHelper.buildResource(theme.getFile(), file))
                .filter(resource -> resource != null)
                .collect(Collectors.toList());
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
}

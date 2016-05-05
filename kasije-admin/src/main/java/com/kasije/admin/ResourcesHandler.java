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
import com.kasije.core.ThemesManager;
import com.kasije.core.WebSite;
import com.kasije.core.WebSiteRepository;
import com.kasije.core.WebSiteTheme;
import com.kasije.core.auth.AuthUser;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerHandler;
import org.bridje.http.HttpServerRequest;
import org.bridje.http.HttpServerResponse;
import org.bridje.http.UploadedFile;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

/**
 *
 */
@Component
@Priority(275)
public class ResourcesHandler implements HttpServerHandler
{
    private static final Gson GSON = new GsonBuilder().create();

    @Inject
    private WebSiteRepository siteRepo;

    @Inject
    private ThemesManager themesManager;

    @InjectNext
    private HttpServerHandler handler;

    @Override
    public boolean handle(HttpServerContext reqCtx) throws IOException
    {
        HttpServerRequest req = reqCtx.get(HttpServerRequest.class);
        if(req == null)
        {
            return false;
        }

        if(!req.getPath().startsWith("/admin"))
        {
            return handler.handle(reqCtx);
        }

        String pathInfo = req.getPath();
        String[] resPathArr = pathInfo.split("/");
        String[] realPathArr = Arrays.copyOfRange(resPathArr, 2, resPathArr.length);
        String realPath = String.join("/", (CharSequence[]) realPathArr);

        if (!isAuthorized(reqCtx))
        {
            return false;
        }

        return doHandle(reqCtx, realPath);
    }

    private boolean doHandle(HttpServerContext reqCtx, String realPath) throws IOException
    {
        HttpServerRequest req = reqCtx.get(HttpServerRequest.class);
        WebSite adminSite = siteRepo.find(reqCtx, req.getHost(), false);
        if (adminSite == null || adminSite.isAdmin())
        {
            return false;
        }

        return handlePageResponse(reqCtx, realPath, adminSite) ||
                handleThemeResponse(reqCtx, realPath, adminSite);
    }

    private boolean handlePageResponse(HttpServerContext reqCtx, String realPath, WebSite adminSite) throws IOException
    {
        File parentFolder = adminSite.getFile().getAbsoluteFile();
        File pagesFolder = new File(parentFolder, "pages");
        if (!pagesFolder.exists() || !pagesFolder.isDirectory() || !pagesFolder.canRead())
        {
            return false;
        }
        if ("pages".equalsIgnoreCase(realPath))
        {
            return doHandleResponse(reqCtx, handlePagesResponse(pagesFolder));
        }

        if ("pages-resources".equalsIgnoreCase(realPath))
        {
            return doHandleResponse(reqCtx, handlePagesResourcesResponse(parentFolder));
        }

        if ("pages-images".equalsIgnoreCase(realPath))
        {
            return doHandleResponse(reqCtx, handlePagesImagesResponse(parentFolder));
        }

        HttpServerRequest req = reqCtx.get(HttpServerRequest.class);
        if (realPath.startsWith("save-page"))
        {
            File folder = pagesFolder;
            if (realPath.startsWith("save-page-resource"))
            {
                folder = parentFolder;
            }

            Map<String, String> params = req.getPostParameters();
            List<Resource> resources = ResourcesHelper.handleSavePageResponse(folder, realPath.substring("save-page/".length()), params);
            return doHandleResponse(reqCtx, resources);
        }

        if (realPath.startsWith("add-page"))
        {
            File folder = pagesFolder;
            if (realPath.startsWith("add-page-resource"))
            {
                folder = parentFolder;
            }

            Map<String, String> params = req.getPostParameters();
            return addResource(reqCtx, folder, params);
        }

        if (realPath.startsWith("upload-page-image"))
        {
            return uploadFile(reqCtx, parentFolder);
        }

        return false;
    }

    private boolean handleThemeResponse(HttpServerContext reqCtx, String realPath, WebSite adminSite) throws IOException
    {
        if ("themes".equalsIgnoreCase(realPath))
        {
            return doHandleResponse(reqCtx, handleThemesResponse(adminSite));
        }

        WebSiteTheme theme = themesManager.findTheme(adminSite);
        if ("theme-images".equalsIgnoreCase(realPath))
        {
            if (theme != null)
            {
                return doHandleResponse(reqCtx, handlePagesImagesResponse(theme.getFile()));
            }

            return false;
        }

        HttpServerRequest req = reqCtx.get(HttpServerRequest.class);
        if (realPath.startsWith("save-theme-resource"))
        {
            if (theme != null)
            {
                Map<String, String> params = req.getPostParameters();
                List<Resource> resources = ResourcesHelper.handleSaveThemeResponse(theme.getFile(), realPath.substring("save-theme-resource/".length()), params);
                return doHandleResponse(reqCtx, resources);
            }

            return false;
        }

        if (realPath.startsWith("add-resource"))
        {
            if (theme != null)
            {
                Map<String, String> params = req.getPostParameters();
                return addResource(reqCtx, theme.getFile(), params);
            }

            return false;
        }

        if (realPath.startsWith("upload-theme-image"))
        {
            return uploadFile(reqCtx, theme.getFile());
        }

        return handler.handle(reqCtx);
    }

    private boolean addResource(HttpServerContext reqCtx, File file, Map<String, String> params)
    {
        try
        {
            Resource resource = ResourcesHelper.createResource(file, params);
            if (resource != null)
            {
                return doHandleResponse(reqCtx, Collections.singletonList(resource));
            }
        }
        catch (Exception ex)
        {
        }

        return false;
    }

    private boolean uploadFile(HttpServerContext reqCtx, File file) throws IOException
    {
        HttpServerRequest req = reqCtx.get(HttpServerRequest.class);
        if (req == null)
        {
            return false;
        }

        UploadedFile[] files = req.getUploadedFiles();
        if (files == null || files.length == 0)
        {
            return false;
        }

        Resource resource = ResourcesHelper.uploadImage(file, files[0]);
        if (resource != null)
        {
            return doHandleResponse(reqCtx, Collections.singletonList(resource));
        }

        return false;
    }

    private boolean doHandleResponse(HttpServerContext reqCtx, List<Resource> resources)
    {
        try
        {
            String jsonResources = GSON.toJson(resources);

            HttpServerResponse resp = reqCtx.get(HttpServerResponse.class);
            resp.setStatusCode(200);
            resp.setContentType("text/json");

            try(OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream()))
            {
                writer.append(jsonResources);
                writer.flush();
            }

            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
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

    private List<Resource> handlePagesResourcesResponse(File parentFolder) throws IOException
    {
        List<File> pages = ResourcesHelper.findFiles(parentFolder, Arrays.asList("js", "css", "scss"));

        return pages
                .parallelStream()
                .map(file -> ResourcesHelper.buildResource(parentFolder, file))
                .filter(resource -> resource != null)
                .collect(Collectors.toList());
    }

    private List<Resource> handlePagesImagesResponse(File parentFolder) throws IOException
    {
        List<File> images = ResourcesHelper.findFiles(parentFolder, Arrays.asList("png", "jpg", "jpeg"));

        return images
                .parallelStream()
                .map(file -> ResourcesHelper.buildImage(parentFolder, file))
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

        List<String> resourcesExts = Arrays.asList("ftl", "css", "scss", "js");
        List<File> files = ResourcesHelper.findFiles(theme.getFile(), resourcesExts);

        return files
                .parallelStream()
                .map(file -> ResourcesHelper.buildResource(theme.getFile(), file))
                .filter(resource -> resource != null)
                .collect(Collectors.toList());
    }

    private boolean isAuthorized(HttpServerContext reqCtx)
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

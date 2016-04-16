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
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
                        resources = handleSavePageResponse(pagesFolder, realPath.substring("save-page/".length()), params);
                    }
                    else if (realPath.startsWith("save-theme-resource"))
                    {
                        WebSiteTheme theme = themesManager.findTheme(adminSite);
                        if (theme != null)
                        {
                            Map<String, String[]> params = req.getParameterMap();
                            resources = handleSaveThemeResponse(theme.getFile(), realPath.substring("save-theme-resource/".length()), params);
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
        List<File> pages = findFiles(pagesFolder, Arrays.asList("xml", "json"));

        return pages
                .parallelStream()
                .map(file -> buildResource(pagesFolder, file))
                .filter(resource -> resource != null)
                .collect(Collectors.toList());
    }

    private Resource buildResource(File baseFile, File file)
    {
        List<String> lines;
        try
        {
            lines = IOUtils.readLines(new FileInputStream(file));
        }
        catch (IOException e)
        {
            return null;
        }
        String content = String.join("\n", lines);

        String name = file.getName();
        int lastDotIndex = name.lastIndexOf(".");
        String ext = name.substring(lastDotIndex + 1);

        String path = file.getAbsolutePath();
        path = path.substring(baseFile.getAbsolutePath().length());
        path = path.substring(0, path.length() - name.length());

        Resource resource = new Resource(path, name, ext, content);
        resource.setTags(findTags(ext, lines));

        return resource;
    }

    private List<Resource> handleThemesResponse(WebSite site) throws IOException
    {
        WebSiteTheme theme = themesManager.findTheme(site);
        if (theme == null)
        {
            return Collections.EMPTY_LIST;
        }

        List<File> files = findFiles(theme.getFile(), Arrays.asList("ftl", "css"));

        return files
                .parallelStream()
                .map(file -> buildResource(theme.getFile(), file))
                .filter(resource -> resource != null)
                .collect(Collectors.toList());
    }

    private List<File> findFiles(File parent, List<String> extensions)
    {
        if (!parent.exists() || !parent.canRead())
        {
            return Collections.EMPTY_LIST;
        }

        List<File> files = new LinkedList<>();

        File[] children = parent.listFiles(file ->
        {
            return file.isDirectory();
        });
        for (File child : children)
        {
            files.addAll(findFiles(child, extensions));
        }

        children = parent.listFiles(file ->
        {
            String name = file.getName();
            int dotIndex = name.lastIndexOf(".");
            if (dotIndex < 2)
            {
                return false;
            }

            String extension = name.substring(dotIndex + 1);
            if (name.endsWith(".min." + extension))
            {
                return false;
            }

            return extensions.contains(extension);
        });
        files.addAll(Arrays.asList(children));

        return files;
    }

    private List<Resource> handleSavePageResponse(File pagesFolder, String pageName, Map<String, String[]> params) throws IOException
    {
        if (!params.containsKey("text"))
        {
            return null;
        }

        File pageFile = new File(pagesFolder, pageName);
        if (!pageFile.exists() || !pageFile.canWrite())
        {
            return null;
        }

        FileOutputStream fileOutStream = new FileOutputStream(pageFile);

        String text = params.get("text")[0];
        IOUtils.write(text, fileOutStream);

        return Collections.emptyList();
    }

    private List<Resource> handleSaveThemeResponse(File themeFolder, String resource, Map<String, String[]> params) throws IOException
    {
        if (!params.containsKey("text"))
        {
            return null;
        }

        File resourceFile = new File(themeFolder, resource);
        if (!resourceFile.exists() || !resourceFile.canRead())
        {
            return null;
        }

        FileOutputStream fileOutStream = new FileOutputStream(resourceFile);

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

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

import com.kasije.core.RequestContext;
import com.kasije.core.RequestHandler;
import com.kasije.core.WebSite;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.bridje.ioc.Component;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;

/**
 *
 */
@Component
@Priority(Integer.MIN_VALUE + 170)
public class SassHandler implements RequestHandler
{
    private static final Logger LOG = Logger.getLogger(SassHandler.class.getName());

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

        if(req.getPathInfo().startsWith("/resources") && req.getPathInfo().endsWith(".scss"))
        {
            String pathInfo = req.getPathInfo();
            String[] resPathArr = pathInfo.split("/");
            String[] realPathArr = Arrays.copyOfRange(resPathArr, 2, resPathArr.length);
            String realPath = String.join("/", realPathArr);
            HttpServletResponse resp = reqCtx.get(HttpServletResponse.class);

            WebSite site = reqCtx.get(WebSite.class);
            if(site != null)
            {
                File resFile = new File("./sites/themes/" + site.getTheme() + "/resources/" + realPath);
                if(resFile.exists() && resFile.isFile())
                {
                    int lastDotIndex = realPath.lastIndexOf(".scss");
                    String cssPath = realPath.substring(0, lastDotIndex) + ".css";
                    File cssFile = new File("./sites/themes/" + site.getTheme() + "/resources/" + cssPath);
                    if (!cssFile.exists())
                    {
                        if (!cssFile.createNewFile())
                        {
                            LOG.log(Level.SEVERE, "Could not create: {0} to compile sass", cssFile.getAbsolutePath());
                            return false;
                        }
                    }

                    //ONLY FROM CACHE IF WAS MODIFIED AFTER SASS FILE
                    if (resFile.lastModified() <= cssFile.lastModified())
                    {
                        List<String> lines = IOUtils.readLines(new FileInputStream(cssFile));
                        if (lines != null && !lines.isEmpty())
                        {
                            IOUtils.copy(new StringReader(String.join("", lines)), resp.getOutputStream());
                            resp.setContentType("text/css");

                            return true;
                        }
                    }

                    Compiler compiler = new Compiler();
                    Options options = new Options();
                    try
                    {
                        Output output = compiler.compileFile(resFile.toURI(), cssFile.toURI(), options);
                        String cssContent = output.getCss();
                        //TODO: compress cssContent

                        IOUtils.copy(new StringReader(cssContent), resp.getOutputStream());
                        resp.setContentType("text/css");

                        try
                        {
                            IOUtils.copy(new StringReader(cssContent), new FileOutputStream(cssFile));
                        }
                        catch(IOException ex)
                        {
                            LOG.log(Level.WARNING, ex.getMessage());
                        }

                        return true;
                    }
                    catch (Exception ex)
                    {
                        LOG.log(Level.SEVERE, "Error compiling " + resFile.getAbsolutePath() + ": " + ex.getMessage(), ex);
                        return false;
                    }
                }
            }

            return false;
        }

        return handler.handle(reqCtx);
    }
}

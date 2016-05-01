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

package com.kasije.main;

import com.kasije.core.RequestContext;
import com.kasije.core.RequestHandler;
import java.io.IOException;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.eclipse.jetty.server.Request;

/**
 *
 */
@Component
@MultipartConfig(
    location = "./",
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
final class KasijeServlet extends HttpServlet
{
    @Inject
    private RequestHandler handler;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement((String)null);
        req.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, multipartConfigElement);

        RequestContext reqCtx = new RequestContextImpl();
        reqCtx.put(HttpServletRequest.class, req);
        reqCtx.put(HttpServletResponse.class, resp);

        handler.handle(reqCtx);
    }
}

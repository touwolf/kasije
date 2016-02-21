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

import com.kasije.core.KasijeConfigRepo;
import com.kasije.core.config.ServerConfig;
import org.bridje.ioc.Ioc;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 *
 */
public class Main
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        ServletHandler servletHandler = new ServletHandler();
        KasijeServlet servlet = Ioc.context().find(KasijeServlet.class);
        ServletHolder servletHolder = new ServletHolder("kasije", servlet);
        servletHandler.addServletWithMapping(servletHolder, "/*");
        server.setHandler(servletHandler);

        KasijeConfigRepo kasijeConfig = Ioc.context().find(KasijeConfigRepo.class);
        ServerConfig config = kasijeConfig.findConfig(".", ServerConfig.class);

        server.start();
        server.join();
    }
}

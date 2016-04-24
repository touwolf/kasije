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

import com.kasije.core.config.server.model.Connector;
import com.kasije.core.config.server.ServerConfig;
import com.kasije.core.config.ConfigProvider;
import org.apache.commons.lang.StringUtils;
import org.bridje.ioc.Ioc;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;

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
        Server server = new Server();
        ServletHandler servletHandler = new ServletHandler();
        KasijeServlet servlet = Ioc.context().find(KasijeServlet.class);
        ServletHolder servletHolder = new ServletHolder("kasije", servlet);
        servletHandler.addServletWithMapping(servletHolder, "/*");
        server.setHandler(servletHandler);

        ConfigProvider configProv = Ioc.context().find(ConfigProvider.class);
        ServerConfig config = configProv.getServerConfig();
        if(null == config || null == config.getConnectors() || config.getConnectors().isEmpty())
        {
            server.addConnector(createConnector(server, null));
        }
        else
        {
            for (Connector connectorConfig : config.getConnectors())
            {
                server.addConnector(createConnector(server, connectorConfig));
            }
        }

        server.start();
        server.join();
    }

    private static org.eclipse.jetty.server.Connector createConnector(Server server, Connector connConfig)
    {
        Connector connectorConfig = connConfig;
        if (connectorConfig == null)
        {
            connectorConfig = new Connector();
        }

        if (!"http".equalsIgnoreCase(connectorConfig.getProtocol()) && !"https".equalsIgnoreCase(connectorConfig.getProtocol()))
        {
            connectorConfig.setProtocol("http");
        }

        if (connectorConfig.getPort() == null || connectorConfig.getPort() == 0)
        {
            connectorConfig.setPort(8080);
        }

        ServerConnector connector;
        if ("https".equalsIgnoreCase(connectorConfig.getProtocol()))
        {
            SslContextFactory sslFact = new SslContextFactory();
            if (StringUtils.isNotBlank(connectorConfig.getKeyStorePath()))
            {
                sslFact.setKeyStorePath(connectorConfig.getKeyStorePath());
            }
            if (StringUtils.isNotBlank(connectorConfig.getKeyStorePassword()))
            {
                sslFact.setKeyStorePassword(connectorConfig.getKeyStorePassword());
            }
            if (StringUtils.isNotBlank(connectorConfig.getKeyManagerPassword()))
            {
                sslFact.setKeyManagerPassword(connectorConfig.getKeyManagerPassword());
            }
            if (StringUtils.isNotBlank(connectorConfig.getTrustStorePath()))
            {
                sslFact.setTrustStorePath(connectorConfig.getTrustStorePath());
            }
            if (StringUtils.isNotBlank(connectorConfig.getTrustStorePassword()))
            {
                sslFact.setTrustStorePassword(connectorConfig.getTrustStorePassword());
            }
            connector = new ServerConnector(server, sslFact);
        }
        else
        {
            connector = new ServerConnector(server);
        }

        connector.setPort(connectorConfig.getPort());
        return connector;
    }
}

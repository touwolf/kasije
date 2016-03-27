package com.kasije.core;

import java.io.IOException;

/**
 * This interfaz find the webSite using the server name
 */
public interface WebSiteRouter
{
    WebSite findWebSite(String serverName) throws IOException;

    WebSite findAdminWebSite(String serverName, String pathInfo) throws IOException;

    String findPathInfo(WebSite site, String pathInfo);
}

package com.kasije.core;

/**
 * This interfaz find the webSite using the server name
 */
public interface WebSiteRouter
{
    WebSite findWebSite(String serverName);

    WebSite findAdminWebSite(String pathInfo);

    String findPathInfo(WebSite site, String pathInfo);
}

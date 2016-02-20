package com.kasije.core;

import java.io.IOException;

/**
 * This interfaz find the webSite using the server name
 */
public interface WebSiteRouter
{
    WebSite findWebSite(String serverName) throws IOException;
}

package com.kasije.core;

import java.io.IOException;

/**
 * This interfaz find the real server name using the server name
 */
public interface WebSiteVirtual
{
    String findRealSiteName(String serverName) throws IOException;
}

package com.kasije.core;

import java.io.IOException;
public interface WebSiteRepository
{
    boolean exists(String siteName);
    
    WebSite find(String siteName) throws IOException;
}

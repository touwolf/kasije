package com.kasije.core;

import org.bridje.ioc.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebSiteRepository
{
    Map<String, WebSite> mapWebSite = new HashMap<>();

    public WebSite findWebSite(String siteName)
    {
        return mapWebSite.get(siteName);
    }

    public void put(String siteName, WebSite webSite)
    {
        mapWebSite.put(siteName, webSite);
    }
}

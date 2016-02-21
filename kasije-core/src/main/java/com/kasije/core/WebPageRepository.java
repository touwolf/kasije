package com.kasije.core;

import org.bridje.ioc.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebPageRepository
{
    Map<String, WebPage> mapWebPage = new HashMap<>();

    public WebPage get(String pageName)
    {
        return mapWebPage.get(pageName);
    }

    public void put(String pageName, WebPage webPage)
    {
        mapWebPage.put(pageName, webPage);
    }
}

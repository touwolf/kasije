package com.kasije.core;

public interface WebSiteRepository
{
    WebSite find(RequestContext reqCtx, String siteName, boolean acceptAdmin);
}

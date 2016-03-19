package com.kasije.core;

import java.io.IOException;
public interface WebSiteRepository
{
    WebSite find(RequestContext reqCtx, String siteName) throws IOException;
}

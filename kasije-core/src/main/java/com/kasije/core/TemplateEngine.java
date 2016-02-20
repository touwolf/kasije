package com.kasije.core;

import java.io.IOException;

public interface TemplateEngine
{
    public boolean render(RequestContext reqCtx) throws IOException;
}

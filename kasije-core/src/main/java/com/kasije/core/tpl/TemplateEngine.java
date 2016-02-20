package com.kasije.core.tpl;

import java.io.File;

public interface TemplateEngine
{
    TemplateContext createContext(File folder);
}

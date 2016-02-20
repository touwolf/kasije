package com.kasije.freemarker.data;

import freemarker.template.TemplateHashModel;
import java.io.File;

public interface TemplateModelBuilder
{
    TemplateHashModel parse(File path, String name);
}

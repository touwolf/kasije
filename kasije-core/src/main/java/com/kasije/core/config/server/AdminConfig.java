package com.kasije.core.config.server;

public class AdminConfig
{
    private String path;

    private String name;

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getName()
    {
        if (name == null || name.isEmpty())
        {
            name = "kasije-admin";
        }

        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}

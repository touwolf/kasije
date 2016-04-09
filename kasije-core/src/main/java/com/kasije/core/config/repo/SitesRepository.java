package com.kasije.core.config.repo;

import org.bridje.cfg.ConfigRepository;
import org.bridje.ioc.Component;

import java.io.*;

@Component
public class SitesRepository implements ConfigRepository
{
    private String filePath;

    @Override
    public Boolean handleContext(String context)
    {
        File configFile = new File(context);
        if(configFile.exists())
        {
            filePath = context;
            return true;
        }

        return false;
    }

    @Override
    public Reader findConfig(String configName) throws IOException
    {
        File configFile = new File(filePath, configName);
        if(configFile.exists())
        {
            InputStream inputStream = new FileInputStream(configFile);
            return new InputStreamReader(inputStream);
        }

        return null;
    }

    @Override
    public Writer saveConfig(String s) throws IOException {
        return null;
    }

    @Override
    public boolean canSave() {
        return false;
    }
}

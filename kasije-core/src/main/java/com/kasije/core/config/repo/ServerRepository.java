package com.kasije.core.config.repo;

import org.bridje.cfg.ConfigRepository;
import org.bridje.ioc.Component;

import java.io.*;
import java.net.URL;

@Component
public class ServerRepository implements ConfigRepository
{
    @Override
    public Boolean handleContext(String context)
    {
        return "server".equals(context);
    }

    @Override
    public Reader findConfig(String configName) throws IOException
    {

        File configFile = new File("sites/etc/" + configName);
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

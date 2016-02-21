package com.kasije.core;

import org.bridje.ioc.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class KasijeConfigRepo
{
    private static final Logger LOG = Logger.getLogger(KasijeConfigRepo.class.getName());

    private Map<String, Object> mapConfig = new HashMap<>();

    public <T> T findConfig(String relativePath, Class<T> cls)
    {
        try
        {
            /* get annotation value to map the name config */
            String name = cls.getAnnotation(XmlRootElement.class).name();
            if("##deault".equals(name))
            {
                /* if the value annotation is not define take the class name */
                name = cls.getName().toLowerCase(); // FIX: only the first character
            }

            String path = relativePath + "/etc/" + name;
            /* if it is cache */
            if(mapConfig.containsKey(path))
            {
                return (T)mapConfig.get(path);
            }

            File configFile = new File(path + ".xml");

            /* verify if it is a file */
            if(!configFile.exists() || !configFile.isFile())
            {
                return null;
            }

            JAXBContext ctx = JAXBContext.newInstance(cls);
            Unmarshaller unm = ctx.createUnmarshaller();
            T obj = (T)unm.unmarshal(configFile);

            /* save on cache */
            mapConfig.put(path, obj);

            return obj;
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }

        return null;
    }
}

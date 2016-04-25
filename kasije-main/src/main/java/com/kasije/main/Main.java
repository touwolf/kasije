/*
 * Copyright 2016 Kasije Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kasije.main;

import java.io.File;
import java.lang.management.ManagementFactory;
import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.IocContext;

/**
 *
 */
public class Main
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception
    {
        if(args.length < 1)
        {
            printUsage();
            return ;
        }

        if ("start".equalsIgnoreCase(args[0]))
        {
            start();
            return;
        }
        else if ("stop".equalsIgnoreCase(args[0]))
        {
            if(args.length >= 2)
            {
                stop(args[1]);
                return;
            }
        }

        printUsage();
    }

    private static void printUsage()
    {
        System.out.println("Options: start|stop");
    }

    private static void start() throws Exception
    {
        System.out.println("Starting Kasije, path context: " + new File(".").getAbsoluteFile().getPath() + "...");

        IocContext context = Ioc.context();
        KasijeServer server = context.find(KasijeServer.class);

        initMBeans(context);
        server.start();
        System.out.println("Server started!");
        server.join();
    }

    private static void initMBeans(IocContext context) throws Exception
    {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName mbeanName = new ObjectName("com.kasije:type=AppCtrl");
        mbs.registerMBean(context.find(AppCtrl.class), mbeanName);
    }

    private static void stop(String port) throws Exception
    {
        JMXServiceURL url =new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:" + port + "/jmxrmi");
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
        ObjectName mbeanName = new ObjectName("com.kasije:type=AppCtrl");
        AppCtrlMBean mbeanProxy = JMX.newMBeanProxy(mbsc, mbeanName, AppCtrlMBean.class, true);
        mbeanProxy.shutdown();
    }
}

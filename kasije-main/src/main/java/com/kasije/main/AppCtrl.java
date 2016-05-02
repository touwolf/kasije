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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.NotificationBroadcasterSupport;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;

@Component
public class AppCtrl extends NotificationBroadcasterSupport implements AppCtrlMBean
{
    private static final Logger LOG = Logger.getLogger(AppCtrl.class.getName());

    @Inject
    private KasijeServer server;

    @Override
    public void shutdown()
    {
        try
        {
            LOG.info("Shutting down Kasije!");
            server.stop();
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            Runtime.getRuntime().exit(1);
        }
    }
}

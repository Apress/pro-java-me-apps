package com.apress.framework.core;

import com.apress.framework.common.EventControllerManager;
import javax.microedition.midlet.MIDlet;

public class Bootstrap
{

    public static void boot(MIDlet midlet, long eventManagerSleepTime)
    {
        Application.init(midlet);
        EventControllerManager main = Application.getMainEventControllerManager();
        main.assign(Application.getMainEventController());
        EventManagerThreads.start(main, eventManagerSleepTime);
    }
}

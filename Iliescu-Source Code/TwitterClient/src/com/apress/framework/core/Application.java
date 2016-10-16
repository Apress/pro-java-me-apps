package com.apress.framework.core;

import com.apress.framework.common.EventController;
import com.apress.framework.common.EventControllerManager;
import com.apress.framework.objecttypes.View;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class Application
{
    static EventController mainEventController = new EventController();
    static EventControllerManager manager = new EventControllerManager();
    static MIDlet midlet = null ;

    protected Application()
    {
        // Do nothing
    }

    public static void showView(View view)
    {
        getDisplay().setCurrent(view.getCanvas());
        view.refresh();
    }

    public static void init(MIDlet instance)
    {
        midlet = instance;
        manager.assign(mainEventController);
    }

    public static EventController getMainEventController()
    {
        return mainEventController;
    }

    public static EventControllerManager getMainEventControllerManager()
    {
        return manager;
    }

    public static MIDlet getMIDlet()
    {
        return midlet;
    }

    public static Display getDisplay()
    {
        return Display.getDisplay(midlet);
    }

    public static void exit()
    {
        getMIDlet().notifyDestroyed();
    }
}

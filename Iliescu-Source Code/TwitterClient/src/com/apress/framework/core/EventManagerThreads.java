package com.apress.framework.core;

import com.apress.framework.common.EventControllerManager;
import java.util.Hashtable;

public class EventManagerThreads
{
    public static class ManagerThread implements Runnable
    {
        EventControllerManager manager = null;
        long sleepTime = 0;
        boolean run = true;

        public ManagerThread(EventControllerManager manager, long sleepTime)
        {
            this.manager = manager;
            this.sleepTime = sleepTime;
        }

        public void run()
        {
            while ( run )
            {
                int evtCount = manager.currentEventCount();
                while ( evtCount>0 )
                {
                    manager.processNextEvent();
                    evtCount--;
                }

                try
                {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
        }

        public void stop()
        {
            run = false;
        }

    }

    protected static Hashtable threads = new Hashtable();

    protected EventManagerThreads()
    {
        // Do nothing
    }

    public static void start(EventControllerManager manager, long sleepTime)
    {
        stop(manager);
        ManagerThread thread = null;
        thread = new ManagerThread(manager, sleepTime);
        Thread t = new Thread(thread);
        t.start();
        threads.put(manager, thread);
    }

    public static void stop(EventControllerManager manager)
    {
        ManagerThread thread = (ManagerThread) threads.get(manager);
        if( thread != null )
        {
            thread.stop();
        }
    }
    
}

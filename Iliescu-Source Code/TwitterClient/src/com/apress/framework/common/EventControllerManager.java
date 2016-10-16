package com.apress.framework.common;

import com.apress.framework.objecttypes.Manager;
import java.util.Enumeration;
import java.util.Vector;

public class EventControllerManager implements Manager
{
    public Vector controllers = new Vector(10,10);

    public boolean assign(Object object)
    {
        if ( object instanceof EventController )
        {
            if ( ! controllers.contains(object) )
            {
                controllers.addElement(object);
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    public void unassign(Object object)
    {
        controllers.removeElement(object);
    }

    public boolean isManaging(Object object)
    {
        return ( controllers.indexOf(object) > -1 );
    }

    public void processNextEvent()
    {
        if ( controllers.size() > 0 )
        {
            EventController controller = (EventController) controllers.firstElement();
            controllers.removeElement(controller);
            controller.processNextEvent();
            controllers.addElement(controller);
        }
    }

    public boolean hasMoreEvents()
    {
        return currentEventCount() > 0;
    }

    public int currentEventCount()
    {
        Enumeration controllerEnum = controllers.elements();
        EventController temp = null;
        int count = 0;
        while ( controllerEnum.hasMoreElements() )
        {
            temp = (EventController) controllerEnum.nextElement() ;
            if ( temp.hasMoreEvents() )
            {
                count += temp.currentEventCount();
            }
        }
        return count;
    }

}

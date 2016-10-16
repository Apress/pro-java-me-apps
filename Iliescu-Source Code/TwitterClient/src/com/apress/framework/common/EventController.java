package com.apress.framework.common;

import com.apress.framework.objecttypes.Controller;
import com.apress.framework.objecttypes.Event;
import com.apress.framework.objecttypes.EventListener;
import java.util.Vector;

public class EventController implements Controller
{
    protected Vector listeners = new Vector(10,10);
    protected Vector events = new Vector(10,10);

    public void registerListener(EventListener listener)
    {
        if (! listeners.contains(listener) )
        {
            listeners.addElement(listener);
        }
    }

    public void unregisterListener(EventListener listener)
    {
        listeners.removeElement(listener);
    }

    public void queueEvent(Event event)
    {
        events.addElement(event);
    }

    public void processNextEvent()
    {
        if ( events.size() > 0 )
        {
            Event event = (Event) events.firstElement();
            events.removeElement(event);

            int listenerCount = listeners.size();
            int currentIndex;
            EventListener temp;
            for (currentIndex = 0; currentIndex < listenerCount; currentIndex++)
            {
                temp = ( (EventListener) listeners.elementAt(currentIndex));
                temp.handleEvent(event);
            }
        }
    }

    public int currentEventCount()
    {
        return events.size();
    }

    public boolean hasMoreEvents()
    {
        return events.size() > 0 ;
    }
}

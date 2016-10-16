package com.apress.framework.objecttypes;

public class Event
{
    protected int context;
    protected int type;
    protected Object payload;

    public Event(int context, int type, Object payload)
    {
        this.context = context;
        this.type = type;
        this.payload = payload;
    }

    public int getContext()
    {
        return this.context;
    }

    public int getType()
    {
        return this.type;
    }

    public Object getPayload()
    {
        return this.payload;
    }
    
}

package com.apress.framework.objecttypes;

public interface Consumer
{
    public boolean isSequential();

    public boolean next();

    public boolean store(Object object);

    public boolean jumpTo(int objectIndex);

    public int getNumberOfAvailableSlots();
}

package com.apress.framework.objecttypes;

public interface Provider
{
    public boolean isSequential();

    public Object retrieveCurrent();

    public boolean next();

    public boolean hasMore();

    public boolean jumpTo(int objectIndex);

    public int getNumberOfAvailableObjects();
}

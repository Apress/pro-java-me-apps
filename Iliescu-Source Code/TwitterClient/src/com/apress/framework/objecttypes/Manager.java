package com.apress.framework.objecttypes;

public interface Manager
{
    public boolean assign(Object object);

    public void unassign(Object object);

    public boolean isManaging(Object object);
}

package app.module.ui.models;

import com.apress.framework.objecttypes.Event;

public interface CallbackHandler
{
    public boolean doCallback(Event evt);
}

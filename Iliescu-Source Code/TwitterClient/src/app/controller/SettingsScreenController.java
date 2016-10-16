package app.controller;

import app.classes.Defaults;
import app.models.Tweet;
import app.module.L10n.classes.Locale;
import app.module.persistence.models.ByteRecordWriter;
import app.module.ui.classes.SimpleTextButton;
import app.module.ui.classes.StringItem;
import app.views.MainForm;
import app.views.SettingsForm;
import com.apress.framework.core.Application;
import com.apress.framework.objecttypes.Controller;
import com.apress.framework.objecttypes.EVT;
import com.apress.framework.objecttypes.Event;
import com.apress.framework.objecttypes.EventListener;
import com.sun.kvem.DeviceConfiguration;

public class SettingsScreenController implements Controller, EventListener {

    public static SettingsForm form = null;
    public static boolean firstTimeShow = true;

    public boolean handleEvent(Event event)
    {
        if ( EVT.PROGRAM_FLOW.SHOW_SETTINGS_SCREEN == event.getType() )
        {
            if ( firstTimeShow )
            {
                firstTimeShow = false;
                form = new SettingsForm(Defaults.THEME);
            }
            
            Application.showView(form);
            return true;
        }
        else
        if ( EVT.SETTINGS.CHANGE_LANGUAGE == event.getType() )
        {
            String locale = (String) event.getPayload();

            ByteRecordWriter writer = new ByteRecordWriter();
            writer.writeString(locale);
            Defaults.persistenceHelper.store(Defaults.DEFAULT_LOCALE_RECORD_NAME, writer.getCurrentResult());
            Locale.loadFromFileBasedOnPreferences(locale, Defaults.DEFAULT_LOCALE);
            return true;
        }
        else
        if ( EVT.SETTINGS.CLEAR_LOGIN_DATA == event.getType() )
        {
            Defaults.persistenceHelper.delete(Defaults.DEFAULT_LOGIN_DATA_RECORD_NAME);
            return true;
        }
        else
        if ( EVT.PROGRAM_FLOW.INITIATE_LOGOUT == event.getType() )
        {
            Event evt = new Event(EVT.CONTEXT.SETTINGS_FORM, EVT.SETTINGS.CLEAR_LOGIN_DATA, null);
            Application.getMainEventController().queueEvent(evt);

            evt = new Event(EVT.CONTEXT.SETTINGS_FORM, EVT.PROGRAM_FLOW.INITIATE_SHUTDOWN, null);
            Application.getMainEventController().queueEvent(evt);
            return true;
        }

        return false;
    }
}
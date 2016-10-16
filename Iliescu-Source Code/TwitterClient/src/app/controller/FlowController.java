package app.controller;

import app.classes.Defaults;
import app.models.Timeline;
import app.models.Tweet;
import app.models.TweetFilter;
import app.models.TwitterServer;
import app.module.L10n.classes.Locale;
import app.module.network.models.ServerImplementation;
import app.module.persistence.helpers.SimplifiedPersistenceHelper;
import app.module.persistence.models.ByteRecordReader;
import com.apress.framework.core.Application;
import com.apress.framework.objecttypes.Controller;
import com.apress.framework.objecttypes.EVT;
import com.apress.framework.objecttypes.Event;
import com.apress.framework.objecttypes.EventListener;

public class FlowController implements Controller, EventListener
{

    public boolean handleEvent(Event event)
    {
        if (EVT.PROGRAM_FLOW.APPLICATION_START == event.getType())
        {
            // Load the user's locale, if any
            byte[] data = Defaults.persistenceHelper.getRecord(Defaults.DEFAULT_LOCALE_RECORD_NAME);
            String locale = null;
            if (data != null)
            {
                ByteRecordReader reader = new ByteRecordReader(data);
                locale = reader.readString();
            }
            Locale.loadFromFileBasedOnPreferences(locale, Defaults.DEFAULT_LOCALE);


            // Show the welcome form
            Event showWelcome = new Event(EVT.CONTEXT.STARTUP, EVT.PROGRAM_FLOW.SHOW_WELCOME_SCREEN, null);
            Application.getMainEventController().queueEvent(showWelcome);

            return true;

        } else if (EVT.PROGRAM_FLOW.APPLICATION_EXIT == event.getType())
        {
            Defaults.persistenceHelper.close();
            Application.exit();
        } else if ( EVT.PROGRAM_FLOW.INITIATE_SHUTDOWN == event.getType() )
        {
                Event shutdown = new Event(EVT.CONTEXT.SETTINGS_FORM, EVT.PROGRAM_FLOW.APPLICATION_PREPARE_SHUTDOWN, null);
                Application.getMainEventController().queueEvent(shutdown);

                shutdown = new Event(EVT.CONTEXT.SETTINGS_FORM, EVT.PROGRAM_FLOW.APPLICATION_EXIT, null);
                Application.getMainEventController().queueEvent(shutdown);
                return true;
        }

        return false;
    }
}

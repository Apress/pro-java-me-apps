package app.controller;

import app.classes.Defaults;
import app.models.UserCredentials;
import app.module.persistence.classes.PersistenceProvider;
import app.module.persistence.helpers.HighLevelSerializer;
import app.module.persistence.helpers.SimplifiedPersistenceHelper;
import app.module.persistence.models.ByteRecordReader;
import app.module.persistence.models.ByteRecordWriter;
import app.module.persistence.models.RMSPersistenceProvider;
import app.views.WelcomeForm;
import com.apress.framework.core.Application;
import com.apress.framework.objecttypes.Controller;
import com.apress.framework.objecttypes.EVT;
import com.apress.framework.objecttypes.Event;
import com.apress.framework.objecttypes.EventListener;

public class WelcomeScreenController implements Controller, EventListener {

    public static boolean firstTimeShow = true;
    public static WelcomeForm form = null;

    public boolean handleEvent(Event event)
    {
        if ( EVT.PROGRAM_FLOW.SHOW_WELCOME_SCREEN == event.getType() )
        {
            if ( firstTimeShow )
            {
                firstTimeShow = false;
                form = new WelcomeForm(Defaults.THEME);
            }

            // See if there's a set of stored user credentials. It there is one,
            // use that to login directly instead of showing the welcome screen.
            byte [] data = Defaults.persistenceHelper.getRecord(Defaults.DEFAULT_LOGIN_DATA_RECORD_NAME);
            if ( data != null )
            {
                ByteRecordReader reader = new ByteRecordReader(data);
                UserCredentials credentials = HighLevelSerializer.deserializeUserCredentials(reader);

                Event evt = new Event ( EVT.CONTEXT.LOGIN_FORM, EVT.NETWORK.BEGIN_LOGIN, credentials);
                Application.getMainEventController().queueEvent(evt);
            }
            else
            {
                Application.showView(form);
                return true;
            }
        }
        else
        if ( EVT.NETWORK.LOGIN_FAILED == event.getType() )
        {
            // Error handling code goes here
            return true;
        }
        else
        if ( EVT.NETWORK.LOGIN_SUCCEEDED == event.getType() )
        {
            Event evt = new Event(EVT.CONTEXT.LOGIN_FORM, EVT.PROGRAM_FLOW.SHOW_MAIN_SCREEN, null);
            Application.getMainEventController().queueEvent(evt);
            
            // Store credentials that were used
            UserCredentials credentials = (UserCredentials) event.getPayload();
            ByteRecordWriter writer = new ByteRecordWriter();
            HighLevelSerializer.serializeUserCredentials(credentials, writer);
            Defaults.persistenceHelper.store(Defaults.DEFAULT_LOGIN_DATA_RECORD_NAME, writer.getCurrentResult());
            
            return true;
        }
        return false;
    }

}

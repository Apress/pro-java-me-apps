package app.controller;

import app.classes.Defaults;
import app.models.Tweet;
import app.module.ui.classes.SimpleTextButton;
import app.module.ui.classes.StringItem;
import app.views.MainForm;
import com.apress.framework.core.Application;
import com.apress.framework.objecttypes.Controller;
import com.apress.framework.objecttypes.EVT;
import com.apress.framework.objecttypes.Event;
import com.apress.framework.objecttypes.EventListener;

public class MainScreenController implements Controller, EventListener {

    public static MainForm form = null;
    public static boolean firstTimeShow = true;

    public boolean handleEvent(Event event)
    {
        if ( EVT.PROGRAM_FLOW.SHOW_MAIN_SCREEN == event.getType() )
        {
            if ( firstTimeShow )
            {   form = new MainForm(Defaults.THEME);
                firstTimeShow = false;
                Event evt = new Event(EVT.CONTEXT.MAIN_FORM, EVT.TWEETS.REQUEST_MAIN_TWEETS_BATCH, null);
                Application.getMainEventController().queueEvent(evt);
            }

            Application.showView(form);
            return true;
        }
        else
        if ( EVT.TWEETS.RECEIVED_TWEET == event.getType() )
        {
           Tweet tweet = (Tweet) event.getPayload();
           form.addTweet(tweet);
        }
        return false;
    }

}

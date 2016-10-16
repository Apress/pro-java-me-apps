package app.views;

import app.classes.Defaults;
import app.models.Tweet;
import app.models.UserCredentials;
import app.module.L10n.classes.Locale;
import app.module.persistence.models.ByteRecordWriter;
import app.module.ui.classes.GameCanvasView;
import app.module.ui.classes.HorizontalContainer;
import app.module.ui.classes.InputStringItem;
import app.module.ui.classes.Label;
import app.module.ui.classes.SimpleTextButton;
import app.module.ui.classes.VerticalContainer;
import app.module.ui.models.CallbackHandler;
import app.module.ui.models.Container;
import app.module.ui.models.UITheme;
import com.apress.framework.core.Application;
import com.apress.framework.objecttypes.EVT;
import com.apress.framework.objecttypes.Event;

public class SettingsForm extends GameCanvasView implements CallbackHandler
{

    SimpleTextButton switchToLanguage1 = null;
    SimpleTextButton switchToLanguage2 = null;
    SimpleTextButton goBack = null;
    SimpleTextButton exit = null;
    SimpleTextButton logout = null;

    public SettingsForm(UITheme theme)
    {
        super(false,theme);

        // Create the command buttons
        switchToLanguage1 = new SimpleTextButton(Locale.get("text.general.language.1"), this, theme);
        switchToLanguage2 = new SimpleTextButton(Locale.get("text.general.language.2"), this, theme);
        goBack = new SimpleTextButton(Locale.get("text.general.goback"), this, theme);
        exit = new SimpleTextButton(Locale.get("text.general.exit"), this, theme);
        logout = new SimpleTextButton(Locale.get("text.general.logout"), this, theme);

        addWidget(switchToLanguage1);
        addWidget(switchToLanguage2);
        addWidget(goBack);
        addWidget(exit);
        addWidget(logout);

        // Do initial layout and focus
        doLayout();
        onFocus();
    }

    public boolean doCallback(Event event)
    {
        if ( EVT.UI.BUTTON_PRESSED == event.getType() )
        {
            if ( switchToLanguage1 == event.getPayload() )
            {
                Event evt = new Event(EVT.CONTEXT.SETTINGS_FORM, EVT.SETTINGS.CHANGE_LANGUAGE, "en-US");
                Application.getMainEventController().queueEvent(evt);
                return true;
            }
            else
            if ( switchToLanguage2 == event.getPayload() )
            {
                Event evt = new Event(EVT.CONTEXT.SETTINGS_FORM, EVT.SETTINGS.CHANGE_LANGUAGE, "weird");
                Application.getMainEventController().queueEvent(evt);
                return true;
            }
            else
            if ( goBack == event.getPayload() )
            {
                Event evt = new Event(EVT.CONTEXT.SETTINGS_FORM, EVT.PROGRAM_FLOW.SHOW_MAIN_SCREEN, null);
                Application.getMainEventController().queueEvent(evt);
                return true;
            }
            else
            if ( logout == event.getPayload() )
            {
                Event evt = new Event(EVT.CONTEXT.SETTINGS_FORM, EVT.PROGRAM_FLOW.INITIATE_LOGOUT, null);
                Application.getMainEventController().queueEvent(evt);
                return true;
            }
            else
            if ( exit == event.getPayload() )
            {
                Event evt = new Event(EVT.CONTEXT.SETTINGS_FORM, EVT.PROGRAM_FLOW.INITIATE_SHUTDOWN, null);
                Application.getMainEventController().queueEvent(evt);
                return true;
            }
        }
        return false;
    }
    
}

package app.views;

import app.classes.Defaults;
import app.models.UserCredentials;
import app.module.L10n.classes.Locale;
import app.module.ui.classes.GameCanvasView;
import app.module.ui.classes.InputStringItem;
import app.module.ui.classes.Label;
import app.module.ui.classes.SimpleTextButton;
import app.module.ui.models.CallbackHandler;
import app.module.ui.models.Container;
import app.module.ui.models.UITheme;
import com.apress.framework.core.Application;
import com.apress.framework.objecttypes.EVT;
import com.apress.framework.objecttypes.Event;

public class WelcomeForm extends GameCanvasView implements CallbackHandler
{

    protected InputStringItem username = null;
    protected InputStringItem password = null;
    protected SimpleTextButton login = null;


    public WelcomeForm(UITheme theme)
    {
        super(false,theme);

        // Create all the needed widgets
        username = new InputStringItem(Locale.get("username.text"), "", Locale.get("text.general.ok"), Locale.get("text.general.cancel"),
            getContentWidth()/2, 100, null, theme);

        password = new InputStringItem(Locale.get("username.text"), "", Locale.get("text.general.ok"), Locale.get("text.general.cancel"),
            getContentWidth()/2, 100, null, theme);

        login = new SimpleTextButton(Locale.get("login.button.text"), this, theme);

        // Create the form structure
        addWidget ( new Label(Locale.get("username.text") + ":",theme));
        addWidget(username);
        addWidget ( new Label(Locale.get("password.text") + ":",theme));
        addWidget(password);
        addWidget(login);

        // Layout and focus the form
        doLayout();
        onFocus();
    }

    public boolean doCallback(Event evt) 
    {
        if ( EVT.UI.BUTTON_PRESSED == evt.getType() )
        {
            UserCredentials credentials = null;
            if ( Defaults.USE_SINGLE_USER_ACCESS )
            {
                credentials = new UserCredentials(null,null, "xxxx", "xxxx");
            }
            else
            {
                credentials = new UserCredentials(username.getText(),password.getText(),null,null);
            }

            Event event = new Event ( EVT.CONTEXT.LOGIN_FORM, EVT.NETWORK.BEGIN_LOGIN, credentials);
            Application.getMainEventController().queueEvent(event);
            return true;
        }
        return false;
    }
}

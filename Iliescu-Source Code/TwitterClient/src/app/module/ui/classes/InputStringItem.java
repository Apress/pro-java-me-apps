package app.module.ui.classes;

import app.module.ui.helpers.KeyHelper;
import app.module.ui.models.CallbackHandler;
import app.module.ui.models.UITheme;
import com.apress.framework.core.Application;
import com.apress.framework.objecttypes.EVT;
import com.apress.framework.objecttypes.Event;
import com.apress.framework.objecttypes.View;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.game.GameCanvas;

public class InputStringItem extends StringItem implements CommandListener
{
   protected CallbackHandler handler;
   protected Command CMD_OK, CMD_CANCEL;
   protected String label;
   protected TextBox textBox;
   protected View oldView;

    public InputStringItem (String label, String initialText, String OKText, String cancelText, int maxLineWidth, int maxTextHeight, CallbackHandler handler, UITheme theme)
    {
        super(initialText, maxLineWidth, maxTextHeight, null, theme);
        this.label = label;
        this.handler = handler;
        
        // Prepare the TextBox commands
        CMD_OK = new Command(OKText,Command.SCREEN,0 ) ;
        CMD_CANCEL = new Command (cancelText, Command.SCREEN, 1);
    }

    public boolean handleKeyReleased(int key)
    {
        if ( GameCanvas.FIRE == KeyHelper.getGameAction(key) )
        {
            showTextBox();
            return true;
        }
        return false;
    }

    public boolean handlePointerReleased(int x, int y)
    {
        super.handlePointerReleased(x,y);
        showTextBox();
        return true;
    }

    public void showTextBox()
    {
        Display d = Display.getDisplay(Application.getMIDlet());
        TextBox t = new TextBox(label,getText(),1024,0);
        t.addCommand(CMD_OK);
        t.addCommand(CMD_CANCEL);
        t.setCommandListener(this);
        oldView = getParentView();
        d.setCurrent(t);
    }

    public void commandAction(Command c, Displayable d)
    {
        if ( CMD_OK == c )
        {
            setText( ( (TextBox) d).getString() );
            if ( getParent() != null )
            {
                getParent().doLayout();
            }
            if ( oldView != null )
            {
                Application.showView(oldView);
            }
            fireEvent();
        }
        else
        if ( CMD_CANCEL == c)
        {
            if ( oldView != null )
            {
                Application.showView(oldView);
            }
        }
    }

    protected void fireEvent()
    {
        if ( handler != null )
        {
            Event event = new Event(EVT.CONTEXT.UI_MODULE, EVT.UI.TEXT_CHANGED, this);
            handler.doCallback(event);
        }
    }
}

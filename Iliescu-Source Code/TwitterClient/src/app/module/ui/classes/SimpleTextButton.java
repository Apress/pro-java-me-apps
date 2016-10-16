package app.module.ui.classes;

import app.module.ui.helpers.ClipHelper;
import app.module.ui.helpers.KeyHelper;
import app.module.ui.models.BaseWidget;
import app.module.ui.models.CallbackHandler;
import app.module.ui.models.UITheme;
import com.apress.framework.objecttypes.EVT;
import com.apress.framework.objecttypes.Event;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

public class SimpleTextButton extends BaseWidget
{
    protected String text;
    protected CallbackHandler handler;
    protected UITheme theme;
    protected int PADDING = 5;

    public SimpleTextButton(String text, CallbackHandler handler, UITheme theme)
    {
        this.text = text;
        this.handler = handler;
        this.theme = theme;
    }

    public void paint(Graphics g)
    {
        ClipHelper.setClipOn(this, g);
        int bgColor = theme.getNotSelectedBgColor();
        int fgColor = theme.getNotSelectedFgColor();
        if ( isFocused() )
        {
           bgColor = theme.getSelectedBgColor();
           fgColor = theme.getSelectedFgColor();
        }

        // Draw BG
        g.setColor(bgColor);
        g.fillRoundRect(getAbsoluteX(), getAbsoluteY(), getTotalWidth(), getTotalHeight(), 15, 15);

        // Draw text;
        g.setColor(fgColor);
        g.setFont(theme.getSelectedFont());
        int sizeInPixels = theme.getSelectedFont().stringWidth(text);
        int posX = (getTotalWidth() - sizeInPixels) / 2;
        g.drawString(text, getAbsoluteX() + posX, getAbsoluteY()+PADDING, Graphics.TOP | Graphics.LEFT );
        
        ClipHelper.resetClip(g);
    }

    public boolean isFocusable()
    {
        return true;
    }

    public int getPreferredContentHeight()
    {
        return theme.getSelectedFont().getHeight() + PADDING * 2;
    }

    public int getPreferredContentWidth()
    {
        return theme.getSelectedFont().stringWidth(text)+PADDING*2;
    }

    public boolean handleKeyReleased(int key)
    {
        if ( GameCanvas.FIRE == KeyHelper.getGameAction(key) )
        {
            fireEvent();
            return true;
        }
        return false;
    }

    public boolean handlePointerReleased(int x, int y)
    {
        super.handlePointerReleased(x,y);
        fireEvent();
        return true;
    }

    protected void fireEvent()
    {
        if ( handler != null )
        {
            Event event = new Event(EVT.CONTEXT.UI_MODULE, EVT.UI.BUTTON_PRESSED, this);
            handler.doCallback(event);
        }
    }


}

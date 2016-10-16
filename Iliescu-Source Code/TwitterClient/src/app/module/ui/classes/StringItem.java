package app.module.ui.classes;

import app.module.ui.helpers.ClipHelper;
import app.module.ui.helpers.KeyHelper;
import app.module.ui.helpers.UITextHelper;
import app.module.ui.models.BaseWidget;
import app.module.ui.models.CallbackHandler;
import app.module.ui.models.UITheme;
import com.apress.framework.objecttypes.EVT;
import com.apress.framework.objecttypes.Event;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

public class StringItem extends BaseWidget
{
    protected String [] linesSelected, linesNotSelected;
    protected String realString;
    protected int maxHeight, maxWidth;
    protected UITheme theme;
    protected int PADDING = 5;
    protected CallbackHandler handler;

    public StringItem(String string, int maxLineWidth, int maxTextHeight, CallbackHandler handler, UITheme theme)
    {
        this.maxHeight = maxTextHeight;
        this.maxWidth = maxLineWidth;
        this.theme = theme;
        this.handler = handler;
        setText(string);
    }

    public void setText(String text)
    {
        linesSelected = UITextHelper.wrapText(text, maxWidth, maxHeight, theme.getSelectedFont());
        linesNotSelected = UITextHelper.wrapText(text, maxWidth, theme.getNotSelectedFont().getHeight() * 2, theme.getNotSelectedFont());
        realString = text;
    }

    public String getText()
    {
        return realString;
    }

    public void paint(Graphics g)
    {
            ClipHelper.setClipOn(this, g);
            int bgColor = theme.getNotSelectedBgColor();
            int fgColor = theme.getNotSelectedFgColor();
            String [] text = linesNotSelected;
            Font f = theme.getNotSelectedFont();
            if ( isFocused() )
            {
               bgColor = theme.getSelectedBgColor();
               fgColor = theme.getSelectedFgColor();
               f = theme.getSelectedFont();
               text = linesSelected;
            }

            // Draw the background
            g.setColor(bgColor);
            g.fillRect(getAbsoluteX(), getAbsoluteY(), getTotalWidth(), getTotalHeight());

            // Draw the text
            g.setColor(fgColor);
            g.setFont(f);
            int i=0;
            for (i=0;i<text.length;i++)
            {
                    g.drawString(text[i], getAbsoluteX()+PADDING, getAbsoluteY()+PADDING+f.getHeight()*i, Graphics.TOP | Graphics.LEFT );
            }
            ClipHelper.resetClip(g);
    }

    public boolean onFocus()
    {
        super.onFocus();
        if ( getParent() != null )
        {
            getParent().doLayout();
        }
        return true;
    }

    public boolean onLostFocus()
    {
        super.onLostFocus();
        if ( getParent() != null )
        {
            getParent().doLayout();
        }
        return false;
    }
    
    public int getPreferredContentHeight()
    {
        Font font = theme.getNotSelectedFont() ;
        String [] text = linesNotSelected;
        if ( isFocused() )
        {
            font = theme.getSelectedFont();
            text = linesSelected;
        }
        return PADDING * 2 + text.length * font.getHeight();
    }

    public int getPreferredContentWidth()
    {
        return maxWidth + PADDING * 2;
    }

    public boolean isFocusable()
    {
        return true;
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

package app.module.ui.models;

import com.apress.framework.objecttypes.View;
import javax.microedition.lcdui.Graphics;

public interface Widget
{
    public int getTotalWidth();

    public int getTotalHeight();
    
    public int getAbsoluteX();

    public int getAbsoluteY();

    public void setX(int x);

    public void setY(int y);

    public int getX();

    public int getY();

    public void setContentWidth(int w);

    public int getContentWidth();

    public void setContentHeight(int h);

    public int getContentHeight();    

    public int getPreferredContentHeight();

    public int getPreferredContentWidth();

    public ClipRect getClipRect();

    public void requestRepaint();
    
    public View getParentView();

    public void paint(Graphics g);

    public Container getParent();

    public void setParent(Container c);

    public boolean handleKeyPressed(int key);

    public boolean handleKeyReleased(int key);

    public boolean handlePointerPressed(int x, int y);

    public boolean handlePointerDragged(int x, int y);

    public boolean handlePointerReleased(int x, int y);

    public boolean onFocus();

    public boolean onLostFocus();

    public boolean isFocusable();

    public boolean isFocused();

}

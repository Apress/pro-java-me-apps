package app.module.ui.models;

import com.apress.framework.objecttypes.View;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;


public abstract class BaseWidget implements Widget
{
    protected int contentWidth;
    protected int contentHeight;
    protected int x;
    protected int y;
    protected ClipRect clipRect = new ClipRect(0,0,0,0);
    protected Container parent;
    protected boolean isFocused = false;
    protected Object widgetData = null;
        
    public void setContentHeight(int contentHeight)
    {
        this.contentHeight = contentHeight;
    }

    public int getContentWidth()
    {
        return contentWidth;
    }

    public int getContentHeight()
    {
        return contentHeight;
    }

    public void setContentWidth(int contentWidth)
    {
        this.contentWidth = contentWidth;
    }   
    
    public ClipRect getClipRect()
    {
        clipRect.setMaxX(getAbsoluteX()+getTotalWidth());
        clipRect.setMaxY(getAbsoluteY()+getTotalHeight());
        clipRect.setMinX(getAbsoluteX());
        clipRect.setMinY(getAbsoluteY());
        return clipRect;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getTotalWidth()
    {
        if ( this.contentWidth > 0 )
        {
            return this.contentWidth;
        }
        else
        {
            return getPreferredContentWidth() ;
        }
    }

    public int getTotalHeight()
    {
       if ( this.contentHeight > 0 )
        {
            return this.contentHeight;
        }
        else
        {
            return getPreferredContentHeight() ;
        }
    }

    public void setParent(Container widget)
    {
        parent = widget;
    }

    public Container getParent()
    {
        return parent;
    }

    public int getAbsoluteX()
    {
        return parent.getAbsoluteX() - parent.getXScroll() + getX();
    }

    public int getAbsoluteY()
    {
        return parent.getAbsoluteY() - parent.getYScroll() + getY();
    }

    public void requestRepaint()
    {
        View v = getParentView();
        if ( v != null )
        {
            getParentView().paintWidget(this);
        }
    }

    public View getParentView()
    {
        if ( parent == null )
        {
            return null;
        }
        return parent.getParentView();
    }

    public boolean handleKeyPressed(int key)
    {
        return false;
    }

    public boolean handleKeyReleased(int key)
    {
        return false;
    }

    public boolean handlePointerPressed(int x, int y)
    {
        return false;
    }

    public boolean handlePointerDragged(int x, int y)
    {
        return false;
    }

    public boolean handlePointerReleased(int x, int y)
    {
        if ( isFocusable() == true && isFocused() == false )
        {
            if ( getParent() != null )
            {
                getParent().focusWidget(this);
            }
            return true;
        }
        return false;
    }

    public boolean onFocus()
    {
        isFocused = true;
        return true;
    }

    public boolean onLostFocus()
    {
        isFocused = false;
        return false;
    }

    public boolean isFocusable()
    {
        return false;
    }

    public boolean isFocused()
    {
        return isFocused;
    }

    public Object getData()
    {
        return widgetData;
    }

    public void setData(Object object)
    {
        widgetData = object;
    }
}

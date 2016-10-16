package app.module.ui.models;

import app.module.ui.helpers.WidgetHelper;
import app.module.ui.models.BaseContainerWidget;
import app.module.ui.models.UITheme;
import app.module.ui.models.Widget;
import javax.microedition.lcdui.Graphics;

public abstract class BaseContainerManager extends BaseContainerWidget
{
    protected Widget focusedWidget = null ;
    protected int oldX  = 0;
    protected int oldY = 0;
    protected boolean isDragging = false;
    protected UITheme theme = null;

    public BaseContainerManager(UITheme theme)
    {
        this.theme = theme;
    }

    public void paint (Graphics g)
    {
        g.setColor(theme.getAppBgColor());
        int absX = getAbsoluteX();
        int absY = getAbsoluteY();
        g.fillRect(getAbsoluteX(), getAbsoluteY(), getTotalWidth()-1, getTotalHeight()-1);

        Widget temp;
        for (int i=0; i < getChildCount(); i++)
        {
            temp = getWidget(i);
            if ( temp.getClipRect().isVisible() )
            {
                temp.paint(g);
            }
        }
    }
    
    public boolean handleKeyPressed(int key)
    {
        return false;
    }

    public void removeWidget(Widget w)
    {
        super.removeWidget(w);
        onFocus();
    }

    public boolean onFocus()
    {
        isFocused = false;
        if (focusedWidget == null)
        {
            for (int i = 0; i < getChildCount(); i++)
            {
                if ( getWidget(i).isFocusable() )
                {
                    focusedWidget = getWidget(i);
                    break;
                }
            }
        }

        if ( focusedWidget != null && indexOf(focusedWidget) == -1 )
        {
            focusedWidget = null;
            return onFocus();
        }

        if ( focusedWidget != null )
        {
            isFocused = focusedWidget.onFocus();
            scrollToWidget(focusedWidget);
            return isFocused;
        }
        else
        {
            return false;
        }
    }

    public boolean onLostFocus()
    {
        if ( focusedWidget != null )
        {
            isFocused = focusedWidget.onLostFocus();
            focusedWidget.requestRepaint();
            return isFocused;
        }
        else
        {
            isFocused = false;
            return false;
        }
    }

    public boolean isFocusable()
    {
        int i;
        for (i=0;i< getChildCount();i++)
        {
            if ( getWidget(i).isFocusable() )
            {
                return true;
            }
        }
        return false;
    }

    public boolean isFocused()
    {
        return isFocused;
    }

    public boolean handlePointerPressed(int x, int y)
    {
        oldX = x;
        oldY = y;
        Widget w = getWidgetAt(x, y);
        if ( w != null )
        {
            if ( w.handlePointerPressed(x, y) == false )
            {
                // Do nothing
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
    }

    public Widget getWidgetAt(int x, int y)
    {
        int i;
        Widget temp;
        for (i=0;i<getChildCount();i++)
        {
            temp = getWidget(i);
            if ( temp.getAbsoluteX() <= x && temp.getAbsoluteX() +  temp.getTotalWidth() >= x  &&
                   temp.getAbsoluteY() <= y && temp.getAbsoluteY() +  temp.getTotalHeight() >= y )
            {
                return temp;
            }
        }
        return null;
    }

    public boolean scrollToWidget(Widget w)
    {
        if ( w == null )
        {
            return false;
        }

        int maxScroll, neededScrollDelta;
        boolean scroll = false;

        // Max allowed horizontal scroll
        maxScroll = Math.max(getPreferredContentWidth() - getContentWidth(), 0);

        // Horizontal scroll delta needed to get the left side of the widget into view
        neededScrollDelta = WidgetHelper.getXDistanceBetween(w, this);

        if ( neededScrollDelta > 0 && getXScroll() + neededScrollDelta < getXScroll() + getTotalWidth() )
        {
            // Don't scroll if the left side of the widget is already visible
        }
        else
        {
            if ( getXScroll() + neededScrollDelta >= maxScroll   )
            {
                // Make sure we don't overscroll
                setXScroll(maxScroll);
            }
            else
            {
                setXScroll(getXScroll()+neededScrollDelta);
            }
            scroll=true;
        }

        // Vertical scroll
        maxScroll = Math.max(getPreferredContentHeight() - getContentHeight(), 0);
        neededScrollDelta = WidgetHelper.getYDistanceBetween(w, this);

        if ( neededScrollDelta > 0 && getYScroll() + neededScrollDelta < getYScroll() + getTotalHeight() )
        {
            // Don't scroll
        }
        else
        {
            if ( getYScroll() + neededScrollDelta >= maxScroll   )
            {
                // Make sure we don't overscroll
                setYScroll(maxScroll);
            }
            else
            {
                setYScroll(getYScroll()+neededScrollDelta);
            }
            scroll=true;
        }

        boolean parentScroll = false;
        if ( parent != null )
        {
             parentScroll = parent.scrollToWidget(w);
        }
        boolean doScroll = parentScroll || scroll ;
        if ( doScroll )
        {
            requestRepaint();
        }
        return doScroll;
   }

    public void focusWidget(Widget w)
    {
        if ( indexOf(w) >= 0 && w.isFocusable() )
        {
            Widget oldFocusedWidget = focusedWidget;
            if ( focusedWidget != null )
            {
                focusedWidget.onLostFocus();
            }
            focusedWidget = w;
            focusedWidget.onFocus();

            // Focus the parent after the children so that we don't get
            // flickering.
            if ( isFocused() == false )
            {
                getParent().focusWidget(this);
            }            

            if ( scrollToWidget(w) == false )
            {
                focusedWidget.requestRepaint();
                if ( oldFocusedWidget != null )
                {
                    oldFocusedWidget.requestRepaint();
                }
            }

        }
    }

    public boolean handlePointerReleased(int x, int y)
    {
        if ( isDragging )
        {
            isDragging = false;
            return true;
        }
        else
        {
            Widget w = getWidgetAt(x,y);
            if ( w!= null )
            {
                return w.handlePointerReleased(x,y);
            }
            else
            {
                return false;
            }
        }
    }

    public boolean handlePointerDragged(int x, int y)
    {
        Widget w = getWidgetAt(x, y);
        if ( w != null )
        {
            if ( w.handlePointerDragged(x, y) )
            {
                // Do nothing
                return true;
            }
        }
        isDragging = true;
        return false;
    }

    public int getOldX()
    {
        return oldX;
    }

    public int getOldY()
    {
        return oldY;
    }

    public void setOldX(int x)
    {
        oldX=x;
    }

    public void setOldY(int y)
    {
        oldY=y;
    }

    public Widget getFocusedWidget()
    {
        return focusedWidget;
    }
    
    public abstract void doLayout() ;

    public abstract int getPreferredContentHeight();

    public abstract int getPreferredContentWidth();

}

package app.module.ui.models;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public abstract class BaseContainerWidget extends BaseWidget implements Container
{
    protected Vector children = new Vector();
    protected int scrollX=0;
    protected int scrollY=0;

    public int getChildCount()
    {
        return children.size();
    }

    public boolean addWidget(Widget b)
    {
        children.addElement(b);
        b.setParent(this);
        return true;
    }

    public boolean insertWidget(Widget b, int index)
    {
        if ( index <= children.size() )
        {
            children.insertElementAt(b, index);
        }
        else
        {
            addWidget(b);
        }
        return true;
    }

    public void removeWidget(Widget b)
    {
        children.removeElement(b);
        b.setParent(null);
    }

    public Widget getWidget(int index)
    {
        return (Widget) children.elementAt(index);
    }

    public int getXScroll()
    {
        return scrollX;
    }

    public int getYScroll()
    {
        return scrollY;
    }

    public void setYScroll(int scroll)
    {
        scrollY = scroll;
    }

    public void setXScroll(int scroll)
    {
        scrollX = scroll;
    }

    public int indexOf(Widget w)
    {
        return children.indexOf(w);
    }

    public abstract void paint(Graphics g) ;

    public abstract void doLayout();
}

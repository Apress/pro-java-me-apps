package app.module.ui.classes;

import app.module.ui.models.BaseWidget;
import app.module.ui.models.ClipRect;
import app.module.ui.models.Container;
import app.module.ui.models.UITheme;
import app.module.ui.models.Widget;
import com.apress.framework.objecttypes.Controller;
import com.apress.framework.objecttypes.View;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;


public class GameCanvasView extends GameCanvas implements View, Container
{
    protected ClipRect clipRect;
    protected VerticalContainer container = null;
    protected Widget widgetToPaint = null;
    protected UITheme theme;

    public GameCanvasView(boolean b, UITheme theme)
    {
        super(b);
        setFullScreenMode(true);
        clipRect = new ClipRect(0,0,getWidth(),getHeight());
        container = new VerticalContainer(theme);
        container.setParent(this);
        container.setContentHeight(getHeight());
        container.setContentWidth(getWidth());
    }

    public boolean refresh()
    {
        repaint();
        serviceRepaints();
        return true;
    }

    public Controller getParentController()
    {
        return null;
    }

    public GameCanvas getCanvas()
    {
        return this;
    }

    public int getTotalWidth()
    {
        return getWidth();
    }

    public int getTotalHeight()
    {
        return getHeight();
    }

    public int getAbsoluteX()
    {
        return 0;
    }

    public int getAbsoluteY()
    {
        return 0;
    }

    public ClipRect getClipRect()
    {
        return clipRect;
    }

    public void requestRepaint()
    {
        refresh();
    }

    public View getParentView()
    {
        return this;
    }

    public Container getParent()
    {
        return null;
    }

    public int getXScroll()
    {
        return 0;
    }

    public int getYScroll()
    {
        return 0;
    }

    public boolean insertWidget(BaseWidget b, int index)
    {
        return container.insertWidget(b, index);
    }

    public void removeWidget(BaseWidget b)
    {
        container.removeWidget(b);
    }

    public int getChildCount()
    {
        return container.getChildCount();
    }

    public Widget getWidget(int index)
    {
        return container.getWidget(index);
    }

    public void setParent(Container c)
    {
        // Do nothing
    }

    public void setContentWidth(int w)
    {
        container.setContentWidth(w);
    }

    public int getContentWidth()
    {
        return container.getContentWidth();
    }

    public void setContentHeight(int h)
    {
        container.setContentHeight(h);
    }

    public int getContentHeight()
    {
        return container.getContentHeight();
    }

    public boolean handleKeyPressed(int key)
    {
        return container.handleKeyPressed(key);
    }

    public boolean handleKeyReleased(int key)
    {
        return container.handleKeyReleased(key);
    }

    public boolean onFocus()
    {
        return container.onFocus();
    }

    public boolean onLostFocus()
    {
        return container.onLostFocus();
    }

    public void setX(int x)
    {
        // Do nothing
    }

    public void setY(int y)
    {
       // Do nothing
    }

    public int getX()
    {
        return 0;
    }

    public int getY()
    {
        return 0;
    }

    public boolean isFocusable()
    {
        return container.isFocusable();
    }

    public boolean isFocused()
    {
        return container.isFocused();
    }

    public boolean addWidget(Widget b)
    {
        return container.addWidget(b);
    }

    public boolean insertWidget(Widget b, int index)
    {
        return container.insertWidget(b, index);
    }

    public int indexOf(Widget w)
    {
        return container.indexOf(w);
    }

    public void removeWidget(Widget b)
    {
        container.removeWidget(b);
    }

    protected void keyReleased(int keyCode)
    {
        handleKeyReleased(keyCode);
    }

    protected void keyPressed(int keyCode)
    {
        handleKeyPressed(keyCode);
    }

    public void paintWidget(Widget w)
    {
        widgetToPaint = w;
        requestRepaint();
    }

    public void paint (Graphics g)
    {

        if ( widgetToPaint != null )
        {
            widgetToPaint.paint(g);
            widgetToPaint = null;
            return;
        }
        else
        {
            container.paint(g);
        }
    }

    public int getPreferredContentHeight()
    {
        return container.getPreferredContentHeight();
    }

    public int getPreferredContentWidth()
    {
        return container.getPreferredContentWidth();
    }

    public void setYScroll(int v)
    {       
        // Do nothing
    }

    public boolean scrollToWidget(Widget w)
    {
        return false;
    }

    public void pointerPressed(int x, int y)
    {
        handlePointerPressed(x, y);
    }

    public void pointerDragged(int x, int y)
    {
        handlePointerDragged(x, y);
    }

    public void pointerReleased(int x, int y)
    {
        handlePointerReleased(x, y);
    }

    public boolean handlePointerPressed(int x, int y)
    {
        return container.handlePointerPressed(x, y);
    }

    public boolean handlePointerDragged(int x, int y)
    {
        return container.handlePointerDragged(x, y);
    }

    public boolean handlePointerReleased(int x, int y)
    {
        return container.handlePointerReleased(x, y);
    }

    public void focusWidget(Widget w)
    {
        container.focusWidget(w);
    }

    public void doLayout()
    {
        container.doLayout();
    }

    public void setXScroll(int scroll)
    {
        // Do nothing
    }

    public Container getContainer()
    {
        return container;
    }
}

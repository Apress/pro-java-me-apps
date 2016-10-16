package app.module.ui.classes;

import app.module.ui.models.BaseContainerManager;
import app.module.ui.helpers.KeyHelper;
import app.module.ui.helpers.WidgetHelper;
import app.module.ui.models.BaseWidget;
import app.module.ui.models.BaseContainerWidget;
import app.module.ui.models.UITheme;
import app.module.ui.models.Widget;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

public class HorizontalContainer extends BaseContainerManager
{
    protected int PADDING = 3 ;

    public HorizontalContainer(UITheme theme)
    {
        super(theme);
    }

    public void doLayout()
    {
        int currentX = 0;
        int i;
        Widget temp;

        for (i=0; i < getChildCount(); i++)
        {
            temp = getWidget(i);
            temp.setY(0);
            temp.setX(currentX);
            currentX += temp.getTotalWidth() + PADDING;
        }

        requestRepaint();
    }
    
    public boolean handleKeyReleased(int key)
    {
        if ( getFocusedWidget().handleKeyReleased(key) )
        {
            return true;
        }
        else
        if ( GameCanvas.LEFT == KeyHelper.getGameAction(key) )
        {
            // Try to focus the previous widget
            int max = indexOf(getFocusedWidget());
            for (int i=max-1;i>=0;i--)
            {
                if ( getWidget(i).isFocusable() )
                {
                    focusWidget(getWidget(i));
                    return true;
                }
            }
            return false;
        }
        else
        if ( GameCanvas.RIGHT == KeyHelper.getGameAction(key) )
        {
            // Try to focus the next widget
            int min = indexOf(getFocusedWidget());
            int max = getChildCount();
            for (int i=min+1;i<max;i++)
            {
                if ( getWidget(i).isFocusable() )
                {
                    focusWidget(getWidget(i));
                    return true;
                }
            }
            return false;
        }
        else
        {
            return false;
        }
    }

    public int getPreferredContentHeight()
    {
        int maxH = 0;
        int tempH;
        for (int i=0;i<getChildCount();i++)
        {
            tempH = getWidget(i).getTotalHeight();
            if ( tempH > maxH )
            {
                maxH = tempH;
            }
        }
        return maxH;
    }

    public int getPreferredContentWidth()
    {
        int maxW = 0;
        if ( getChildCount() > 0 )
        {
            Widget lastWidget = getWidget(getChildCount()-1);
            maxW = lastWidget.getX() + lastWidget.getTotalWidth();
        }
        return maxW;
    }

    public boolean handlePointerDragged(int x, int y)
    {
       if ( super.handlePointerDragged(x,y) )
        {
            return true;
        }
        
        int deltaX = getOldX() - x ;
        setOldY(y);
        setOldX(x);

        int scroll = getXScroll() + deltaX;
        int maxScroll = Math.max(getPreferredContentWidth() - getContentWidth(), 0);
        if ( scroll >= 0 && scroll <= maxScroll )
        {
            setXScroll(scroll);
            requestRepaint();
        }
        return true;
    }

}

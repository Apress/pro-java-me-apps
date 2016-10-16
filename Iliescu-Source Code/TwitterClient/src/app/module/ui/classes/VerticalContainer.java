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

public class VerticalContainer extends BaseContainerManager
{
    protected int PADDING = 3 ;

    public VerticalContainer(UITheme theme)
    {
        super(theme);
    }

    public void doLayout()
    {
        int currentY = 0;
        int i;
        Widget temp;

        for (i=0; i < getChildCount(); i++)
        {
            temp = getWidget(i);
            temp.setX(0);
            temp.setY(currentY);
            currentY += temp.getTotalHeight() + PADDING;
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
        if ( GameCanvas.UP == KeyHelper.getGameAction(key) )
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
        if ( GameCanvas.DOWN == KeyHelper.getGameAction(key) )
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
        if ( getChildCount() > 0 )
        {
            Widget lastWidget = getWidget(getChildCount()-1);
            maxH = lastWidget.getY() + lastWidget.getTotalHeight();
        }
        return maxH;
    }

    public int getPreferredContentWidth()
    {
        int maxW = 0;
        int tempW;
        for (int i=0;i<getChildCount();i++)
        {
            tempW = getWidget(i).getTotalWidth();
            if ( tempW > maxW )
            {
                maxW = tempW;
            }
        }
        return maxW;
    }
    
    public boolean handlePointerDragged(int x, int y)
    {
        if ( super.handlePointerDragged(x,y) )
        {
            return true;
        }
        
        int deltaY = getOldY() - y ;
        setOldY(y);
        setOldX(x);

        int scroll = getYScroll() + deltaY;
        int maxScroll = Math.max(getPreferredContentHeight() - getContentHeight(), 0);
        if ( scroll >= 0 && scroll <= maxScroll )
        {
            setYScroll(scroll);
            requestRepaint();
        }
        return true;
    }

}

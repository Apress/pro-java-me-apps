package app.module.ui.helpers;

import app.module.ui.models.ClipRect;
import app.module.ui.models.Widget;
import javax.microedition.lcdui.Graphics;

public class ClipHelper
{
    public static ClipRect getTargetClipRectFor(Widget w)
    {
        ClipRect result = w.getClipRect();
        Widget parent = w.getParent();
        while ( parent != null )
        {
            result = result.intersectWith(parent.getClipRect());
            parent = parent.getParent();
        }

        return result;
    }

    public static void setClipOn(Widget w, Graphics g)
    {
        ClipRect result = getTargetClipRectFor(w);
        g.setClip(result.getMinX(), result.getMinY(), result.getMaxX()-result.getMinX(), result.getMaxY()-result.getMinY());
    }

    public static void resetClip(Graphics g)
    {
        g.setClip(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

}

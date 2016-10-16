package app.module.ui.helpers;

import app.module.ui.models.Container;
import app.module.ui.models.Widget;

public class WidgetHelper
{
    public static int getYDistanceBetween(Widget w, Container c)
    {
        int result = 0;
        boolean found = false;
        while ( w != null )
        {
            // We have reached the desired container, we can stop.
            if ( w == c )
            {
                found = true;
                break;
            }

            // Add the local parent-to-child distance to the total distance
            result += w.getY();

            // Compensate for the parent scroll
            if ( w.getParent() != null )
            {
                result -= w.getParent().getYScroll();
            }

            // Go up one level
            w = w.getParent();

        }

        if ( found )
        {
            return result;
        }
        else
        {
            return Integer.MIN_VALUE;
        }
    }

    public static int getXDistanceBetween(Widget w, Container c)
    {
        int result = 0;
        boolean found = false;
        while ( w != null )
        {
            // We have reached the desired container, we can stop.
            if ( w == c )
            {
                found = true;
                break;
            }

            // Add the local parent-to-child distance to the total distance
            result += w.getX();

            // Compensate for the parent scroll
            if ( w.getParent() != null )
            {
                result -= w.getParent().getXScroll();
            }

            // Go up one level
            w = w.getParent();
        }

        if ( found )
        {
            return result;
        }
        else
        {
            return -1;
        }
    }
}

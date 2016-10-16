package app.classes;

import app.module.ui.models.UITheme;
import javax.microedition.lcdui.Font;


public class AppTheme implements UITheme
{

    public int getSelectedBgColor()
    {
        return 0x0000FF00;
    }

    public int getNotSelectedBgColor()
    {
        return 0x00EEEE00;
    }

    public int getSelectedFgColor()
    {
        return 0x00000000;
    }

    public int getNotSelectedFgColor()
    {
        return 0x00FF0000;
    }

    public Font getSelectedFont()
    {
        return Font.getDefaultFont();
    }

    public Font getNotSelectedFont()
    {
        return Font.getDefaultFont();
    }

    public int getAppBgColor()
    {
        return 0x00FFFFFF;
    }

}

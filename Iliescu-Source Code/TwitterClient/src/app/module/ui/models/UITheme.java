package app.module.ui.models;

import javax.microedition.lcdui.Font;

public interface UITheme
{
    public int getAppBgColor();

    public int getSelectedBgColor();

    public int getNotSelectedBgColor();

    public int getSelectedFgColor();

    public int getNotSelectedFgColor();

    public Font getSelectedFont();

    public Font getNotSelectedFont();

}

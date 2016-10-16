package app.module.ui.classes;

import app.module.ui.models.CallbackHandler;
import app.module.ui.models.UITheme;

public class Label extends StringItem
{
    public Label(String string,UITheme theme)
    {
        super(string, theme.getNotSelectedFont().stringWidth(string), theme.getNotSelectedFont().getHeight(), null, theme);
    }

    public boolean isFocusable()
    {
        return false;
    }
}

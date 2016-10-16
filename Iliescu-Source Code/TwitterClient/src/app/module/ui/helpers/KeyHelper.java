package app.module.ui.helpers;

import javax.microedition.lcdui.game.GameCanvas;

public class KeyHelper
{
    public static final GameCanvas gc = new GameCanvas(false) {};

    public static final int getGameAction(int keycode)
    {
        return gc.getGameAction(keycode);
    }

}

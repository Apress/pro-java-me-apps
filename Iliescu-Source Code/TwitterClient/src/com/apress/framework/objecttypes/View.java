package com.apress.framework.objecttypes;

import app.module.ui.models.Widget;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

public interface View
{
    public boolean refresh();

    public Controller getParentController();

    public GameCanvas getCanvas();

    public void paintWidget(Widget w);
}

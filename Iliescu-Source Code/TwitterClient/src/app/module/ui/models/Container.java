package app.module.ui.models;

public interface Container extends Widget
{
    public int getXScroll();

    public int getYScroll();

    public void setYScroll(int scroll);

    public void setXScroll(int scroll);

    public boolean addWidget(Widget b);

    public boolean insertWidget(Widget b, int index);

    public int indexOf(Widget w);

    public void removeWidget(Widget b);

    public int getChildCount();

    public boolean scrollToWidget(Widget w);

    public void focusWidget(Widget w);

    public Widget getWidget(int index);

    public void doLayout();

}

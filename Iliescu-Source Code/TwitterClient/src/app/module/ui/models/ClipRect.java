package app.module.ui.models;

public class ClipRect
{
    int minX;
    int minY;
    int maxX;
    int maxY;

    public ClipRect(int minX, int minY, int maxX, int maxY)
    {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public int getMaxX()
    {
        return maxX;
    }

    public void setMaxX(int maxX)
    {
        this.maxX = maxX;
    }

    public int getMaxY()
    {
        return maxY;
    }

    public void setMaxY(int maxY)
    {
        this.maxY = maxY;
    }

    public int getMinX()
    {
        return minX;
    }

    public void setMinX(int minX)
    {
        this.minX = minX;
    }

    public int getMinY()
    {
        return minY;
    }

    public void setMinY(int minY)
    {
        this.minY = minY;
    }

    public ClipRect intersectWith(ClipRect otherRect)
    {
       ClipRect newRect = new ClipRect ( Math.max (this.minX, otherRect.getMinX()),
            Math.max (this.minY, otherRect.getMinY()),
            Math.min (this.maxX, otherRect.getMaxX()),
            Math.min (this.maxY, otherRect.getMaxY()));
       
       return newRect;
    }

    public boolean isVisible()
    {
        return ( this.maxX >= this.minX && this.maxY > this.minY );
    }

}

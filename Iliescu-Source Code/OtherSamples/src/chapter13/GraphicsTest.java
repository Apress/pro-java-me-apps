package chapter13;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

public class GraphicsTest extends GameCanvas {

    Image clouds;
    Image wall;
    Image grass;
    Image gradient;
    Image mask;

    public GraphicsTest(boolean b)
    {
        super(b);

        // Load images
        try
        {
            clouds = Image.createImage(getClass().getResourceAsStream("/images/clouds.png"));
            wall = Image.createImage(getClass().getResourceAsStream("/images/wall.png"));
            grass = Image.createImage(getClass().getResourceAsStream("/images/grass.png"));
            gradient = Image.createImage(getClass().getResourceAsStream("/images/gradient.png"));
            mask = Image.createImage(getClass().getResourceAsStream("/images/mask.png"));
        }
        catch (Exception ex) {
        }

        this.setFullScreenMode(true);
    }

    public void drawMaskedImage(Image source, Image mask, Graphics g, int x, int y)
    {
        // Reserve an array for the pixel data of each image
        int [] sourceData = new int[source.getHeight()*source.getWidth()];
        int [] maskData = new int[mask.getHeight()*mask.getWidth()];

        // Retrieve the individual pixels of each image (source, mask)
        source.getRGB(sourceData, 0, source.getWidth(), 0, 0, source.getWidth(), source.getHeight());
        mask.getRGB(maskData, 0, mask.getWidth(), 0, 0, mask.getWidth(), mask.getHeight());

        // Merge the alpha channel of the mask with the color channels of the source
        for (int i=0;i<sourceData.length;i++) {
           sourceData[i] =  (maskData[i] & 0xFF000000) | ( sourceData[i] & 0x00FFFFFF ) ;
        }

        // Draw the result
        g.drawRGB(sourceData, 0, source.getWidth(), x, y, source.getWidth(), source.getHeight(), true);       
    }

    public void drawBlendedImage(Image bottom, Image top, Graphics g, int coeff, int x, int y)
    {
        // Reserve an array for the pixel data of each image
        int [] bottomData = new int[bottom.getHeight()*bottom.getWidth()];
        int [] topData = new int[top.getHeight()*top.getWidth()];

        // Retrieve the individual pixels of each image (source, mask)
        bottom.getRGB(bottomData, 0, bottom.getWidth(), 0, 0, bottom.getWidth(), bottom.getHeight());
        top.getRGB(topData, 0, top.getWidth(), 0, 0, top.getWidth(), top.getHeight());

        // Define the needed pixel values
        int alpha1, alpha2;
        int red1, red2;
        int green1, green2;
        int blue1, blue2;
        int resultA,resultR,resultG,resultB;

        // Go through all the pixels in the top and bottom images
        for (int i=0;i<bottomData.length;i++) {

           // Get individual channel values for each pixel (top,bottom)
           alpha1 = (bottomData[i] & 0xFF000000) >>> 24;
           alpha2 = (topData[i] & 0xFF000000) >>> 24;
           red1 = (bottomData[i] & 0x00FF0000) >> 16;
           red2 = (topData[i] & 0x00FF0000) >> 16;
           green1 = (bottomData[i] & 0x0000FF00) >> 8;
           green2 = (topData[i] & 0x0000FF00) >> 8;
           blue1 = (bottomData[i] & 0x000000FF);
           blue2 = (topData[i] & 0x000000FF);

           // Apply the image blending formula
           resultA = ( alpha1 * coeff + alpha2 * (255 - coeff) ) / 255;
           resultR = ( red1 * coeff + red2 * (255 - coeff) ) / 255;
           resultG = ( green1 * coeff + green2 * (255 - coeff) ) / 255;
           resultB = ( blue1 * coeff + blue2 * (255 - coeff) ) / 255;

           // Create the final pixel value
           bottomData[i] = resultA << 24 | resultR << 16 | resultG << 8 | resultB ;
        }

        // Draw the result
        g.drawRGB(bottomData, 0, bottom.getWidth(), x, y, bottom.getWidth(), bottom.getHeight(), true);
    }
    
    public void drawMultipliedImage(Image firstImage, Image secondImage, Graphics g, int x, int y)
    {
        // Reserve an array for the pixel data of each image
        int [] bottomData = new int[firstImage.getHeight()*firstImage.getWidth()];
        int [] topData = new int[secondImage.getHeight()*secondImage.getWidth()];

        // Retrieve the individual pixels of each image (source, mask)
        firstImage.getRGB(bottomData, 0, firstImage.getWidth(), 0, 0, firstImage.getWidth(), firstImage.getHeight());
        secondImage.getRGB(topData, 0, secondImage.getWidth(), 0, 0, secondImage.getWidth(), secondImage.getHeight());

        // Define the needed pixel values
        int alpha1, alpha2;
        int red1, red2;
        int green1, green2;
        int blue1, blue2;
        int resultA,resultR,resultG,resultB;

        for (int i=0;i<bottomData.length;i++) {

           // Get individual channel values for each of the top and bottom images
           alpha1 = (bottomData[i] & 0xFF000000) >>> 24;
           alpha2 = (topData[i] & 0xFF000000) >>> 24;
           red1 = (bottomData[i] & 0x00FF0000) >> 16;
           red2 = (topData[i] & 0x00FF0000) >> 16;
           green1 = (bottomData[i] & 0x0000FF00) >> 8;
           green2 = (topData[i] & 0x0000FF00) >> 8;
           blue1 = (bottomData[i] & 0x000000FF);
           blue2 = (topData[i] & 0x000000FF);

           resultA = alpha1 * alpha2 / 255 ;
           resultR = red1 * red2 / 255 ;
           resultG = green1 * green2 / 255 ;
           resultB = blue1 * blue2 / 255;

           // Create the final pixel value
           bottomData[i] = resultA << 24 | resultR << 16 | resultG << 8 | resultB ;
        }

        // Draw the result
        g.drawRGB(bottomData, 0, firstImage.getWidth(), x, y, firstImage.getWidth(), firstImage.getHeight(), true);
    }

    public void drawResizedImage(Image image, Graphics g, double factor, int x, int y)
    {
        // Variables to mark X,Y positions in the original and zoomed image
        int xpos,ypos;
        int origx, origy;

        // Position in the RGB array
        int origpos, zoompos;

        // Calculate zoomed image size
        int originalW = image.getWidth();
        int originalH = image.getWidth();
        int zoomW = (int) (originalW * factor);
        int zoomH = (int) (originalH * factor);

        // Reserve an array for the pixel data of the original and zoomed image
        int [] sourceData = new int[originalW * originalH];
        int [] zoomData = new int[zoomW * zoomH];

        // Retrieve the pixels of the original image
        image.getRGB(sourceData, 0, originalW, 0, 0, originalW, originalH);

        // Process each pixel of the zoomed image
        for (xpos=0;xpos<zoomW;xpos++)
        {
            for (ypos=0;ypos<zoomH;ypos++)
            {
                // Calculate its corresponding original image pixel
                origx = (int) (xpos / factor);
                origy = (int) (ypos / factor);

                // Map both pixels (original,zoomed) in the data arrays
                origpos = origy * originalW + origx;
                zoompos = ypos * zoomW + xpos;
                
                // Move the pixel data from the original array to the zoomed array
                zoomData[zoompos] = sourceData[origpos];
            }            
        }

        // Draw the result
        g.drawRGB(zoomData, 0, zoomW, x, y, zoomW, zoomH, true);
    }
    
    public void drawRotatedImage(Image image, Graphics g, double angle, int x, int y)
    {
        // Get the size of the original image
        int originalW = image.getWidth();
        int originalH = image.getHeight();

        // Calculate rotated image size
        // To do this, first assume the lower-left corner is at (0,0).
        // Then, calculate the other three corners
        double point1x = originalW * Math.cos(angle);
        double point1y = originalW * Math.sin(angle);
        double point2x = -originalH * Math.sin(angle);
        double point2y = originalH * Math.cos(angle);
        double point3x = originalW * Math.cos(angle) - originalH * Math.sin(angle);
        double point3y = originalW * Math.sin(angle) + originalH * Math.cos(angle);

        // Next find the minimum and maximum coordinate values of the corners
        double minx = Math.min( 0, Math.min(point1x , Math.min(point2x , point3x)));
        double miny = Math.min( 0, Math.min(point1y , Math.min(point2y , point3y)));
        double maxx = Math.max( 0, Math.max(point1x , Math.max(point2x , point3x)));
        double maxy = Math.max( 0, Math.max(point1y , Math.max(point2y , point3y)));

        // Finally, calculate the actual width and height of the rotated image
        int rotatedW = (int) Math.floor(Math.abs(maxx - minx));
        int rotatedH = (int) Math.floor(Math.abs(maxy - miny));
        
        // Calculate the "origin" (in our case, the center) of the rotated image
        int referenceX = rotatedW / 2;
        int referenceY = rotatedH / 2;

        // Reserve an array for the pixel data of the original and rotated images
        int [] sourceData = new int[originalW * originalH];
        int [] rotatedData = new int[rotatedW * rotatedH];

        // Retrieve the pixels of the original image
        image.getRGB(sourceData, 0, originalW, 0, 0, originalW, originalH);

        // Variables to mark X,Y pixel positions in the original and rotated images
        int rotX,rotY;
        int origX, origY;

        // Variables to keep track of index positions in the RGB arrays
        int origPos, rotatedPos;

        // Process each pixel of the rotateded image
        for (rotX=0;rotX<rotatedW;rotX++)
        {
            for (rotY=0;rotY<rotatedH;rotY++)
            {
                // For the current "rotated" pixel, calculate the
                // X coordinate of the original image pixel. Since for this operation
                // the origin is assumed to be the center of the rotated image, we
                // must offset the coordinates according to this reference point.
                origX = (int) ( (rotX - referenceX) * Math.cos(angle) - (rotY - referenceY) *  Math.sin(angle) + originalW / 2);

                // Check if the resulting X value falls inside the original image or outside of it
                // Go to the next pixel if this is so.
                if ( origX < 0 || origX >= originalW)
                {
                    continue;
                }

                // Calculate the Y coordinate next
                origY = (int) ( (rotY - referenceY) * Math.cos(angle) + (rotX - referenceX) *  Math.sin(angle) + originalH / 2);

                // Calculate the pixel's would-be position in the original source array
                origPos = origY * originalW + origX ;

                // Check if the position is valid
                // Go to the next pixel if the position is not valid
                if ( origPos < 0 || origPos >= sourceData.length )
                {
                    continue;
                }
                
                // Calculate the "rotated" pixel's positon in the rotated image array
                rotatedPos = rotY * rotatedW + rotX;

                // Move the pixel data from the original array to the rotateded array
                rotatedData[rotatedPos] = sourceData[origPos];
            }
        }

        // Draw the result
        g.drawRGB(rotatedData, 0, rotatedW, x, y, rotatedW, rotatedH, true);
    }



    public void paint(Graphics g)
    {
        g.setColor(255,255,255);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Pick one of the effects below for a quick demo
        //-----------------------------------------------

        //drawMaskedImage(clouds,mask,g, (getWidth()-clouds.getWidth())/2, (getHeight()-clouds.getHeight())/2);
        //drawBlendedImage(clouds,grass,g,200, (getWidth()-clouds.getWidth())/2, (getHeight()-clouds.getHeight())/2);
        //drawMultipliedImage(grass,gradient,g, (getWidth()-clouds.getWidth())/2, (getHeight()-clouds.getHeight())/2);
        //drawResizedImage(grass, g, 3, 0,0);
        drawRotatedImage(grass, g, 0.5, 0, 20);
    }


}

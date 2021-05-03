/**
 * Starter code for Processor - the class that processes images.
 * <p>
 * This class manipulated Java BufferedImages, which are effectively 2d arrays
 * of pixels. Each pixel is a single integer packed with 4 values inside it.
 * <p>
 * I have included two useful methods for dealing with bit-shift operators so
 * you don't have to. These methods are unpackPixel() and packagePixel() and do
 * exactly what they say - extract red, green, blue and alpha values out of an
 * int, and put the same four integers back into a special packed integer. 
 * 
 * Included is a deepCopy method to produce an exact copy of a BufferedImage.
 * 
 * Original author is Jordan Cohen.
 * @author David Yao
 * @version May 2018
 */

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Processor  
{   
    /**
     * Takes the integer value of only every fourth pixel, and applies it to all pixels that are skipped.
     * Produces a blocky (pixelated) effect.
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     */
    public static void pixelate (BufferedImage bi)
    {
        // Get image size to use in for loops
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        int[][] pixels = new int[xSize][ySize];  // Stores all pixels

        // Filling array
        for (int x = 0; x < xSize - 4; x += 4)
        {
            for (int y = 0; y < ySize - 4; y += 4) // for loop y
            {
                pixels [x][y] = bi.getRGB(x, y);  // Only getRGB every four pixels, both horizontally and vertically
                for (int i = 0; i < 8; i++)
                {
                    for (int j = 0; j < 6; j++)
                    {
                        pixels [x + i][y + j] = bi.getRGB(x, y);  // Pixels in the rectangle specified are all set to the pixel extrated in for loop y
                    }
                }
            }
        }
        // Applying array to image
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                bi.setRGB(x, y, pixels [x][y]);
            }
        }
    }

    /**
     * This method will increase the green value while reducing the red and blue values.
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     */
    public static void greenify (BufferedImage bi)
    {
        // Get image size to use in for loops
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Using array size as limit
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                // Calls method in BufferedImage that returns R G B and alpha values
                // encoded together in an integer
                int rgb = bi.getRGB(x, y);

                // Call the unpackPixel method to retrieve the four integers for
                // R, G, B and alpha and assign them each to their own integer
                int[] rgbValues = unpackPixel (rgb);
                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];

                // make the pic BLUE-er
                if (green < 254)
                    green += 2;
                if (red >= 50)
                    red--;
                if (blue >= 50)
                    blue--;

                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB (x, y, newColour);
            }
        }

    }

    /**
     * This method will increase the red value while reducing the green and blue values.
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     */
    public static void redify (BufferedImage bi)
    {
        // Get image size to use in for loops
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Using array size as limit
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                // Calls method in BufferedImage that returns R G B and alpha values
                // encoded together in an integer
                int rgb = bi.getRGB(x, y);

                // Call the unpackPixel method to retrieve the four integers for
                // R, G, B and alpha and assign them each to their own integer
                int[] rgbValues = unpackPixel (rgb);
                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];

                // make the pic RED-er
                if (red < 254)
                    red += 2;
                if (blue >= 50)
                    blue--;
                if (green >= 50)
                    green--;

                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB (x, y, newColour);
            }
        }

    }

    /**
     * Example colour altering method by Mr. Cohen. This method will
     * increase the blue value while reducing the red and green values.
     * 
     * Demonstrates use of packagePixel() and unpackPixel() methods.
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     */
    public static void blueify (BufferedImage bi)
    {
        // Get image size to use in for loops
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Using array size as limit
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                // Calls method in BufferedImage that returns R G B and alpha values
                // encoded together in an integer
                int rgb = bi.getRGB(x, y);

                // Call the unpackPixel method to retrieve the four integers for
                // R, G, B and alpha and assign them each to their own integer
                int[] rgbValues = unpackPixel (rgb);
                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];

                // make the pic BLUE-er
                if (blue < 254)
                    blue += 2;
                if (red >= 50)
                    red--;
                if (green >= 50)
                    green--;

                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB (x, y, newColour);
            }
        }

    }

    /**
     * Reverses x-axis of 2D array to flip the image horizontally.
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     */
    public static void flipHorizontal (BufferedImage bi)
    {
        // Get image size to use in for loops
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Temp image, to store pixels as we reverse everything
        BufferedImage newBi = new BufferedImage (xSize, ySize, 3);
        int[][] pixels = new int[xSize][ySize];

        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                pixels [x][y] = bi.getRGB(x, y);
                newBi.setRGB (xSize - x - 1, y, pixels [x][y]);  // 'Drawing' new image by placing pixels starting from the right instead of the left
            }
        }
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                bi.setRGB(x, y, newBi.getRGB(x, y));  // Applying new image to image
            }
        }
    }

    /**
     * Reverses y-axis of 2D array to flip the image vertically.
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     */
    public static void flipVertical (BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Temp image, to store pixels as we reverse everything
        BufferedImage newBi = new BufferedImage (xSize, ySize, 3);
        int[][] pixels = new int[xSize][ySize];

        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                pixels [x][y] = bi.getRGB(x, y);
                newBi.setRGB (x, ySize - y - 1, pixels [x][y]);
            }
        }
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                bi.setRGB(x, y, newBi.getRGB(x, y));
            }
        }
    }

    /**
     * Shifts the image to greyscale by changing the BufferedImage type to TYPE_BYTE_GRAY.
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     */
    public static void greyScale (BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Temp image, to store pixels as we reverse everything
        BufferedImage newBi = new BufferedImage (xSize, ySize, 10);  // Type 10 (TYPE_BYTE_GRAY) is grayscale only
        int[][] pixels = new int[xSize][ySize];

        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                pixels [x][y] = bi.getRGB(x, y);
                newBi.setRGB (x, y, pixels [x][y]);  // Populating array with old image then feeding it into new image, decolourizing in process
            }
        }
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                bi.setRGB(x, y, newBi.getRGB(x, y));
            }
        }
    }

    /**
     * Changes all colours to their negatives (255 - (RGB value))
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     */
    public static void negative (BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = bi.getRGB(x, y);

                // Call the unpackPixel method to retrieve the four integers for
                // R, G, B and alpha and assign them each to their own integer
                int[] rgbValues = unpackPixel (rgb);

                int alpha = rgbValues[0];
                // Changing all colours to negatives
                int red = 255 - rgbValues[1];
                int green = 255 - rgbValues[2];
                int blue = 255 - rgbValues[3];

                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB(x, y, newColour);
            }
        }
    }

    /**
     * Brightens entire image by shifting closer to white (255)
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     */
    public static void brighten (BufferedImage bi)
    {
        // Get image size to use in for loops
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Using array size as limit
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                // Calls method in BufferedImage that returns R G B and alpha values
                // encoded together in an integer
                int rgb = bi.getRGB(x, y);

                // Call the unpackPixel method to retrieve the four integers for
                // R, G, B and alpha and assign them each to their own integer
                int[] rgbValues = unpackPixel (rgb);
                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];

                // make the pic WHITE-er
                if (green < 254)
                    green += 2;
                if (red < 254)
                    red += 2;
                if (blue < 254)
                    blue += 2;

                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB (x, y, newColour);
            }
        }
    }

    /**
     * Shifts RGB values for every pixel to one of the other two colours.
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     */
    public static void scramble (BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                int rgb = bi.getRGB(x, y);

                int[] rgbValues = unpackPixel (rgb);

                int alpha = rgbValues[0];
                int red = rgbValues[3]; // normally rgbValues[1]
                int green = rgbValues[1]; // normally rgbValues[2]
                int blue = rgbValues[2]; // normally rgbValues[3]

                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB(x, y, newColour);
            }
        }
    }

    /**
     * Changes BufferedImage bi back to BufferedImage oldBi, representing current image and given image from ArrayList in World class, respectively
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     * @param oldBi The BufferedImage that bi will change into. Draws from ArrayList of previous images to achieve undo.
     */
    public static void undoEdit (BufferedImage bi, BufferedImage oldBi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Temp image, to store pixels as we reverse everything
        int[][] pixels = new int[xSize][ySize];

        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                pixels [x][y] = oldBi.getRGB(x, y);  // Populate array with pixels of old image
                bi.setRGB (x, y, pixels [x][y]);  // Feed pixels into current image
            }
        }
    }

    /**
     * Changes BufferedImage type to TYPE_3BYTE_BGR, no alpha to facilitate compatibility with JPG format which is incompatible with transparency
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     * @return BufferedImage Returns image that is compatible with JPG.
     */
    public static BufferedImage makeJPGCompatible (BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Temp image, to store pixels as we reverse everything
        BufferedImage newBi = new BufferedImage (xSize, ySize, 5);  // Type 5 is TYPE_3BYTE_BGR
        int[][] pixels = new int[xSize][ySize];
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                pixels [x][y] = bi.getRGB(x, y);
                newBi.setRGB (x, y, pixels [x][y]);
            }
        }
        return newBi;
    }

    /**
     * Takes in an rgb value - the kind that is returned from BufferedImage's
     * getRGB() method - and returns 4 integers for easy manipulation.
     * 
     * By Jordan Cohen
     * Version 0.2
     * 
     * @param rgbaValue The value of a single pixel as an integer, representing<br>
     *                  8 bits for red, green and blue and 8 bits for alpha:<br>
     *                  <pre>alpha   red     green   blue</pre>
     *                  <pre>00000000000000000000000000000000</pre>
     * @return int[4]   Array containing 4 shorter ints<br>
     *                  <pre>0       1       2       3</pre>
     *                  <pre>alpha   red     green   blue</pre>
     */
    public static int[] unpackPixel (int rgbaValue)
    {
        int[] unpackedValues = new int[4];
        // alpha
        unpackedValues[0] = (rgbaValue >> 24) & 0xFF;
        // red
        unpackedValues[1] = (rgbaValue >> 16) & 0xFF;
        // green
        unpackedValues[2] = (rgbaValue >>  8) & 0xFF;
        // blue
        unpackedValues[3] = (rgbaValue) & 0xFF;

        return unpackedValues;
    }

    /**
     * Takes in a red, green, blue and alpha integer and uses bit-shifting
     * to package all of the data into a single integer.
     * 
     * @param   int red value (0-255)
     * @param   int green value (0-255)
     * @param   int blue value (0-255)
     * @param   int alpha value (0-255)
     * 
     * @return int  Integer representing 32 bit integer pixel ready
     *              for BufferedImage
     */
    public static int packagePixel (int r, int g, int b, int a)
    {
        int newRGB = (a << 24) | (r << 16) | (g << 8) | b;
        return newRGB;
    }

    /**
     * Creates an exact copy of the BufferedImage that is given.
     * By Jordan Cohen
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     * @return BufferedImage Exact copy of bi, in a different object.
     */
    public static BufferedImage deepCopy(BufferedImage bi) 
    {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultip = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultip, null);
    }
}

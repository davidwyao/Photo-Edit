import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;

/**
 * Simple class that serves to be an Actor to display the image.
 * 
 * (Revised 11/14 to avoid crashing if user cancels import operation).
 * 
 * Original author is Jordan Cohen.
 * @author David Yao
 * @version May 2018
 */
public class ImageHolder extends Actor
{
    private GreenfootImage imageToDisplay;
    private BufferedImage imageToJPG;  // Adapted image on canvas for compatibility with JPG file limitations

    /**
     * Construct an ImageHolder with a file name. If there is an error, 
     * show a blank GreenfootImage.
     * 
     * @param fileName  Name of image file to be displayed.
     */
    public ImageHolder (String fileName)
    {
        openFile (fileName);
    }

    /**
     * Attempt to open a file and assign it as this Actor's image
     * 
     * @param fileName  Name of the image file to open (must be in this directory)
     * @return boolean  True if operation successful, otherwise false
     */
    public boolean openFile (String fileName)
    {
        try {
            if (fileName != null)
            {
                imageToDisplay = new GreenfootImage (fileName);
                setImage(imageToDisplay);
            }
            else
                return false;
        }
        catch (IllegalArgumentException e)
        {
            JOptionPane.showMessageDialog(null, "File name invalid or not found. Please make sure full extension is included");
            return false;
        }
        return true;
    }

    /**
     * Attempt to create a new PNG file in the program directory
     * 
     * @param fileName  Name of the image file to be created
     * @return boolean  True if operation successful, otherwise false
     */
    public boolean exportPNG (String fileName)
    {
        try {
            if (fileName != null)
            {
                ImageIO.write(this.getBufferedImage(), "png", new File(fileName + ".png"));
            }
            else
                return false;
        }
        catch (IOException e)  // If an error occurs during the exporting process, hasn't happened yet
        {
            JOptionPane.showMessageDialog(null, "Sorry, there was an error exporting the image.");
            return false;
        }
        return true;
    }

    /**
     * Attempt to create a new JPG file in the program directory
     * 
     * @param fileName  Name of the image file to be created
     * @return boolean  True if operation successful, otherwise false
     */
    public boolean exportJPG (String fileName)
    {
        try {
            if (fileName != null)
            {
                imageToJPG = Processor.makeJPGCompatible(this.getBufferedImage());  // Changes type of BufferedImage to prevent corruption when exporting file as JPG
                ImageIO.write(imageToJPG, "jpg", new File(fileName + ".jpg"));
            }
            else
                return false;
        }
        catch (IOException e)  // If an error occurs during the exporting process
        {
            JOptionPane.showMessageDialog(null, "Sorry, there was an error exporting the image.");
            return false;
        }
        return true;
    }

    /**
     * Allows access to my awtImage - the backing data underneath the GreenfootImage class.
     * 
     * @return BufferedImage returns the backing image for this Actor as an AwtImage
     */
    public BufferedImage getBufferedImage ()
    {
        return this.getImage().getAwtImage();
    }

}

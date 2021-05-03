import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.File;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
/**
 * Starter code for Image Manipulation Array Assignment.
 * 
 * The class Processor contains all of the code to actually perform
 * transformation. The rest of the classes serve to support that
 * capability. This World allows the user to choose transformations
 * and open files.
 * 
 * Add to it and make it your own!
 * 
 * David's features: added error messages with input
 * Original author is Jordan Cohen.
 * 
 * @author David Yao
 * @version May 2018
 */
public class Background extends World
{
    // Constants:
    private final String STARTING_FILE = "patryu.jpg";

    // Objects and Variables:

    private int edits;  // How many edits (filter button presses) are made. Helps determine index of ArrayList of previous images.

    private ImageHolder image;

    // Enacts various filters
    private TextButton blueButton;
    private TextButton redButton;
    private TextButton greenButton;
    private TextButton hRevButton;
    private TextButton vRevButton;
    private TextButton gScaleButton;
    private TextButton negButton;
    private TextButton pixelButton;
    private TextButton brightButton;
    private TextButton scrambleButton;

    private TextButton undoButton;

    // Export canvas to external PNG or JPG file
    private TextButton exportPNGButton;
    private TextButton exportJPGButton;
    
    private TextButton openFile;

    private String fileName;

    private ArrayList<BufferedImage> images;  // List of previous images, used for undo function

    /**
     * Constructor for objects of class Background.
     * 
     */
    public Background()
    {    
        // Create a new world with 960x768 cells with a cell size of 1x1 pixels.
        super(960, 768, 1); 

        edits = 0;

        // Initialize buttons and the image
        image = new ImageHolder(STARTING_FILE);
        blueButton = new TextButton(" [ Blue-ify ] ");
        redButton = new TextButton(" [ Red-ify ] ");
        greenButton = new TextButton(" [ Green-ify ] ");
        hRevButton = new TextButton(" [ Flip Horizontal ] ");
        vRevButton = new TextButton(" [ Flip Vertical ] ");
        gScaleButton = new TextButton(" [ Grayscale ] ");
        negButton = new TextButton(" [ Negative ] ");
        pixelButton = new TextButton(" [ Pixelate ] ");
        brightButton = new TextButton(" [ Brighten ] ");
        scrambleButton = new TextButton(" [ Scramble! ] ");

        undoButton = new TextButton(" [ Undo ] ");

        exportPNGButton = new TextButton(" [ Export as PNG ] ");
        exportJPGButton = new TextButton(" [ Export as JPG ] ");
        openFile = new TextButton(" [ Open File: " + STARTING_FILE + " ] ");

        // Add objects to the screen
        addObject (image, 370, 400);

        addObject (pixelButton, 796, 64);
        addObject (greenButton, 800, 96);
        addObject (redButton, 794, 128);
        addObject (blueButton, 795, 160);
        addObject (hRevButton, 817, 192);
        addObject (vRevButton, 808, 224);
        addObject (gScaleButton, 804, 256);
        addObject (negButton, 799, 288);
        addObject (brightButton, 799, 320);
        addObject (scrambleButton, 806, 352);

        addObject (undoButton, 550, 24);

        addObject (exportPNGButton, 300, 24);
        addObject (exportJPGButton, 440, 24);
        addObject (openFile, 105, 24);

        // Initialize ArrayList
        images = new ArrayList<BufferedImage>();
        images.add(Processor.deepCopy(image.getBufferedImage()));  // Set original image as first object
    }

    /**
     * Act() method just checks for mouse input
     */
    public void act ()
    {
        checkMouse();
    }

    /**
     * Check for user clicking on a button
     */
    private void checkMouse ()
    {
        // Avoid excess mouse checks - only check mouse if somethething is clicked.
        if (Greenfoot.mouseClicked(null))
        {
            // Each of these calls a different filter method in the Processor class.
            if (Greenfoot.mouseClicked(blueButton)){
                Processor.blueify(image.getBufferedImage());
                addToList();  // Adds newly created image to ArrayList of edited images.
            }
            else if (Greenfoot.mouseClicked(redButton)){
                Processor.redify(image.getBufferedImage());
                addToList();
            }
            else if (Greenfoot.mouseClicked(greenButton)){
                Processor.greenify(image.getBufferedImage());
                addToList();
            }
            else if (Greenfoot.mouseClicked(hRevButton)){
                Processor.flipHorizontal(image.getBufferedImage());
                addToList();
            }
            else if (Greenfoot.mouseClicked(vRevButton)){
                Processor.flipVertical(image.getBufferedImage());
                addToList();
            }
            else if (Greenfoot.mouseClicked(gScaleButton)){
                Processor.greyScale(image.getBufferedImage());
                addToList();
            }
            else if (Greenfoot.mouseClicked(pixelButton)){
                Processor.pixelate(image.getBufferedImage());
                addToList();
            }
            else if (Greenfoot.mouseClicked(negButton)){
                Processor.negative(image.getBufferedImage());
                addToList();
            }
            else if (Greenfoot.mouseClicked(brightButton)){
                Processor.brighten(image.getBufferedImage());
                addToList();
            }
            else if (Greenfoot.mouseClicked(scrambleButton)){
                Processor.scramble(image.getBufferedImage());
                addToList();
            }

            else if (Greenfoot.mouseClicked(undoButton)){
                if (edits > 0){  // Will not undo if no edits have been made
                    Processor.undoEdit(image.getBufferedImage(), images.get(edits - 1));
                    edits--;  // Will access previous image in ArrayList the next time this is called
                }
            }

            else if (Greenfoot.mouseClicked(exportPNGButton)){
                exportPNG ();
            }
            else if (Greenfoot.mouseClicked(exportJPGButton)){
                exportJPG ();
            }
            else if (Greenfoot.mouseClicked(openFile))
            {
                openFile ();
            }
        }
    }

    /**
     * Adds image being displayed at the time of calling to the ArrayList of images, then increments edit counter.
     */
    private void addToList ()
    {
        images.add(Processor.deepCopy(image.getBufferedImage()));  // Avoiding passing by reference
        edits++;
    }

    /**
     * Allows the user to open a new image file.
     */
    private void openFile ()
    {
        // Use a JOptionPane to get file name from user
        String fileName = JOptionPane.showInputDialog("Please input a file name with extension");

        // If the file opening operation is successful, update the text in the open file button
        if (image.openFile (fileName))
        {
            String display = " [ Open File: " + fileName + " ] ";
            openFile.update (display);
            images.clear();  // Cannot undo back to previous image
            edits = 0;
            images.add(Processor.deepCopy(image.getBufferedImage()));  // Reset everything, set first object in ArrayList to new image
        }
    }

    /**
     * Export canvas to a new PNG file in the directory of the program.
     */
    private void exportPNG ()
    {
        // Use a JOptionPane to get file name from user
        String fileName = JOptionPane.showInputDialog("Please input a file name, without extensions");

        // If the export operation is successful, display a popup message
        if (image.exportPNG (fileName))
        {
            JOptionPane.showMessageDialog(null, "Image successfully exported: '" + fileName + ".png'");
        }
    }

    /**
     * Export canvas to a new JPG file in the directory of the program.
     */
    private void exportJPG ()
    {
        // Use a JOptionPane to get file name from user
        String fileName = JOptionPane.showInputDialog("Please input a file name, without extensions");

        // If the export operation is successful, display a popup message
        if (image.exportJPG (fileName))
        {
            JOptionPane.showMessageDialog(null, "Image successfully exported: '" + fileName + ".jpg'");
        }
    }
}


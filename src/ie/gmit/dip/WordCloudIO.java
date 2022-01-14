package ie.gmit.dip;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

/**
 * <p>A class which handles IO parameters and methods for writing an image rendered by <code>WordCloudRenderer</code> to a specified path as a <code>PNG</code> image file.</p>
 * 
 * @version 1.0
 * @since 1.8
 */
public final class WordCloudIO {
    static File imageDir = Files.isWritable(new File(System.getProperty("user.dir")).toPath()) ? new File(System.getProperty("user.dir")) : new File(System.getProperty("user.home")); // The default value for this is the user's working directory. If for whatever reason they run the app from a place they don't have writing permissions at, their home directory is chosen instead. They can change this anyway through the menu, this is just to avoid any critical IO errors. If your permissions change WHILE you're using the app, well played, enjoy the error message logged by writeImageFile.
    static String imageFileName = "wcloud.png";

    /**
     * <p>Sets the output directory for the image file.</p>
     *
     * @param file The new output directory for the image file.
     */
    // O(1) simple getter/setter
    public static void setImageDir(File file) {
        imageDir = file;
    }

    /**
     * <p>Sets the file name for the output image.</p>
     *
     * @param file A <code>File</code> object with the desired file name.
     */
    // O(1) simple getter/setter, "endsWith" only checks the last 4 chars here
    public static void setImageFileName(File file) {
        if (file != null)
            if (file.getName().endsWith(".png") || file.getName().endsWith(".PNG")) imageFileName = file.getName();
            else
                imageFileName = file.getName() + ".png";
    }

    /**
     * Returns a new <code>File</code> object which points to the path under which the output image will be saved.
     *
     * @return A <code>File</code> object instantiated using the image path and the image file name.
     */
    // O(1) simple getter/setter
    public static File getImageFile() {
        return new File(imageDir, imageFileName);
    }

    /**
     * <p>Checks if there already is an existing file at the output path.</p>
     *
     * @return True if a file exists, or else, false.
     */
    // O(1) just reading system properties
    public static boolean imageFileExists() {
        return Files.isRegularFile(getImageFile().toPath());
    }

    /**
     * <p>Asks the user if they want to overwrite an existing file.</p>
     * @return True, if the user gives their permission, or else, false.
     */
    // O(n) because Strings are being compared. No guarantees on user input inside a loop, could go on forever.
    public static boolean willOverwrite() {
        String answer = "none";
        while (!(answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")) && !answer.equals("")) {
            System.out.println(Strings.IO_WRITING_IMAGE_FILE_EXISTS_OVERWRITE.get());
            System.out.print(Strings.IO_INPUT_CURSOR.get());
            Scanner console = new Scanner(System.in);
            String input;
            if ((input = console.nextLine()) != null) answer = input;
        }
        return !answer.equals("");
    }

    /**
     * <p>Writes an <code>image</code> under a path and file name stored in class variables as a <code>PNG</code> file. Checks if the file exists before saving and asks the user to confirm via the console.</p>
     *
     * @param image The image to be written. Cannot be null.
     */
    // The PNG writer needs to iterate over the image. Depending on its implementation, it could be O(n^2) in the worst case.
    public static void writeImageFile(BufferedImage image) {
        if (image != null) {
            try {
                ImageIO.write(image, "png", getImageFile());
                Runner.log(Strings.IO_WRITING_IMAGE_SUCCESS.get());
            } catch (IOException e) {
                Runner.log(Strings.IO_WRITING_IMAGE_ERROR.get());
            }
        }
    }

    private WordCloudIO() {
    }
}
package ie.gmit.dip;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;

/**
 * <p>A class that takes in user input as a <code>String</code> on construction, safely interprets it as a <code>File</code> path and verifies that the file exists.</p>
 *
 * @version 1.0
 * @since 1.8
 */
public class FileFromConsole extends ConsoleValidator<File> {
    /**
     * <p>Safely interprets a <code>String</code> as a <code>File</code> path and returns the resulting object.</p>
     *
     * @param input A string to be interpreted as a <code>File</code> path; the path is made canonical to make sure the result is unique.
     * @return A <code>File</code> object based on the <code>input</code>.
     */
    // O(1) no loops here
    public File recast(String input) {
        File file = null;
        try {
            file = new File(input);
            file = file.getCanonicalFile();
        } catch (IOException ignored) {
        }
        return file;
    }

    /**
     * <p>Verifies that the <code>File</code> exists.</p>
     *
     * @param file The <code>File</code> in question.
     * @return True, if the file exists, otherwise false.
     */
    // O(1) no loops here
    public boolean validate(File file) {
        boolean isValid;
        try {
            isValid = Files.isRegularFile(file.toPath());
        } catch (InvalidPathException e) {
            return false;
        }
        return isValid;
    }
}

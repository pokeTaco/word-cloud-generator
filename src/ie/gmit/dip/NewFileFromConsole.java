package ie.gmit.dip;

import java.io.File;
import java.io.IOException;

/**
 * <p>A class that takes in user input as a <code>String</code> on construction, safely interprets it as a <code>File</code> name and verifies that a new file with the name can be created.</p>
 *
 * @version 1.0
 * @since 1.8
 */
public class NewFileFromConsole extends FileFromConsole {
    /**
     * Verifies that the <code>File</code> can be created.<br>
     * Source: <a href="https://www.baeldung.com/java-validate-filename">https://www.baeldung.com/java-validate-filename</a>
     *
     * @param file The <code>File</code> in question.
     * @return True, if the file can be created, otherwise false.
     */
    // O(1) no loops just computation
    public boolean validate(File file) {
        boolean isValid = false;
        try {
            isValid = file.createNewFile();
        } catch (IOException e) {
            return false;
        } finally {
            if (isValid) file.delete();
        }
        return isValid;
    }
}
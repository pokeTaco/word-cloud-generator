package ie.gmit.dip;

import java.io.File;
import java.nio.file.Files;

/**
 * <p>A class that takes in user input as a <code>String</code> on construction, safely interprets it as a <code>File</code> object and verifies that the object points to a writable directory.</p>
 *
 * @version 1.0
 * @since 1.8
 */
public class DirFromConsole extends FileFromConsole {
    /**
     * Verifies that the <code>File</code> points to a writable directory.<br>
     *
     * @param file The <code>File</code> in question.
     * @return True, if it points to a writeable directory, otherwise false.
     */
    // O(1) No loops just reading system attributes
    public boolean validate(File file) {
        boolean isDir = Files.isDirectory(file.toPath());
        boolean isWriteable = Files.isWritable(file.toPath());
        return isDir && isWriteable;
    }
}
package ie.gmit.dip;

import java.io.*;
import java.util.Map;

/**
 * <p>A <code>Runnable</code> used to read a text from a source <code>File</code> line by line and token by token, count the occurrences of each token, and store the frequency table in a <code>Map</code>.</p>
 *
 * @version 1.0
 * @since 1.8
 */
public class FileFrequencyTabulator extends FrequencyTabulator<File> {
    /**
     * <p>Returns a <code>BufferedReader</code> that reads the content of the source <code>File</code> line by line.</p>
     *
     * @param source The file containing the text to be buffered.
     * @return A <code>BufferedReader</code> for the source <code>file</code>, or <code>null</code> if the object fails to be created.
     */
    // O(1) no loops here
    public BufferedReader read(File source) {
        try {
            return new BufferedReader(new FileReader(source));
        } catch (FileNotFoundException e) {
            return null;
        }
    }
    // O(1) no loops here
    public String toString() {
        try {
            return source().getCanonicalPath();
        } catch (IOException e) {
            return null;
        }
    }

    public FileFrequencyTabulator(File file, Map<String, Integer> frequencyTable) {
        super(file, frequencyTable);
    }
}
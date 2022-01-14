package ie.gmit.dip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

/**
 * <p>A <code>Runnable</code> used to read a text from a source <code>URL</code> line by line and token by token, count the occurrences of each token, and store the frequency table in a <code>Map</code>.</p>
 *
 * @version 1.0
 * @since 1.8
 */
public class URLFrequencyTabulator extends FrequencyTabulator<URL> {
    /**
     * <p>Returns a <code>BufferedReader</code> that reads the content at the source <code>URL</code> line by line.</p>
     *
     * @param source The URL pointing at the text to be buffered.
     * @return A <code>BufferedReader</code> for the source <code>URL</code>, or <code>null</code> if the object fails to be created.
     */
    // O(1), disregarding IO operations
    public BufferedReader read(URL source) {
        try {
            return new BufferedReader(new InputStreamReader(source.openStream()));
        } catch (IOException e) {
            return null;
        }
    }

    // O(1) no loops
    @Override
    public String toString() {
        return source().toString();
    }

    public URLFrequencyTabulator(URL url, Map<String, Integer> frequencyTable) {
        super(url, frequencyTable);
    }
}
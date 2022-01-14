package ie.gmit.dip;

import java.io.BufferedReader;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * <p>A description of the process used to read a text from a <i>source type</i> <code>T</code> line by line and token by token, count the occurrences of each token, and store the frequency table in a <code>Map</code>.</p>
 *
 * @param <T> The source type, such as a file or URL.
 * @version 1.0
 * @since 1.8
 */
public interface FrequencyTabulation<T> extends Runnable {
    /**
     * <p>Returns a <code>BufferedReader</code> that reads the content of the <code>source</code> line by line.</p>
     *
     * @param source The object containing the text to be buffered.
     * @return A <code>BufferedReader</code> for the <code>source</code>, or <code>null</code> if the object fails to be created.
     */
    BufferedReader read(T source);

    /**
     * <p>Returns a <code>StringTokenizer</code> that reads the content of <code>line</code> token by token after removing tags, English contractions, and any non-word artefacts.</p>
     *
     * @param line The <code>String</code> containing tokens to be buffered.
     * @return A <code>StringTokenizer</code> for the parameter <code>line</code>.
     */
    StringTokenizer tokenize(String line);

    /**
     * <p>If the target <code>map</code> already contains the <code>word</code> as a key, increases its value by 1; or else, adds the <code>word</code> as a new key with an assigned value of 1.</p>
     *
     * @param word A single token.
     * @param map  The map that holds the frequency table.
     */
    void put(String word, Map<String, Integer> map);
}
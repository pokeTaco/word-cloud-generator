package ie.gmit.dip;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * <p>A <code>Runnable</code> used to read a text from a <i>source type</i> <code>T</code> line by line and token by token, count the occurrences of each token, and store the frequency table in a <code>Map</code>.</p>
 *
 * @param <T> The source type, such as a file or URL.
 * @version 1.0
 * @since 1.8
 */
public abstract class FrequencyTabulator<T> implements FrequencyTabulation<T> {
    private final T SOURCE;
    private final Map<String, Integer> FREQUENCY_TABLE;

    /**
     * <p>Returns the <code>source</code>. This gives subtypes the access they need to override <code>toString</code>.</p>
     *
     * @return The original <code>source</code> type.
     */
    // O(1) no loops here
    public T source() {
        return this.SOURCE;
    }

    /**
     * Overrides <code>toString</code> with a unique, user-friendly String representation of their source type object.
     *
     * @return A unique, user-friendly String representation of this instance's source type object.
     */
    @Override
    public abstract String toString();

    /**
     * <p>Returns a <code>StringTokenizer</code> that reads the content of <code>line</code> token by token after removing tags, English contractions, and any non-word artefacts.</p>
     * <p>This method runs in <b>linear time O(n)</b> because it calls methods for String manipulation, such as <code>replaceAll</code> and <code>toLowerCase</code>.</p>
     *
     * @param line The <code>String</code> containing tokens to be buffered.
     * @return A <code>StringTokenizer</code> for the parameter <code>line</code>.
     */
    // O(n) see JavaDocs
    public StringTokenizer tokenize(String line) {
        String pattern = Strings.PARSER_PATTERN_HTML.get() + "|" + Strings.PARSER_PATTERN_CONTRACTIONS.get(); // These two patterns should be safe to go at the same time.
        return new StringTokenizer(
                line.replaceAll(pattern, " ")
                        .replaceAll(Strings.PARSER_PATTERN_ARTEFACTS.get(), " ")
                        .toLowerCase()
        );
    }

    /**
     * <p>If the target <code>map</code> already contains the <code>word</code> as a key, increases its value by 1; or else, adds the <code>word</code> as a new key with an assigned value of 1.</p>
     * <p>This method runs in <b>constant time O(1)</b> (as per Golden Rule).</p>
     *
     * @param word A single token.
     * @param map  The map that holds the frequency table.
     */
    // O(1) see JavaDocs
    public void put(String word, Map<String, Integer> map) {
        int occurrences = 1;
        if (map.containsKey(word)) occurrences = map.get(word) + 1;
        map.put(word, occurrences);
    }

    public FrequencyTabulator(T type, Map<String, Integer> frequencyMap) {
        this.SOURCE = type;
        this.FREQUENCY_TABLE = frequencyMap;
    }

    /**
     * Determines object equality based on <code>toString</code>.
     *
     * @param obj The object being compared.
     * @return True, if the objects are equal, or else, false.
     */
    // O(1) no loops just computation
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof FrequencyTabulator<?>)) return false;
        FrequencyTabulator<?> wm = (FrequencyTabulator<?>) obj;
        return this.toString().equals(wm.toString());
    }

    /**
     * Returns the source type object's hash code.
     *
     * @return The source type object's hash code.
     */
    // O(1) no loops just computation
    @Override
    public int hashCode() {
        return Objects.hash(SOURCE);
    }

    /**
     * <p>Executes <code>read</code> (constant time), <code>tokenize</code> (linear time) for each line, and <code>put</code> (constant time) for each word.</p>
     * <p>This method runs in linear time O(n) overall.</p>
     */
    // O(n) see JavaDocs
    public void run() {
        BufferedReader reader = read(this.SOURCE);
        if (reader != null) {
            String nextLine;
            while (true) {
                try {
                    nextLine = reader.readLine();
                    if (nextLine == null) break;
                    StringTokenizer tokenizer = tokenize(nextLine);
                    while (tokenizer.hasMoreTokens()) {
                        String token = tokenizer.nextToken();
                        if ((token.length() >= Tabulators.getMinWordLength()) && !(Tabulators.getStopWords().contains(token)))
                            put(token, this.FREQUENCY_TABLE);
                    }
                } catch (IOException e) {
                    Runner.log(Strings.PARSER_ERROR_READING_SOURCE.get());
                }
            }
        } else Runner.log(Strings.PARSER_ERROR_READING_SOURCE.get());
    }
}
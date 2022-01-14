package ie.gmit.dip;

import javafx.scene.control.Tab;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>A class that collects <code>FrequencyTabulator</code> objects in a <code>List</code>, then executes them simultaneously and stores the resulting frequency table in a thread-safe <code>Map</code>. Its <code>execute</code> method returns a <code>List</code> of words <b>sorted by frequency in descending order</b>.</p>
 *
 * @version 1.0
 * @since 1.8
 */
public final class Tabulators {
    private static List<FrequencyTabulator<?>> tabulators = new ArrayList<>();
    private static final Map<String, Integer> FREQUENCY_TABLE = new ConcurrentHashMap<>();
    private static Set<String> stopWords = new TreeSet<>();
    private static int minWordLength = 4;

    /**
     * <p>Checks if a <code>FrequencyTabulator</code> already exists in this class's object list.</p>
     * <p>This method runs in <b>linear time O(n)</b>.</p>
     *
     * @param ftNew The <code>FrequencyTabulator</code> in question.
     * @return True, if the new object is unique, or false, if an equal object is found in the list.
     */
    // O(n) due to the loop -> TABULATORS.size()
    private static boolean isUnique(FrequencyTabulator<?> ftNew) {
        for (FrequencyTabulator<?> ft : tabulators) {
            if (ftNew.equals(ft)) {
                Runner.log(Strings.PARSER_JOB_LIST_ERROR_DUPE.get());
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the minimum length, below which tokens are discarded by the parsing algorithm.
     *
     * @return The minimum word length.
     */
    // O(1) simple getter/setter
    public static int getMinWordLength() {
        return minWordLength;
    }

    /**
     * Sets the minimum length, below which tokens are discarded by the parsing algorithm.
     *
     * @param length The new minimum word length.
     */
    // O(1) simple getter/setter
    public static void setMinWordLength(int length) {
        minWordLength = length;
        Runner.log(Strings.VAR_SET_MIN_WORD_LENGTH.get());
    }

    /**
     * Returns the <code>TreeSet</code> of stop words that are discarded by the parsing algorithm.
     *
     * @return The stop words stored in the class variable. The <code>Set</code> may be empty if the "ignorewords" text file is empty or has not been read.
     */
    // O(1) simple getter/setter
    public static Set<String> getStopWords() {
        return new TreeSet<>(stopWords);
    }

    /**
     * <p>Returns a user-friendly list of currently buffered <code>FrequencyTabulator</code>s to be displayed in the application's menu.</p>
     * <p>This method runs in <b>linear time O(n)</b>.</p>
     *
     * @return A <code>String</code> object that contains a numbered list of frequency tabulators. If the list is empty, returns <code>null</code>.
     */
    // O(n) due to the loop -> TABULATORS.size()
    public static String list() {
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (FrequencyTabulator<?> ft : tabulators) {
            sb.append("  ").append(index).append(") ").append(ft).append("\n");
            index++;
        }
        return sb.toString();
    }

    /**
     * <p>Obtains a <code>TreeSet</code> of stop words by mapping the content of the "ignorewords" text file, copying the key set, and discarding the frequency table.</p>
     * <p>To avoid having to call this method every time <code>minWordLength</code> is changed, it temporarily assigns <code>minWordLength</code> a value of 1.</p>
     * <p>This method runs in <b>linear time O(n)</b>.</p>
     */
    // As per brief: You can assume that the file is available in the current directory and should refer to it as "./ignorewords.txt". So, I'm hard-coding this one. It won't work if the JAR is executed from any directory which doesn't have an ignorewords.txt!
    // (Note: Granted, cloning the keys is yet another thing done in linear time, but that won't affect overall time complexity in a big way because even if we forego the tokenizer, we'll need to pre-process the words somehow (toLowerCase!); also, subtyping a dedicated parser for the sake of calling "Set.add" instead of "Map.put" would be weird.)
    public static void bufferStopWords() {
        try {
            if (Files.isRegularFile(Paths.get(Strings.PARSER_STOP_WORDS_IMPORT_PATH.get()))) {
                int temp = minWordLength;
                minWordLength = 1;
                FrequencyTabulator<File> ft = new FileFrequencyTabulator(new File(Strings.PARSER_STOP_WORDS_IMPORT_PATH.get()), FREQUENCY_TABLE);
                Thread td = new Thread(ft);
                td.start();
                td.join();
                stopWords = new TreeSet<>(FREQUENCY_TABLE.keySet());
                FREQUENCY_TABLE.clear();
                minWordLength = temp;
                Runner.log(Strings.PARSER_STOP_WORDS_IMPORT_SUCCESS.get());
            } else {
                Runner.log(Strings.PARSER_STOP_WORDS_IMPORT_ERROR.get());
            }
        } catch (Exception e) {
            Runner.log(Strings.PARSER_STOP_WORDS_IMPORT_ERROR.get());
        }
    }

    /**
     * <p>Clears the list of <code>FrequencyTabulator</code>s stored in the class variable.</p>
     */
    // O(1) The implementations for .clear of ArrayLists/LinkedLists take O(n) so it's faster to just instantiate a new list.
    public static void clear() {
        tabulators = new ArrayList<>();
        Runner.log(Strings.PARSER_JOB_LIST_RESET.get());
    }

    /**
     * Instantiates a <code>URLFrequencyTabulator</code> and checks if it is a duplicate; if it is not, adds it to the list.
     * <p>This method runs in <b>linear time O(n)</b> because it calls <code>isUnique</code>.</p>
     *
     * @param url The <code>URL</code> used to instantiate the new tabulator.
     */
    // O(n) see JavaDocs
    public static void add(URL url) {
        if (url != null) {
            URLFrequencyTabulator ft = new URLFrequencyTabulator(url, FREQUENCY_TABLE);
            if (isUnique(ft)) {
                tabulators.add(ft);
                Runner.log(Strings.PARSER_JOB_LIST_ADD_SUCCESS_URL.get());
            }
        }
    }

    /**
     * Instantiates a <code>FileFrequencyTabulator</code> and checks if it is a duplicate; if it is not, adds it to the list.
     * <p>This method runs in <b>linear time O(n)</b> because it calls <code>isUnique</code>.</p>
     *
     * @param file The <code>File</code> used to instantiate the new tabulator.
     */
    // O(n) see JavaDocs
    public static void add(File file) {
        if (file != null) {
            FileFrequencyTabulator ft = new FileFrequencyTabulator(file, FREQUENCY_TABLE);
            if (isUnique(ft)) {
                tabulators.add(ft);
                Runner.log(Strings.PARSER_JOB_LIST_ADD_SUCCESS_FILE.get());
            }
        }
    }

    /**
     * Executes all stored <code>FrequencyTabulator</code>s simultaneously; returns a sorted <code>List</code> of <code>Map.Entry&lt;K,V&gt;</code> objects.
     * <p>This method runs in <b>linear time O(n)</b>.</p>
     *
     * @return A list of all words contained in any parsed texts, <b>sorted by frequency in descending order</b>. The list is made up of <code>Map.Entry&lt;K,V&gt;</code> objects where:<br>&emsp;K == <code>String</code> word<br>&emsp;V == <code>Integer</code> number of occurrences
     */
    // O(n) see JavaDocs - for loop on list, and the comparator also runs in linear time
    public static List<Map.Entry<String, Integer>> execute() {
        List<Map.Entry<String, Integer>> list = null;
        if (tabulators.isEmpty()) {
            Runner.log(Strings.PARSER_JOB_LIST_EMPTY_NO_WORDS.get());
        } else {
            ExecutorService executor = Executors.newCachedThreadPool();
            for (FrequencyTabulator<?> ft : tabulators) {
                executor.execute(ft);
            }
            executor.shutdown();
            while (true) {
                if (executor.isTerminated()) break;
            }
            if (!FREQUENCY_TABLE.isEmpty()) {
                list = new LinkedList<>(FREQUENCY_TABLE.entrySet());
                list.sort(Map.Entry.comparingByValue()); // O(n)
                Collections.reverse(list); // O(1) with a doubly-linked list
                FREQUENCY_TABLE.clear();
            }
        }
        return list;
    }

    private Tabulators() {
    }

}
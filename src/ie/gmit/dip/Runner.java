package ie.gmit.dip;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * <p>A UI that calls other classes' API methods. Methods can add log entries as a response, which are displayed to users.</p>
 *
 * @version 1.0
 * @since 1.8
 */
public final class Runner {
    private static boolean isQuitting = false;
    private static final List<String> LOG = new ArrayList<>();

    /**
     * <p>Prints a numbered list of <code>FrequencyTabulator</code>s currently held by the <code>Tabulators</code> class, unless there are none.</p>
     * <p>This method runs in linear time O(n) (because it calls on <code>Tabulators.list</code>.)</p>
     */
    private static void printTabulators() {
        String tabulators = Tabulators.list();
        if (!tabulators.equals("")) {
            System.out.println(Strings.PARSER_JOB_LIST_LABEL.get());
            System.out.println(tabulators);
        }
    }

    /**
     * <p>Prints any events logged since the last time this method was called and clears the log, unless the log is already empty.</p>
     * <p>This method runs in linear time O(n).</p>
     */
    private static void printLog() {
        if (!LOG.isEmpty()) {
            for (String event : LOG) {
                System.out.println(event + "\n");
            }
            LOG.clear();
        }
    }

    /**
     * <p>Prompts the user to enter a <b>whole number</b> inside a <b>specific range</b>. Checks if the input is valid and, if so, returns it as an <i>Integer</i>. Repeats until the user makes a valid choice without an option to cancel.</p>
     * <p>In terms of time complexity, this method contains blocking user input, so it takes as long as it takes. (Constant time O(1) otherwise.)</p>
     *
     * @param min     The smallest accepted value.
     * @param max     The largest accepted value.
     * @param message A clarifying message to the user displayed above the input prompt. Can be omitted by passing <code>null</code>.
     * @return The value input by the user.
     */
    private static int getRangedInt(int min, int max, String message) {
        int input = 0;
        while (input < min || input > max) {
            if (message != null) System.out.print(message);
            Scanner console = new Scanner(System.in);
            System.out.printf(Strings.IO_INPUT_NUMBER.get(), min, max);
            System.out.print(Strings.IO_INPUT_CURSOR.get());
            try {
                input = Integer.parseInt(console.nextLine());
            } catch (Exception ignored) {
            }
        }
        return input;
    }

    /**
     * <p>Displays <b>hard-coded menu options</b> and prompts the user to choose one option by calling <code>getRangedInt</code>.</p>
     * <p>In terms of time complexity, this method by itself runs in constant time O(1), however, it is dependent on user input and, depending on the user's input, calls other methods with various time complexities. Please see their respective comments for details.</p>
     */
    private static void navigateMenu() {
        System.out.printf(Strings.MENU_TOP_LEVEL.get(),
                Tabulators.getMinWordLength(),
                WordCloudRenderer.getMaxWords(),
                WordCloudRenderer.getImageSize()[0],
                WordCloudRenderer.getImageSize()[1]);
        switch (getRangedInt(1, 11, null)) {
            case 1: // Add file
                System.out.println(Strings.IO_INPUT_FILEPATH.get());
                Tabulators.add(new FileFromConsole().result());
                break;
            case 2: // Add URL
                System.out.println(Strings.IO_INPUT_URL.get());
                Tabulators.add(new URLFromConsole().result());
                break;
            case 3: // Empty source list
                Tabulators.clear();
                break;
            case 4: // Re-import stop words
                Tabulators.bufferStopWords();
                break;
            case 5: // Set minimum word length
                Tabulators.setMinWordLength(getRangedInt(1, 100, Strings.IO_INPUT_MIN_WORD_LENGTH.get()));
                break;
            case 6: // Set max number of words in cloud
                WordCloudRenderer.setMaxWords(getRangedInt(1, 100, Strings.IO_INPUT_MAX_WORDS.get()));
                break;
            case 7: // Set image size
                WordCloudRenderer.setImageSize(new int[]{
                        getRangedInt(300, 3000, Strings.IO_INPUT_IMAGE_WIDTH.get()),
                        getRangedInt(300, 3000, Strings.IO_INPUT_IMAGE_HEIGHT.get())
                });
                break;
            case 8: // Set output directory
                System.out.println(Strings.IO_INPUT_IMAGE_PATH.get());
                WordCloudIO.setImageDir(new DirFromConsole().result());
                break;
            case 9: // Set output file name
                System.out.println(Strings.IO_INPUT_IMAGE_FILE_NAME.get());
                WordCloudIO.setImageFileName(new NewFileFromConsole().result());
                break;
            case 10: // Render word cloud
                WordCloudRenderer.process(Tabulators.execute());
                break;
            case 11: // Quit
                isQuitting = true;
        }
    }

    /**
     * <p>Clears the console (to keep the menu aligned with the top of the screen/window).</p>
     * <p>Source: <a href="https://www.delftstack.com/howto/java/java-clear-console/" target="_new">https://www.delftstack.com/howto/java/java-clear-console/</a></p>
     */
    // No guarantees on time complexity, but it should be around O(1).
    private static void clearConsole() {
        try {
            String operatingSystem = System.getProperty("os.name");
            if (operatingSystem.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            }
        } catch (Exception ignored) {
        } // If this doesn't work, the app will still function, it will just be ugly.
    }

    /**
     * <p>Adds an event to the log.</p>
     * <p>This method runs in constant time O(1).</p>
     *
     * @param event A <code>String</code> which cannot be <code>null</code>.
     */
    // O(1) add to an ArrayList
    public static void log(String event) {
        if (event != null) LOG.add(event);
    }

    /**
     * On start up, this method calls the <code>Tabulators</code> API method to buffer the list of stop words; it then continuously displays relevant information to the user and enables them to make method calls via the menu.
     *
     * @param args are ignored.
     */
    // O(n) because that's the slowest in any method in this package
    public static void main(String[] args) {
        Tabulators.bufferStopWords();
        do {
            clearConsole();

            // App header
            System.out.println(Strings.APP_HEADER.get());

            // Output path for word cloud renderer
            System.out.println(Strings.MENU_OUTPUT_PATH_LABEL.get());
            System.out.println(WordCloudIO.getImageFile().toPath() + "\n");

            printTabulators();

            printLog();

            navigateMenu();
        } while (!isQuitting);
        System.out.println(Strings.RUNNER_SHUTDOWN.get());
    }

    private Runner() {
    }
}
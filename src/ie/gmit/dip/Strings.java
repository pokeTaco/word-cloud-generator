package ie.gmit.dip;

/**
 * An enumeration that stores any Strings used by the application as constants to keep the code clean and the text uniform.
 */
public enum Strings {
    APP_HEADER(hlBlue("***************************************************") + "\n" +
            hlBlue("* GMIT - Dept. Computer Science & Applied Physics *") + "\n" +
            hlBlue("*                                                 *") + "\n" +
            hlBlue("*            Word Cloud Generator V1.0            *") + "\n" +
            hlBlue("*     H.Dip in Science (Software Development)     *") + "\n" +
            hlBlue("*                                                 *") + "\n" +
            hlBlue("***************************************************") + "\n"),
    RUNNER_SHUTDOWN("Shutting down!"),
    IO_WRITING_IMAGE_FILE_EXISTS_OVERWRITE(cYellow("Warning: A file of that name already exists in the same directory.\nOverwrite the existing file? Type \"y\" or \"yes\". Press Enter to cancel.")),
    IO_WRITING_IMAGE_SUCCESS(cGreen("Image written to output path.")),
    IO_WRITING_IMAGE_ERROR(cRed("Error writing image to output path.")),
    IO_INPUT_NUMBER("\nPlease enter a number from %s to %s.%n"),
    IO_INPUT_CURSOR(" >> "),
    IO_INPUT_IMAGE_PATH("\nPlease enter a valid output directory with writing permissions. (Press Enter to cancel.)"),
    IO_INPUT_IMAGE_FILE_NAME("\nPlease enter a valid file name without extension. (Press Enter to cancel.)"),
    IO_INPUT_FILEPATH("\nPlease enter the path of the text file to be parsed. (Press Enter to cancel.)"),
    IO_INPUT_IMAGE_HEIGHT("\n(Image height)"),
    IO_INPUT_IMAGE_WIDTH("\n(Image width)"),
    IO_INPUT_MIN_WORD_LENGTH("\n(Minimum word length)"),
    IO_INPUT_MAX_WORDS("\n(Maximum number of words)"),
    IO_INPUT_URL("\nPlease enter a valid existing URL. (Press Enter to cancel.)"),
    IO_INPUT_URL_PLEASE_WAIT("Contacting %s...%n"),
    IO_VALIDATION_FAILED("Invalid input."),
    IO_TRY_AGAIN("Please try again or press Enter to return to the main menu."),
    MENU_OUTPUT_PATH_LABEL(cGreen("Output path: ")),
    MENU_TOP_LEVEL(
            "  " + cYellow(" MAIN MENU ") + "\n\n  1) Add local file\n" + "  2) Add URL\n"
                    + "  3) Empty file/URL list\n\n  4) Re-import stop words\n\n  5) Change min. word length (%s)\n"
                    + "  6) Change max. number of words (%s)\n  7) Change image size (%s x %s)\n\n  8) Change output directory\n"
                    + "  9) Change image file name\n  10) Render word cloud\n\n  11) Quit%n"
    ),
    PARSER_ERROR_READING_SOURCE(cRed("Error reading source.")),
    PARSER_JOB_LIST_ADD_SUCCESS_FILE(cGreen("File added to parser list.")),
    PARSER_JOB_LIST_ADD_SUCCESS_URL(cGreen("URL added to parser list.")),
    PARSER_JOB_LIST_EMPTY_NO_WORDS(cYellow("Please add at least one file or URL before rendering.")),
    PARSER_JOB_LIST_LABEL(cGreen("\nFiles/URLs to be parsed:")),
    PARSER_JOB_LIST_ERROR_DUPE(cRed("Error: File/URL already in list.")),
    PARSER_JOB_LIST_RESET(cGreen("The list of files/URLs to be parsed has been emptied.")),
    PARSER_PATTERN_HTML("(<.*?>|&lt;.+&gt;|&\\w+;)"),
    PARSER_PATTERN_CONTRACTIONS("((n't|'s|'d|'ve|'re|'ll|'m)\\b)"),
    PARSER_PATTERN_ARTEFACTS("([\\d\\W_]+)"),
    PARSER_STOP_WORDS_IMPORT_ERROR(cRed("Warning: Stop words could not be imported from ignorewords.txt.")),
    PARSER_STOP_WORDS_IMPORT_PATH("./ignorewords.txt"),
    PARSER_STOP_WORDS_IMPORT_SUCCESS(cGreen("Stop words successfully imported from ignorewords.txt.")),
    RENDER_WORD_LIST_EMPTY(cRed("Error: No matching words were found. Cannot render image.")),
    VAR_SET_IMAGE_SIZE(cGreen("Output image size set.")),
    VAR_SET_MIN_WORD_LENGTH(cGreen("Minimum word length set.")),
    VAR_SET_MAX_WORDS(cGreen("Maximum number of words set."));


    private final String string;

    private static String cRed(String s) {
        return "\033[0;31m" + s + "\033[0m";
    }

    private static String cGreen(String s) {
        return "\033[0;32m" + s + "\033[0m";
    }

    private static String cYellow(String s) {
        return "\033[0;33m" + s + "\033[0m";
    }

    private static String hlBlue(String s) {
        return "\033[0;97m" + "" + "\033[44m" + s + "\033[0m";
    }

    Strings(String string) {
        this.string = string;
    }

    // O(1) simple getter
    public String get() {
        return this.string;
    }
}
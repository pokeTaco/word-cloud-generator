package ie.gmit.dip;

import java.util.Scanner;

/**
 * <p>A class that takes in user input as a <code>String</code> on construction, safely interprets it as a <code>V</code> <i>verifiable type</i> and verifies that the resulting object meets a set of <i>custom requirements</i>.</p>
 *
 * @param <V> The verifiable type.
 * @version 1.0
 * @since 1.8
 */
public abstract class ConsoleValidator<V> implements Validation<String, V> {
    private V result = null;
    private boolean isValid;

    // O(1) simple getter/setter
    public V result() {
        return result; // The instance variable is set to null if the requirements aren't met, so there is no need to check here.
    }

    public abstract V recast(String input);

    /**
     * <p>Takes user input from the console and returns it as a <code>String</code>. Straight quotation marks are removed to avoid parsing issues with path names.</p>
     *
     * @return The user's input as a <code>String</code>.
     */
    // O(n) due to replaceAll
    private String readLine() {
        System.out.print(Strings.IO_INPUT_CURSOR.get());
        Scanner console = new Scanner(System.in);
        return console.nextLine().replaceAll("\"", "");
    }

    /**
     * <p>Takes user input as a <code>String</code>, interprets it as a <code>V</code> type, verifies that it meets a set of <i>custom requirements</i>, and stores the result in an instance variable. Loops on invalid input, can be cancelled by submitting an empty line.</p>
     */
    // O(?) Time complexity not guaranteed due to call of abstract method recast
    public ConsoleValidator() {
        while (!isValid) {
            String input = readLine();
            if (!input.equals("")) {
                V verifiable = recast(input);
                isValid = validate(verifiable);
                if (isValid) {
                    this.result = verifiable;
                } else {
                    System.out.println(Strings.IO_VALIDATION_FAILED.get());
                    System.out.println(Strings.IO_TRY_AGAIN.get());
                }
            } else break;
        }
    }
}
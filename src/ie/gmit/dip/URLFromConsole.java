package ie.gmit.dip;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <p>A class that takes in user input as a <code>String</code> on construction, safely interprets it as a <code>URL</code> and verifies that a GET request to the <code>URL</code> will not fail.</p>
 *
 * @version 1.0
 * @since 1.8
 */
public class URLFromConsole extends ConsoleValidator<URL> {
    /**
     * <p>Safely interprets a <code>String</code> as a <code>URL</code> and returns the resulting object.</p>
     *
     * @param input A string to be interpreted as a <code>URL</code>; if it does not start with "http://" or "https://", adds "http://" to the beginning.
     * @return A <code>URL</code> object based on the <code>input</code>.
     */
    // O(n) due to toLowerCase
    public URL recast(String input) {
        if (!input.startsWith("http://") && !input.startsWith("https://")) {
            input = "http://" + input.toLowerCase();
        }
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * Verifies that a GET request to the <code>URL</code> will not fail; this requirement is met if a HEAD request yields an OK response within a reasonable time.
     *
     * @param url The <code>URL</code> in question.
     * @return True, if an OK response is received, otherwise false.
     */
    // O(1) disregarding IO operations, no guarantees about those
    public boolean validate(URL url) {
        if (url != null) {
            System.out.printf(Strings.IO_INPUT_URL_PLEASE_WAIT.get(), url.getHost());
            try {
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestMethod("HEAD");
                huc.setConnectTimeout(5000);
                return (huc.getResponseCode() == HttpURLConnection.HTTP_OK);
            } catch (IOException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
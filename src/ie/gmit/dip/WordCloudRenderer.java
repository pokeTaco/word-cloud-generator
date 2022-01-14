package ie.gmit.dip;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * <p>A class that renders a set number of words from a text as a cloud that visualises the relative frequency of occurrence for each word.</p>
 *
 * @version 1.0
 * @since 1.8
 */
public final class WordCloudRenderer {
    private static final Random random = new Random();
    private static int maxWords = 12;
    private static int[] imageSize = new int[]{600, 300};

    /**
     * <p>Returns the size of the output image as an integer array with the image width at index 0 and the image height at index 1.</p>
     *
     * @return An <code>int[]{width,height}</code>.
     */
    // O(1) simple getter/setter
    public static int[] getImageSize() {
        return imageSize;
    }

    /**
     * <p>Assigns the size of the output image an integer array with the image width at index 0 and the image height at index 1.</p>
     *
     * @param newSize An <code>int[]{width,height}</code>.
     */
    // O(1) simple getter/setter
    public static void setImageSize(int[] newSize) {
        imageSize = newSize;
        Runner.log(Strings.VAR_SET_IMAGE_SIZE.get());
    }

    /**
     * <p>Returns the maximum number of words that are rendered per image. Note that the cloud may contain fewer words if the <code>List</code> that is passed to the <code>process</code> method does not contain enough elements to reach this limit.</p>
     *
     * @return The maximum number of words rendered per image.
     */
    // O(1) simple getter/setter
    public static int getMaxWords() {
        return maxWords;
    }

    /**
     * <p>Sets the maximum number of words that are rendered per image. Note that the cloud may contain fewer words if the <code>List</code> that is passed to the <code>process</code> method does not contain enough elements to reach this limit.</p>
     *
     * @param maxWords The maximum number of words rendered per image.
     */
    // O(1) simple getter/setter
    public static void setMaxWords(int maxWords) {
        WordCloudRenderer.maxWords = maxWords;
        Runner.log(Strings.VAR_SET_MAX_WORDS.get());
    }

    /**
     * <p>Returns the appropriate font size to be used for a word <b>based on the image dimensions and several other parameters</b> which help strike a balance between an accurate visualisation of the relative frequency of each word and a legible, aesthetically pleasing result.</p>
     * <p>This method runs in <b>constant time O(1)</b>.</p>
     *
     * @param words       The total number of words rendered.
     * @param max         The number of times the most frequent word occurs in the source text.
     * @param occurrences The number of times this word occurs in the source text.
     * @return The font size as a <code>Float</code>.
     */
    // O(1) no loop, just computation
    private static float getFontSize(int words, int max, int occurrences) {
        return (float) (getImageSize()[1] / 25 + 200 / words + (occurrences * (getImageSize()[0] / 20) / max));
    }

    /**
     * Clamps the parameter to fit the range 0..255 for the purpose of using it as a value for an ARGB channel.
     * <p>This method runs in <b>constant time O(1)</b>.</p>
     *
     * @param channel An <code>Integer</code>.
     * @return 0 for a parameter &lt;0, or 255 for a parameter &gt;255, or else the parameter.
     */
    // O(1) no loop, just computation
    private static int clamp(int channel) {
        return Math.max(0, Math.min(channel, 255));
    }

    /**
     * <p>Takes in a <code>List</code> of <code>Map.Entry&lt;String, Integer&gt;</code> objects, which contain <b>words</b> as keys and <b>their occurrence counts in a given text</b> as values, and renders a set number of said words as a cloud that visualises the relative frequency of occurrence for each word. The word cloud is then written to the disk under the name stored in the class variable.</p>
     * <p>This method runs in <b>linear time O(n)</b> with n being the number of actually rendered words.</p>
     *
     * @param list A <code>List</code> of <code>Map.Entry&lt;String, Integer&gt;</code> objects that cannot be null.
     */
    // O(n) see JavaDocs it's just a for loop
    public static void process(List<Map.Entry<String, Integer>> list) {
        boolean canProceed = true;
        if (WordCloudIO.imageFileExists()) canProceed = WordCloudIO.willOverwrite();

        if (list != null && canProceed) {
            list = list.subList(0, Math.min(list.size(), maxWords));
            BufferedImage image = new BufferedImage(imageSize[0], imageSize[1], BufferedImage.TYPE_4BYTE_ABGR);

            Graphics2D g2 = image.createGraphics();
            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );
            g2.setRenderingHints(rh);

            // Colours are dynamically computed but not randomized because a combination of random colours is unlikely to be aesthetically pleasant. Words appear more red and less blue the more frequent they are, and the green channel alternates between a value of 70 and a value of 130. Words are rendered with increasing opacity from 180 for the least frequent word to 255 for the most frequent word.
            int red = clamp(255 - 10 * list.size());
            int blue = 255;
            int green = 130;
            int alpha = 180;

            g2.setColor(new Color(red, green, blue, alpha));

            // This method cycles through a selection of 10 font styles that were selected for good readability. Again, randomization is possible but not desirable in this context because there is a high chance of illegible results.
            int fontCounter = 0;
            Fonts[] fonts = Fonts.values();

            for (int i = list.size() - 1; i >= 0; i--) {
                String word = list.get(i).getKey();
                int occurrences = list.get(i).getValue();

                Font font = fonts[fontCounter].get().deriveFont(getFontSize(list.size(), list.get(0).getValue(), occurrences));
                g2.setFont(font);
                fontCounter++;
                if (fontCounter > 9) fontCounter = 0;

                // Roughly calculates the dimensions of the rendered word and assigns a safe position avoid truncations.
                int wordHeight = font.getSize();
                int wordWidth = font.getSize() * word.length();
                int maxX = Math.max((image.getWidth() - wordWidth), 1);
                int maxY = Math.max((image.getHeight() - wordHeight), 1);
                int posX = random.nextInt(maxX);
                int posY = Math.max(random.nextInt(maxY), wordHeight);

                g2.drawString(word, posX, posY);

                red = clamp(red + 10);
                blue = clamp(blue - 255 / list.size());
                green = Math.abs(200 - green);
                alpha = clamp(alpha + 75 / list.size());

                g2.setColor(new Color(red, green, blue, alpha));
            }
            g2.dispose();
            WordCloudIO.writeImageFile(image);
        } else if (list == null){
            Runner.log(Strings.RENDER_WORD_LIST_EMPTY.get());
        }
    }

    private WordCloudRenderer() {
    }

    /**
     * A selection of fonts to be used by the <code>WordCloudRenderer</code>. The size is 0 because it is computed at runtime for each word.
     */
    private enum Fonts {
        F0(new Font(Font.SANS_SERIF, Font.PLAIN, 0)),
        F1(new Font(Font.SERIF, Font.BOLD, 0)),
        F2(new Font(Font.SANS_SERIF, Font.ITALIC, 0)),
        F3(new Font(Font.SERIF, Font.PLAIN, 0)),
        F4(new Font(Font.MONOSPACED, Font.BOLD, 0)),
        F5(new Font(Font.SANS_SERIF, Font.BOLD, 0)),
        F6(new Font(Font.SERIF, Font.ITALIC, 0)),
        F7(new Font(Font.MONOSPACED, Font.BOLD | Font.ITALIC, 0)),
        F8(new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 0)),
        F9(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 0));

        private final Font font;

        Fonts(Font font) {
            this.font = font;
        }

        // O(1) no loops here
        private Font get() {
            return this.font;
        }
    }
}
package mormon.extractors;

/**
 * ExtractorHelper
 *
 * Helper class to ensure that all extractors gather the same type of text. Helps remove code copy-pasta.
 */
public class ExtractorHelper {

    /**
     * Returns string in the proper format to be included in a text.
     *
     * @param str -
     * @return -
     */
    public static String convertToProperFormat(String str) {
        return str.replaceAll("\\p{Punct}", "").toLowerCase();
    }
}

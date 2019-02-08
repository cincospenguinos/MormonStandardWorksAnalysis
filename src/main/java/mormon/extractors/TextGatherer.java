package mormon.extractors;

import mormon.extractors.BookOfMormonExtractor;
import mormon.extractors.LateWarExtractor;
import mormon.model.AnnotatedText;

import java.io.File;
import java.util.*;

/**
 * TextGatherer class
 *
 * Extracts all of the texts and annotates them according to what is included in each file.
 */
public class TextGatherer {

    private static final String BOOK_OF_MORMON_FILENAME = "book_of_mormon.txt";
    private static final String LATE_WAR_FILENAME = "the_late_war.txt";

    private static final String BOOK_OF_MORMON = "The Book of Mormon";
    private static final String LATE_WAR = "The Late War";

    private File[] _files;
    private List<AnnotatedText> _annotatedTexts; // Maps name to text

    public TextGatherer(File[] files) {
        _files = files;
        _annotatedTexts = new ArrayList<>();
    }

    /**
     * Gathers the various texts and gets them annotated.
     */
    public void gatherTexts() {
        for (File f : _files) {
            AnnotatedText text = getExtractedText(f);
            text.annotate();
            _annotatedTexts.add(text);
        }
    }

    /**
     * Returns all of the mormon texts that have been gathered.
     *
     * @return -
     */
    public List<AnnotatedText> getMormonTexts() {
        List<AnnotatedText> mormonTexts = new ArrayList<>();

        for (AnnotatedText text : _annotatedTexts) {
            if (text.isMormon()) {
                mormonTexts.add(text);
            }
        }

        return mormonTexts;
    }

    /**
     * Returns all of the non-mormon texts that have been gathered.
     *
     * @return -
     */
    public List<AnnotatedText> getNonMormonTexts() {
        List<AnnotatedText> nonMormonTexts = new ArrayList<>();

        for (AnnotatedText text : _annotatedTexts) {
            if (!text.isMormon()) {
                nonMormonTexts.add(text);
            }
        }

        return nonMormonTexts;
    }

    public List<AnnotatedText> getAllTexts() {
        return _annotatedTexts;
    }

    /*--PRIVATE METHODS--*/

    /**
     * Helper method. Gets the extracted text from the file and places it where it is needed.
     *
     * @param file -
     * @return text -
     */
    private AnnotatedText getExtractedText(File file) {
        AnnotatedText text;
        if (file.getName().equals(BOOK_OF_MORMON_FILENAME)) {
            text = new BookOfMormonExtractor().extractText(file);
        } else if (file.getName().equals(LATE_WAR_FILENAME)) {
            text = new LateWarExtractor().extractText(file);
        } else {
            throw new RuntimeException("No extractor found for the file provided!");
        }

        return text;
    }

}

package mormon;

import mormon.extractors.BookOfMormonExtractor;
import mormon.extractors.LateWarExtractor;

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
    private Map<String, AnnotatedText> _annotatedTexts; // Maps name to text

    public TextGatherer(File[] files) {
        _files = files;
        _annotatedTexts = new HashMap<String, AnnotatedText>();
    }

    /**
     * Gathers the various texts and gets them annotated.
     */
    public void gatherTexts() {
        for (File f : _files) {
            getExtractedText(f);
        }
    }

    /**
     * Helper method. Gets the extracted text from the file and places it where it is needed.
     *
     * @param file
     */
    private void getExtractedText(File file) {
        if (file.getName().equals(BOOK_OF_MORMON_FILENAME)) {
            AnnotatedText bookOfMormon = new BookOfMormonExtractor().extractText(file);
            _annotatedTexts.put(BOOK_OF_MORMON, bookOfMormon);
        } else if (file.getName().equals(LATE_WAR_FILENAME)) {
            AnnotatedText lateWar = new LateWarExtractor().extractText(file);
            _annotatedTexts.put(LATE_WAR, lateWar);
        } else {
            throw new RuntimeException("No extractor found for the file provided!");
        }
    }
}

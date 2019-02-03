package mormon.extractors;

import mormon.model.AnnotatedText;

import java.io.File;

/**
 * TextExtractor
 *
 * Interface to be implemented by any object that extracts the text of a file. This was done to make
 * adding new texts to be extracted much easier to handle.
 */
public interface TextExtractor {

    public static final String META_INFO = "META_INFO";

    /**
     * Extracts the text found in the file provided and returns an AnnotatedText object of that text.
     *
     * @param textFile - The text file to be extracted
     * @return AnnotatedText of that text
     */
    public AnnotatedText extractText(File textFile);
}

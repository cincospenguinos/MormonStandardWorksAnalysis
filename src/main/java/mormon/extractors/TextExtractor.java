package mormon.extractors;

import mormon.AnnotatedText;

import java.io.File;

/**
 * TextExtractor
 *
 * Interface to be implemented by any object that extracts the text of a file.
 */
public interface TextExtractor {
    public AnnotatedText extractText(File textFile);
}

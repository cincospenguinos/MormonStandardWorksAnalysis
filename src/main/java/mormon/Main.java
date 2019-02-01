package mormon;

import java.io.File;

/**
 * Main class. Entry point for the application.
 */
public class Main {

    private static final String TEXTS_DIR = "src/main/resources/texts/";

    public static void main(String[] args) {
        TextExtractor extractor = new TextExtractor(new File(TEXTS_DIR).listFiles());
        extractor.extractTexts();
    }
}

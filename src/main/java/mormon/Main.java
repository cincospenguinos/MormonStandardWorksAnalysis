package mormon;

import mormon.analysis.AnnotatedTextAnalyzer;
import mormon.model.AnnotatedText;
import mormon.model.TextGatherer;
import mormon.model.TextLevel;

import java.io.File;

/**
 * Main class. Entry point for the application.
 */
public class Main {

    private static final String TEXTS_DIR = "src/main/resources/texts/";

    public static void main(String[] args) {
        TextGatherer gatherer = new TextGatherer(new File(TEXTS_DIR).listFiles());
        gatherer.gatherTexts();

        for (AnnotatedText mormonText : gatherer.getMormonTexts()) {
            for (AnnotatedText nonMormonText : gatherer.getNonMormonTexts()) {
                AnnotatedTextAnalyzer analyzer = new AnnotatedTextAnalyzer(mormonText, nonMormonText);
                analyzer.performSimilarityAnalysis(TextLevel.BOOK, TextLevel.BOOK);
            }
        }
    }
}

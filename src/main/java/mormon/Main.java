package mormon;

import mormon.report.AnalysisReport;
import mormon.analysis.SimpleNGramAnalysis;
import mormon.model.AnnotatedText;
import mormon.extractors.TextGatherer;

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
                SimpleNGramAnalysis simpleAnalysis = new SimpleNGramAnalysis(mormonText, nonMormonText);
                simpleAnalysis.performAnalysis();
                AnalysisReport report = simpleAnalysis.generateReport();
                System.out.println(report.toJsonStrings());
            }
        }
    }
}

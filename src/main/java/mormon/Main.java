package mormon;

import mormon.report.AnalysisReport;
import mormon.analysis.SimpleNGramAnalysis;
import mormon.model.AnnotatedText;
import mormon.extractors.TextGatherer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Main class. Entry point for the application.
 */
public class Main {

    private static final String TEXTS_DIR = "src/main/resources/texts/";
    private static final String OUTPUT_DIR = "output";

    public static void main(String[] args) {
        TextGatherer gatherer = new TextGatherer(new File(TEXTS_DIR).listFiles());
        gatherer.gatherTexts();

        for (AnnotatedText mormonText : gatherer.getMormonTexts()) {
            for (AnnotatedText nonMormonText : gatherer.getNonMormonTexts()) {
                SimpleNGramAnalysis simpleAnalysis = new SimpleNGramAnalysis(mormonText, nonMormonText);
                simpleAnalysis.performAnalysis();

                AnalysisReport report = simpleAnalysis.generateReport();

                String reportFileNameChunk = mormonText.getName().replaceAll("\\s+", "_") + "_" + nonMormonText.getName().replaceAll("\\s+", "_");
                outputFilesFor(reportFileNameChunk, report.toJsonStrings());
            }
        }
    }

    private static void outputFilesFor(String filenameChunk, Map<String, String> jsonReports) {
        ensureOutputDirCreated();

        for (Map.Entry<String, String> e : jsonReports.entrySet()) {
            StringBuilder fullFileName = new StringBuilder();
            fullFileName.append(filenameChunk);
            fullFileName.append("_");
            fullFileName.append(e.getKey());
            fullFileName.append(".json");

            try {
                PrintWriter writer = new PrintWriter(new File(OUTPUT_DIR + "/" + fullFileName.toString()));
                writer.print(e.getValue());
                writer.flush();
                writer.close();
            } catch (FileNotFoundException e1) {
                throw new RuntimeException("Could not find the file!");
            }
        }
    }

    private static void ensureOutputDirCreated() {
        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists() || !outputDir.isDirectory()) {
            if (!outputDir.mkdir()) {
                throw new RuntimeException("Could not make output dir!");
            }
        }
    }
}

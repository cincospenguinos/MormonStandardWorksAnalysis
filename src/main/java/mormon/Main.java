package mormon;

import mormon.report.AnalysisReport;
import mormon.analysis.SimpleNGramAnalysis;
import mormon.model.AnnotatedText;
import mormon.extractors.TextGatherer;
import mormon.version.VersionInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main class. Entry point for the application.
 */
public class Main {

    private static final String CURRENT_VERSION = "0.1.0";
    private static final String VERSION_INFO_FILE_NAME = "analysis_version_info.json";

    private static final String TEXTS_DIR = "src/main/resources/texts/";
    private static final String OUTPUT_DIR = "output";

    public static void main(String[] args) {
        TextGatherer gatherer = new TextGatherer(new File(TEXTS_DIR).listFiles());
        gatherer.gatherTexts();

        createVersionReportFile(gatherer);

        for (AnnotatedText mormonText : gatherer.getMormonTexts()) {
            for (AnnotatedText nonMormonText : gatherer.getNonMormonTexts()) {
                SimpleNGramAnalysis simpleAnalysis = new SimpleNGramAnalysis(mormonText, nonMormonText);
                simpleAnalysis.performAnalysis();

                AnalysisReport report = simpleAnalysis.generateReport();

                String reportFileNameChunk = reportFileNameChunk(mormonText, nonMormonText);
                outputFilesFor(reportFileNameChunk, report.toJsonStrings());
            }
        }
    }

    /**
     * Helper method. Returns the file name chunk for two texts.
     *
     * @param mormonText -
     * @param nonMormonText -
     * @return -
     */
    private static String reportFileNameChunk(AnnotatedText mormonText, AnnotatedText nonMormonText) {
        String mormonTextName = mormonText.getName().replaceAll("\\s+", "_");
        String nonMormonTextName = nonMormonText.getName().replaceAll("\\s+", "_");

        return mormonTextName + "_" + nonMormonTextName;
    }

    /**
     * Helper method. Outputs report file detailing information about the current version of this application.
     *
     * @param gatherer -
     */
    private static void createVersionReportFile(TextGatherer gatherer) {
        VersionInfo info = new VersionInfo();

        info.setVersion(CURRENT_VERSION);
        info.addMormonBooks(
                gatherer.getMormonTexts().stream()
                        .map(AnnotatedText::getName)
                        .collect(Collectors.toList()));
        info.addNonMormonBooks(
                gatherer.getNonMormonTexts().stream()
                        .map(AnnotatedText::getName)
                        .collect(Collectors.toList()));

        String json = info.toJson();
        writeJsonToFile(VERSION_INFO_FILE_NAME, json);
    }

    /**
     * Helper method. Creates output files given a filename chunk (the first part of the file name) and the set of all
     * the json reports we performed.
     *
     * @param filenameChunk -
     * @param jsonReports -
     */
    private static void outputFilesFor(String filenameChunk, Map<String, String> jsonReports) {
        ensureOutputDirCreated();

        for (Map.Entry<String, String> e : jsonReports.entrySet()) {
            String fullFileName = filenameChunk + "_" + e.getKey() + ".json";
            writeJsonToFile(fullFileName, e.getValue());
        }
    }

    /**
     * Ensures that the output directory is created to put stuff out into.
     */
    private static void ensureOutputDirCreated() {
        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists() || !outputDir.isDirectory()) {
            if (!outputDir.mkdir()) {
                throw new RuntimeException("Could not make output dir!");
            }
        }
    }

    /**
     * Helper method. Does what it says on the tin.
     * @param filename -
     * @param json -
     */
    private static void writeJsonToFile(String filename, String json) {
        String fullFileName = OUTPUT_DIR + "/" + filename;
        try {
            PrintWriter printWriter = new PrintWriter(new File(fullFileName));
            printWriter.print(json);
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not write version report file!");
        }
    }
}

package mormon.analysis;

import com.google.gson.Gson;
import mormon.model.AnnotatedText;
import mormon.report.AnalysisReport;

import java.util.HashMap;
import java.util.Map;

/**
 * MetaDataAnalysis
 *
 * Generates metadata on a given text. Unlike other analyzers, this one analyzes a single text. This simply provides
 * information about a text.
 */
public class MetaDataAnalysis extends AnnotatedTextAnalyzer {

    private TextualMetaData metaData;

    public static final String META_DATA_NAME = "meta_data";

    public MetaDataAnalysis(AnnotatedText text) {
        this.setTextA(text);
        metaData = new TextualMetaData();
    }

    @Override
    public void performAnalysis() {
        AnnotatedText text = this.getTextA();

        metaData.name = text.getName();
        metaData.wordCount = text.wordCount();
    }

    @Override
    public AnalysisReport generateReport() {
        return () -> {
            Map<String, String> jsonStrings = new HashMap<>();
            Gson gson = new Gson();
            jsonStrings.put(META_DATA_NAME, gson.toJson(metaData));
            return jsonStrings;
        };
    }

    private class TextualMetaData {
        String name;
        long wordCount;
    }
}

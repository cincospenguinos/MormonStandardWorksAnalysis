package mormon.report;

import com.google.gson.Gson;
import mormon.model.AnnotatedText;
import mormon.model.NGram;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * SectionNGramReport
 *
 * Report on the number of NGrams per section in a couple of texts.
 */
public class SectionNGramReport implements AnalysisReport {

    private Map<String, Map<Integer, Integer>> nGramCountMap;

    public static final String SECTION_SIMILARITY_REPORT_NAME = "section_ngram_similarity";

    public SectionNGramReport() {
        nGramCountMap = new LinkedHashMap<>();
    }

    @Override
    public Map<String, String> toJsonStrings() {
        Map<String, String> jsonStrings = new HashMap<>();
        Gson gson = new Gson();
        jsonStrings.put(SECTION_SIMILARITY_REPORT_NAME, gson.toJson(nGramCountMap));
        return  jsonStrings;
    }

    /**
     * Adds a section to the internal collection.
     *
     * @param sectionName -
     * @param nGramCounts -
     */
    public void addSection(String sectionName, Map<Integer, Set<NGram>> nGramCounts) {
        Map<Integer, Integer> sectionMap = new HashMap<>();
        for(int n : AnnotatedText.N_GRAM_VALUES) {
            Set<NGram> set = nGramCounts.get(n);

            if (set == null) {
                sectionMap.put(n, 0);
            } else {
                sectionMap.put(n, set.size());
            }
        }
        nGramCountMap.put(sectionName, sectionMap);
    }
}

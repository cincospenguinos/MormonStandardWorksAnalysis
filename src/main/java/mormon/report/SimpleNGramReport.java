package mormon.report;

import com.google.gson.Gson;
import mormon.model.Pair;

import java.util.HashMap;
import java.util.Map;

public class SimpleNGramReport implements AnalysisReport {

    private Map<Integer, NGramCount> nGramCountMap;
    private Pair<String, String> textNames;

    public SimpleNGramReport(String textAName, String textBName) {
        nGramCountMap = new HashMap<>();
        textNames = new Pair<>(textAName, textBName);
    }

    @Override
    public Map<String, String> toJsonStrings() {
        Gson gson = new Gson();
        Map<String, String> jsonReport = new HashMap<>();
        String reportName = textNames.getFirst().replaceAll("\\s+", "_") + "_"
                + textNames.getSecond().replaceAll("\\s+", "_");
        jsonReport.put(reportName, gson.toJson(nGramCountMap));
        return jsonReport;
    }

    /**
     * Adds the counts of NGrams to this report, given the size of the NGram, how many appear in text A, and how
     * many appear in text B.
     *
     * @param nGramSize
     * @param countTextA
     * @param countTextB
     */
    public void addNGramCounts(int nGramSize, int countTextA, int countTextB, int totalSimilar) {
        if (!nGramCountMap.containsKey(nGramSize)) {
            NGramCount count = new NGramCount();
            count.nGramSize = nGramSize;
            nGramCountMap.put(nGramSize, count);
        }

        NGramCount count = nGramCountMap.get(nGramSize);
        count.countTextA = Math.addExact(count.countTextA, countTextA);
        count.countTextB = Math.addExact(count.countTextB, countTextB);
        count.countSimilar = Math.addExact(count.countSimilar, totalSimilar);
    }

    /**
     * Private POJO to help aid in the creation of the JSON reports.
     */
    private class NGramCount {
        int nGramSize;
        long countTextA;
        long countTextB;
        long countSimilar;
    }
}

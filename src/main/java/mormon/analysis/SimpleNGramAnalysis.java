package mormon.analysis;

import mormon.model.AnnotatedText;
import mormon.model.NGram;
import mormon.report.AnalysisReport;
import mormon.report.SimpleNGramReport;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleNGramAnalysis extends AnnotatedTextAnalyzer {

    private Map<NGram, Integer> nGramSimilarityCounts; // Maps NGrams to how many times they are similar between the two texts.

    // Maps the NGram size to the number of NGrams of that size in each text
    private Map<Integer, Integer> textANGramSize;
    private Map<Integer, Integer> textBNGramSize;

    public SimpleNGramAnalysis(AnnotatedText textA, AnnotatedText textB) {
        this.setTextA(textA);
        this.setTextB(textB);

        nGramSimilarityCounts = new HashMap<>();
        textANGramSize = new HashMap<>();
        textBNGramSize = new HashMap<>();
    }

    /**
     * Performs a simple NGram analysis observing the flat similarity between the two texts.
     */
    @Override
    public void performAnalysis() {
        Collection<NGram> nGramsTextA = getNGramsFrom(this.getTextA());
        Collection<NGram> nGramsTextB = getNGramsFrom(this.getTextB());

        for (final int n : AnnotatedText.N_GRAM_VALUES) {
            nGramsTextA.stream()
                .filter(nGram -> nGram.length() == n)
                .forEach(nGram -> {
                    if (nGramsTextB.contains(nGram)) {
                        if (nGramSimilarityCounts.containsKey(nGram)) {
                            nGramSimilarityCounts.put(nGram, nGramSimilarityCounts.get(nGram) + 1);
                        } else {
                            nGramSimilarityCounts.put(nGram, 1);
                        }
                    }
                });

            textANGramSize.put(n, nGramsTextA.size());
            textBNGramSize.put(n, nGramsTextB.size());
        }
    }

    /**
     * Generates a simple report of the similarity between the two texts.
     *
     * @return AnalysisReport
     */
    @Override
    public AnalysisReport generateReport() {
        SimpleNGramReport finalReport = new SimpleNGramReport(this.getTextA().getName(), this.getTextB().getName());

        for (int n : AnnotatedText.N_GRAM_VALUES) {
            nGramSimilarityCounts.entrySet().stream()
                .filter(e -> e.getKey().length() == n)
                .forEach(e -> {
                    int totalSimilar = (int) nGramSimilarityCounts.entrySet().stream()
                            .filter(e1 -> e1.getKey().length() == n).count();

                    finalReport.addNGramCounts(n, textANGramSize.get(n), textBNGramSize.get(n), totalSimilar);
                });
        }

        return finalReport;
    }

    /**
     * Helper method. Returns all of the NGrams from the AnnotatedText provided.
     *
     * @param text -
     * @return -
     */
    private Collection<NGram> getNGramsFrom(AnnotatedText text) {
        return text.getAllNGrams();
    }
}

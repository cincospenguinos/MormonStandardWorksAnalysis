package mormon.analysis;

import mormon.model.AnnotatedText;
import mormon.model.NGram;
import mormon.report.AnalysisReport;
import mormon.report.SimpleNGramReport;

import java.util.*;

/**
 * SimpleNGramAnalysis
 *
 * Does simple NGram analysis on the texts provided. Counts number of unique NGrams between the two texts.
 */
public class SimpleNGramAnalysis extends AnnotatedTextAnalyzer {

    // Maps NGram size to Set of NGrams that are similar to the two texts
    private Map<Integer, Set<NGram>> nGramSimilarityCounts;

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
                        if (!nGramSimilarityCounts.containsKey(n)) {
                            nGramSimilarityCounts.put(n, new HashSet<>());
                        }

                        nGramSimilarityCounts.get(n).add(nGram);
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
        SimpleNGramReport finalReport = new SimpleNGramReport();

        for (int n : AnnotatedText.N_GRAM_VALUES) {
            Set<NGram> nGramSet = nGramSimilarityCounts.get(n);
            finalReport.addNGramCounts(n, textANGramSize.get(n), textBNGramSize.get(n), nGramSet.size());
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

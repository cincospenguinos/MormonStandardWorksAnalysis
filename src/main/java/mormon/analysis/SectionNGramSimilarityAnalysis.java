package mormon.analysis;

import mormon.model.AnnotatedText;
import mormon.model.NGram;
import mormon.report.AnalysisReport;
import mormon.report.SectionNGramReport;

import java.util.*;

/**
 * SectionNGramSimilarityAnalysis
 *
 * Performs NGram similarity analysis on a section level. Basically the SimpleNGramAnalysis,
 * but on a section level.
 */
public class SectionNGramSimilarityAnalysis extends AnnotatedTextAnalyzer {

    private Map<String, Map<Integer, Set<NGram>>> sectionSimilaritySet;

    public SectionNGramSimilarityAnalysis(AnnotatedText textA, AnnotatedText textB) {
        this.setTextA(textA);
        this.setTextB(textB);

        sectionSimilaritySet = new HashMap<>();
    }

    @Override
    public void performAnalysis() {
        Collection<AnnotatedText> sections = this.getTextA().getSectionsOrChapters();
        Collection<NGram> textBNGrams = this.getTextB().getAllNGrams();

        for (AnnotatedText section : sections) {
            Map<Integer, Set<NGram>> sectionMapping = new HashMap<>();

            section.getAllNGrams().forEach(nGram -> {
                if (textBNGrams.contains(nGram)) {
                    if (!sectionMapping.containsKey(nGram.length())) {
                        sectionMapping.put(nGram.length(), new HashSet<>());
                    }

                    sectionMapping.get(nGram.length()).add(nGram);
                }
            });

            sectionSimilaritySet.put(section.getName(), sectionMapping);
        }
    }

    @Override
    public AnalysisReport generateReport() {
        SectionNGramReport report = new SectionNGramReport();

        for (Map.Entry<String, Map<Integer, Set<NGram>>> e : sectionSimilaritySet.entrySet()) {
            String sectionName = e.getKey();
            report.addSection(sectionName, e.getValue());
        }

        return report;
    }
}

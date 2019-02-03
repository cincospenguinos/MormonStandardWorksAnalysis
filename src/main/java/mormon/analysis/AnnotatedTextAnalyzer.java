package mormon.analysis;

import mormon.model.AnalysisReport;
import mormon.model.AnnotatedText;
import mormon.model.NGram;
import mormon.model.TextLevel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AnnotatedTextAnalyzer
 *
 * Performs analysis of the two annotated texts provided. Provides a variety of analyses for the
 * two texts.
 */
public abstract class AnnotatedTextAnalyzer {

    private AnnotatedText textA;
    private AnnotatedText textB;

    /**
     * Performs similarity analysis on the two texts associated with this analyzer at
     * the textual levels provided.
     *
     * @param levelA -
     * @param levelB -
     */
    public abstract void performAnalysis(TextLevel levelA, TextLevel levelB);

    /**
     * Returns an AnalysisReport of the type of analysis that the analyzer does. If the analysis is N-Gram similarity,
     * then this would return a report with that information.
     *
     * @return an AnalysisReport
     */
    public abstract AnalysisReport generateReport();

    public AnnotatedText getTextA() {
        return textA;
    }

    public void setTextA(AnnotatedText textA) {
        this.textA = textA;
    }

    public AnnotatedText getTextB() {
        return textB;
    }

    public void setTextB(AnnotatedText textB) {
        this.textB = textB;
    }
}

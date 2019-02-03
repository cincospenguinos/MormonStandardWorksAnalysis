package mormon.analysis;

import mormon.model.AnnotatedText;
import mormon.report.AnalysisReport;

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
     * Performs some form of analysis on the two texts provided. What this analysis is depends wholly upon the
     * implementation of the analyzer.
     */
    public abstract void performAnalysis();

    /**
     * Returns an AnalysisReport of the type of analysis that the analyzer does. This depends wholly upon the implementation
     * of the Analyzer.
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

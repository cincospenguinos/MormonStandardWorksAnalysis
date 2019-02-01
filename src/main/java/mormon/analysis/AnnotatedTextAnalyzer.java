package mormon.analysis;

import edu.stanford.nlp.simple.Document;
import mormon.model.AnnotatedText;
import mormon.model.TextType;

import java.util.List;

/**
 * AnnotatedTextAnalyzer
 *
 * Performs analysis of the two annotated texts provided. Provides a variety of analyses for the
 * two texts.
 */
public class AnnotatedTextAnalyzer {

    private AnnotatedText _textA;
    private AnnotatedText _textB;

    public AnnotatedTextAnalyzer(AnnotatedText textA, AnnotatedText textB) {
        _textA = textA;
        _textB = textB;
    }

    public void performSimilarityAnalysis(TextType levelA, TextType levelB) {
        throw new RuntimeException("Implement me!");
    }
}

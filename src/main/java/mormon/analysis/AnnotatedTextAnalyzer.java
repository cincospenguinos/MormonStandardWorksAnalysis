package mormon.analysis;

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
public class AnnotatedTextAnalyzer {

    private AnnotatedText _textA;
    private AnnotatedText _textB;

    public AnnotatedTextAnalyzer(AnnotatedText textA, AnnotatedText textB) {
        _textA = textA;
        _textB = textB;
    }

    /**
     * Performs similarity analysis on the two texts associated with this analyzer at
     * the textual levels provided.
     *
     * @param levelA -
     * @param levelB -
     */
    public void performSimilarityAnalysis(TextLevel levelA, TextLevel levelB) {
        List<NGram> textANgrams = _textA.getTextAtLevel(levelA).extractNGrams(1);
        List<NGram> textBNgrams = _textB.getTextAtLevel(levelB).extractNGrams(1);

        Map<NGram, Integer> similarNGrams = new HashMap<NGram, Integer>();

        for (NGram a : textANgrams) {
            for (NGram b: textBNgrams) {
                if (a.equals(b)) {
                    if (similarNGrams.containsKey(a)) {
                        similarNGrams.put(a, similarNGrams.get(a) + 1);
                    } else {
                        similarNGrams.put(a, 1);
                    }
                }
            }
        }

        // TODO: Generate a report and include it here
    }
}

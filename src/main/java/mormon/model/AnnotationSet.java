package mormon.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * AnnotationSet
 *
 * An annotation set to be included in an AnnotatedText. This contains the specific NGram, NGram type, and index into
 * the containing document the annotation can be found.
 */
public class AnnotationSet {

    private Map<Integer, Set<Pair<NGram, Integer>>> annotationSet;

    public AnnotationSet() {
        annotationSet = new HashMap<Integer, Set<Pair<NGram, Integer>>>();
    }

    /**
     * Adds the NGram provided from the index provided to the set.
     *
     * @param nGram -
     * @param index -
     */
    public void addNGram(NGram nGram, int index) {
        if (!annotationSet.containsKey(nGram.length())) {
            annotationSet.put(nGram.length(), new HashSet<Pair<NGram, Integer>>());
        }

        Set<Pair<NGram, Integer>> annotations = annotationSet.get(nGram.length());
        annotations.add(new Pair<NGram, Integer>(nGram, index));
    }
}

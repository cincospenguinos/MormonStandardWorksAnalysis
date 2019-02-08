package mormon.model;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

import java.util.*;

/**
 * AnnotatedText
 *
 * A single text that has been annotated (or will be annotated) by Stanford Core NLP library. Allows
 * being broken up into books, chapters, and verses.
 */
public class AnnotatedText {

    // The values for N in generating NGrams internally
    public static final int[] N_GRAM_VALUES = { 1, 2, 3, 4, 5 };

    private TextLevel _textLevel;
    private boolean _isMormon;
    private String _name;

    // Non-leaf node information
    private Map<String, AnnotatedText> _locationsToTexts;

    // Leaf node information
    private Document _text; // The literal text itself. Only relevant to leaf-node types (chapter, verse)
    private AnnotationSet _annotationSet; // The set of annotations to be generated from the text

    public AnnotatedText(TextLevel type, boolean isMormon) {
        _textLevel = type;
        _isMormon = isMormon;

        switch(_textLevel) {
            case BOOK:
            case SECTION:
            case CHAPTER:
                _locationsToTexts = new LinkedHashMap<>();
                break;
            case VERSE:
                break;
        }
    }

    public AnnotatedText(TextLevel type, boolean isMormon, Document text) {
        _textLevel = type;
        _isMormon = isMormon;
        _text = text;
        _annotationSet = new AnnotationSet();
    }

    /**
     * Gathers information on this text and includes it in the various member variables that are associated with
     * this text.
     */
    public void annotate() {
        if (isLeafNode()) {
            generateAnnotationSet();
            return;
        }

        // Go down to each leaf node and annotate themselves
        for (AnnotatedText t : _locationsToTexts.values()) {
            t.annotate();
        }
    }

    /**
     * Adds a section to this annotated text. This text must be a book.
     *
     * @param sectionName -
     * @param section -
     */
    public void addSection(String sectionName, AnnotatedText section) {
        if (_textLevel != TextLevel.BOOK) {
            throw new RuntimeException("Only books may have a section added to them!");
        }

        section.setName(sectionName);
        _locationsToTexts.put(sectionName, section);
    }

    /**
     * Adds a chapter to this annotated text. This text must either be a section or book.
     *
     * @param chapterName -
     * @param chapter -
     */
    public void addChapter(String chapterName, AnnotatedText chapter) {
        if (_textLevel != TextLevel.SECTION && _textLevel != TextLevel.BOOK) {
            throw new RuntimeException("Only sections can have chapters added to them!");
        }

        chapter.setName(chapterName);
        _locationsToTexts.put(chapterName, chapter);
    }

    /**
     * Adds a verse to this annotated text. This text must either be a book or a chapter.
     *
     * @param verseReference -
     * @param verse -
     */
    public void addVerse(String verseReference, AnnotatedText verse) {
        if (_textLevel != TextLevel.SECTION && _textLevel != TextLevel.CHAPTER) {
            throw new RuntimeException("Only sections and chapters may have verses added to them!");
        }

        verse.setName(verseReference);
        _locationsToTexts.put(verseReference, verse);
    }

    /**
     * Returns all of the NGrams found in this annotation, mapping their size to the set of NGrams.
     *
     * @return Map
     */
    public Collection<NGram> getAllNGrams() {
        Collection<NGram> nGrams = new HashSet<>();
        if (isLeafNode()) {
            for (int n : N_GRAM_VALUES) {
                nGrams.addAll(_annotationSet.getAllNGramsOfSize(n));
            }
        } else {
            for (AnnotatedText t : _locationsToTexts.values()) {
                nGrams.addAll(t.getAllNGrams());
            }
        }

        return nGrams;
    }

    public void setType(TextLevel type) {
        _textLevel = type;
    }

    public boolean isMormon() {
        return _isMormon;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    /**
     * Returns a list of all of the annotated texts that are either chapters or sections. Can
     * only be called on a root node.
     *
     * @return List
     */
    public Collection<AnnotatedText> getSectionsOrChapters() {
        if (_textLevel != TextLevel.BOOK) {
            throw new RuntimeException("getSectionsOrChapters() can only be called on a root node!");
        }

        return _locationsToTexts.values();
    }

    /**
     * Helper method. Gathers the various annotations and puts them into this object. To be called only on leaf nodes.
     */
    private void generateAnnotationSet() {
        if (!isLeafNode()) {
            throw new RuntimeException("Cannot call this method on non-leaf nodes!");
        }

        for (Sentence sentence : _text.sentences()) {
            for (int n : N_GRAM_VALUES) {
                addNGrams(n, sentence);
            }
        }
    }

    /**
     * Helper method. Adds NGrams to the annotation set, given a size and sentence to generate them from.
     *
     * @param nGramSize -
     * @param sentence -
     */
    private void addNGrams(int nGramSize, Sentence sentence) {
        List<String> words = sentence.words();

        for (int wordIndex = 0; wordIndex < words.size(); wordIndex++) {
            boolean pastEndOfSentence = (wordIndex + nGramSize> words.size());

            if (!pastEndOfSentence) {
                LinkedList<String> wordSet = new LinkedList<>();
                wordSet.add(words.get(wordIndex));

                for (int i = 1; i < nGramSize; i++) {
                    wordSet.add(words.get(wordIndex + i));
                }

                String[] nGramWords = wordSet.toArray(new String[0]);
                NGram nGram = new NGram(nGramWords);
                _annotationSet.addNGram(nGram, wordIndex);
            } else {
                break;
            }
        }
    }

    private boolean isLeafNode() {
        return _text != null;
    }
}

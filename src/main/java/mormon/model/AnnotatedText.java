package mormon.model;

import edu.stanford.nlp.simple.Document;

import java.util.*;

/**
 * AnnotatedText
 *
 * A single text that has been annotated (or will be annotated) by Stanford Core NLP library. Allows
 * being broken up into books, chapters, and verses.
 */
public class AnnotatedText {

    private Map<String, AnnotatedText> _locationsToTexts; // Maps text locations (introduction, book, chapter, etc.) to texts

    private Document _text; // The literal text itself. Only relevant to leaf-node types (chapter, verse)

    private TextLevel _textLevel;
    private boolean _isMormon;
    private String _name;

    public AnnotatedText(TextLevel type, boolean isMormon) {
        _textLevel = type;
        _isMormon = isMormon;

        switch(_textLevel) {
            case BOOK:
            case SECTION:
            case CHAPTER:
                _locationsToTexts = new LinkedHashMap<String, AnnotatedText>();
                break;
            case VERSE:
                break;
        }
    }

    public AnnotatedText(TextLevel type, boolean isMormon, Document text) {
        _textLevel = type;
        _isMormon = isMormon;
        _text = text;
    }

    /**
     * Returns an AnnotatedText chunked to the level provided. Essentially creates a leaf node
     * out of the current node.
     *
     * @param level -
     * @return
     */
    public AnnotatedText getTextAtLevel(TextLevel level) {
        throw new RuntimeException("Implement me!");
    }

    /**
     * Returns a list of NGrams extracted from the current _text of this AnnotatedText.
     *
     * @param n -
     */
    public List<NGram> extractNGrams(int n) {
        throw new RuntimeException("Implement me!");
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

        _locationsToTexts.put(sectionName, section);
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

        _locationsToTexts.put(verseReference, verse);
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

        _locationsToTexts.put(chapterName, chapter);
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
}

package mormon;

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

    private TextType _type; // The type of text--book, scriptural book (like Nephi or Isaiah,) chapter, verse
    private boolean _isMormon;

    public AnnotatedText(TextType type, boolean isMormon) {
        _type = type;
        _isMormon = isMormon;

        switch(_type) {
            case BOOK:
            case SECTION:
            case CHAPTER:
                _locationsToTexts = new LinkedHashMap<String, AnnotatedText>();
                break;
            case VERSE:
                break;
        }
    }

    public AnnotatedText(TextType type, boolean isMormon, Document text) {
        _type = type;
        _isMormon = isMormon;
        _text = text;
    }

    /**
     * Adds a section to this annotated text. This text must be a book.
     *
     * @param sectionName -
     * @param section -
     */
    public void addSection(String sectionName, AnnotatedText section) {
        if (_type != TextType.BOOK) {
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
        if (_type != TextType.SECTION && _type != TextType.CHAPTER) {
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
        if (_type != TextType.SECTION && _type != TextType.BOOK) {
            throw new RuntimeException("Only sections can have chapters added to them!");
        }

        _locationsToTexts.put(chapterName, chapter);
    }

    public void setType(TextType type) {
        _type = type;
    }

    public boolean isMormon() {
        return _isMormon;
    }
}

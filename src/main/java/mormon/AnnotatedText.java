package mormon;

import edu.stanford.nlp.simple.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AnnotatedText
 *
 * A single text that has been annotated (or will be annotated) by Stanford Core NLP library. Allows
 * being broken up into books, chapters, and verses.
 */
public class AnnotatedText {

    private Map<String, AnnotatedText> _locationsToTexts; // Maps text locations (introduction, book, chapter, etc.) to texts
    private List<String> _locations; // Locations within the text, in order of appearance

    private Document _text; // The literal text itself. Only relevant to leaf-node types (chapter, verse)

    private TextType _type; // The type of text--book, scriptural book (like Nephi or Isaiah,) chapter, verse
    private boolean _isMormon;

    public AnnotatedText(TextType type, boolean isMormon, Document text) {
        _type = type;
        _isMormon = isMormon;

        switch(_type) {
        case BOOK:
            if (text == null) {
                throw new RuntimeException("Text must be included with text of this type!");
            }

            _text = text;
            break;
        case SCRIPTURAL_BOOK:
            if (text != null) {
                _text = text;
                break;
            }
        case CHAPTER:
        case VERSE:
            _locations = new ArrayList<String>();
            _locationsToTexts = new HashMap<String, AnnotatedText>();
            break;
        }
    }
}

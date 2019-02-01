package mormon;

import edu.stanford.nlp.simple.Document;

public class AnnotatedTextFactory {

    private AnnotatedText _textUnderConstruction;

    public AnnotatedTextFactory(TextType type, boolean isMormon) {
        _textUnderConstruction = new AnnotatedText(type, isMormon);
    }

    public void addSection(String sectionName, String sectionText) {
        AnnotatedText section = new AnnotatedText(TextType.SECTION, _textUnderConstruction.isMormon(), new Document(sectionText));
        _textUnderConstruction.addSection(sectionName, section);
    }

    public AnnotatedText getAnnotatedText() {
        return _textUnderConstruction;
    }
}

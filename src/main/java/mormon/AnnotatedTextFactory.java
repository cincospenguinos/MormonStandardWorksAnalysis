package mormon;

import edu.stanford.nlp.simple.Document;

import java.util.Map;

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

    public void addSectionWithVerses(String sectionName, Map<String, String> verses) {
        AnnotatedText section = new AnnotatedText(TextType.SECTION, _textUnderConstruction.isMormon());

        for (Map.Entry<String, String> e : verses.entrySet()) {
            String verseReference = e.getKey();
            String verseText = e.getValue();

            AnnotatedText verse = new AnnotatedText(TextType.VERSE, _textUnderConstruction.isMormon(), new Document(verseText));
            section.addVerse(verseReference, verse);
        }

        _textUnderConstruction.addSection(sectionName, section);
    }

    public void addSectionWithChapters(String sectionName, Map<String, Map<String, String>> chapters) {
        AnnotatedText section = new AnnotatedText(TextType.SECTION, _textUnderConstruction.isMormon());

        for (Map.Entry<String, Map<String, String>> e : chapters.entrySet()) {
            String chapterName = e.getKey();
            Map<String, String> verses = e.getValue();

            AnnotatedText chapter = createChapterWithVerses(verses);
            section.addChapter(chapterName, chapter);
        }

        _textUnderConstruction.addSection(sectionName, section);
    }

    public void addChapterWithVerses(String chapterName, Map<String, String> verses) {
        AnnotatedText chapter = createChapterWithVerses(verses);
        _textUnderConstruction.addChapter(chapterName, chapter);
    }

    private AnnotatedText createChapterWithVerses(Map<String, String> verses) {
        AnnotatedText chapter = new AnnotatedText(TextType.CHAPTER, _textUnderConstruction.isMormon());

        for (Map.Entry<String, String> verse : verses.entrySet()) {
            String verseReference = verse.getKey();
            String verseText = verse.getValue();

            AnnotatedText annotatedVerse = new AnnotatedText(TextType.VERSE, _textUnderConstruction.isMormon(), new Document(verseText));
            chapter.addVerse(verseReference, annotatedVerse);
        }

        return chapter;
    }

}

package mormon.extractors;

import mormon.model.AnnotatedText;
import mormon.model.AnnotatedTextFactory;
import mormon.model.Pair;
import mormon.model.TextLevel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class LateWarExtractor implements TextExtractor {
    public AnnotatedText extractText(File textFile) {
        AnnotatedTextFactory factory = new AnnotatedTextFactory(TextLevel.BOOK, false);
        factory.setName("The Late War");

        for (Map.Entry<String, Pair<String, String>> chapter : extractChaptersFrom(textFile).entrySet()) {
            String chapterName = chapter.getKey();
            String chapterHeader = chapter.getValue().getFirst();
            String chapterText = chapter.getValue().getSecond();

            Map<String, String> verses = new LinkedHashMap<String, String>();
            verses.put("CHAPTER_HEADER", chapterHeader);
            for (Map.Entry<String, String> verse : extractVersesFrom(chapterText).entrySet()) {
                verses.put(verse.getKey(), verse.getValue());
            }

            factory.addChapterWithVerses(chapterName, verses);
        }

        return factory.getAnnotatedText();
    }

    /**
     * Helper method. Extracts chapters from the file provided.
     *
     * @param f -
     * @return
     */
    private Map<String, Pair<String, String>> extractChaptersFrom(File f) {
        Scanner scanner;

        try {
            scanner = new Scanner(f);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find Late War text file!");
        }

        Pattern chapterPattern = Pattern.compile("(CHAP.? \\w+\\.?|[A-Z ]+.?)");
        Map<String, Pair<String, String>> chapters = new LinkedHashMap<String, Pair<String, String>>();

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (chapterPattern.matcher(line).matches()) {
                String headerText = scanner.nextLine();
                String chapterText = scanner.nextLine();

                Pair<String, String> headerAndText = new Pair<String, String>(headerText, chapterText);
                chapters.put(line, headerAndText);
            }
        }

        scanner.close();

        return chapters;
    }

    /**
     * Helper method. Extracts verses from the chapter text provided.
     *
     * @param chapterText -
     * @return
     */
    private Map<String, String> extractVersesFrom(String chapterText) {
        Map<String, String> verses = new LinkedHashMap<String, String>();

        String[] verseChunks = chapterText.split("\\d+");
        for (int i = 0; i < verseChunks.length; i++) {
            String verseName = "verse " + (i + 1);
            String verseText = verseChunks[i];

            verses.put(verseName, verseText);
        }

        return verses;
    }
}

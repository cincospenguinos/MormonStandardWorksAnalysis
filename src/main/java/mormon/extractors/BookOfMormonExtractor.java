package mormon.extractors;

import mormon.model.AnnotatedText;
import mormon.model.AnnotatedTextFactory;
import mormon.model.TextLevel;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class BookOfMormonExtractor implements TextExtractor {

    public AnnotatedText extractText(File bookOfMormonFile) {
        AnnotatedTextFactory factory = new AnnotatedTextFactory(TextLevel.BOOK, true);
        factory.setName("The Book of Mormon");

        Map<String, String> sections = extractSections(bookOfMormonFile);
        for (Map.Entry<String, String> section : sections.entrySet()) {
            String sectionName = section.getKey();
            String sectionText = section.getValue();

            if (sectionHasVerses(sectionText)) {
                if (sectionHasChapters(sectionText)) {
                    Map<String, Map<String, String>> chapters = new LinkedHashMap<String, Map<String, String>>();

                    Map<String, String> chapterChunks = extractChaptersFrom(sectionText);
                    for (Map.Entry<String, String> chapter : chapterChunks.entrySet()) {
                        String chapterName = chapter.getKey();
                        String chapterText = chapter.getValue();

                        if (chapterText.trim().length() != 0) {
                            Map<String, String> verses = extractVersesFrom(chapterText);
                            chapters.put(chapterName, verses);
                        }
                    }

                    factory.addSectionWithChapters(sectionName, chapters);
                } else {
                    Map<String, String> verses = extractVersesFrom(sectionText);
                    factory.addSectionWithVerses(sectionName, verses);
                }
            } else {
                factory.addSection(sectionName, sectionText);
            }
        }

        return factory.getAnnotatedText();
    }

    /**
     * Helper method. Extracts the chapters out of the section text provided.
     *
     * @param sectionText -
     * @return
     */
    private Map<String, String> extractChaptersFrom(String sectionText) {
        Map<String, String> chapters = new LinkedHashMap<String, String>();
        Pattern chapterHeaderPattern = Pattern.compile("(\\d+ )*[\\w ]+ \\d+");
        Pattern nonChapterHeaderPattern = Pattern.compile("Chapter \\d+");

        Scanner scanner = new Scanner(sectionText);
        StringBuilder chapterText = null;
        String chapterHeader = null;

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (nonChapterHeaderPattern.matcher(line).matches()) {
                continue;
            } else if (chapterHeaderPattern.matcher(line).matches()) {
                if (chapterHeader != null) {
                    chapters.put(chapterHeader, chapterText.toString());
                }

                chapterText = new StringBuilder();
                chapterHeader = line;
            } else {
                if (chapterText == null) {
                    chapterHeader = "CHAPTER_HEADER";
                    chapterText = new StringBuilder();
                }

                chapterText.append(line);
                chapterText.append('\n');
            }
        }

        chapters.put(chapterHeader, chapterText.toString());

        scanner.close();

        return chapters;
    }

    /**
     * Helper method. Extracts the verses out of the section or chapter text provided.
     *
     * @param sectionText -
     * @return
     */
    private Map<String, String> extractVersesFrom(String sectionText) {
        Map<String, String> verses = new LinkedHashMap<String, String>();
        Pattern verseHeaderPattern = Pattern.compile("[\\w+ *]+ \\d+:\\d+");

        Scanner scanner = new Scanner(sectionText);
        StringBuilder verseText = null;
        String verseReference = null;

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (verseHeaderPattern.matcher(line).matches()) {
                if (verseText != null) {
                    verses.put(verseReference, verseText.toString());
                }

                verseText = new StringBuilder();
                verseReference = line;
            } else if (line.trim().length() == 0) {
                continue;
            } else {
                if (verseText == null) {
                    // We are in the header of the section, and we should add that
                    verseReference = "SECTION_HEADER";
                    verseText = new StringBuilder();
                }

                verseText.append(line);
                verseText.append('\n');
            }
        }

        verses.put(verseReference, verseText.toString());

        return verses;
    }

    /**
     * Helper method. Returns whether or not the section has chapters.
     *
     * @param sectionText -
     * @return
     */
    private boolean sectionHasChapters(String sectionText) {
        Scanner scanner = new Scanner(sectionText);
        boolean foundChapter = false;
        Pattern pattern = Pattern.compile("Chapter \\d+");

        while(scanner.hasNextLine() && !foundChapter) {
            String line = scanner.nextLine();
            foundChapter = pattern.matcher(line).matches();
        }
        scanner.close();

        return foundChapter;
    }

    /**
     * Helper method. Returns whether or not the section provided has verses.
     *
     * @param section -
     * @return
     */
    private boolean sectionHasVerses(String section) {
        Scanner scanner = new Scanner(section);
        boolean foundVerse = false;
        Pattern pattern = Pattern.compile("\\d* ?[\\w+ ?]+ \\d+:\\d+");

        while(scanner.hasNextLine() && !foundVerse) {
            String line = scanner.nextLine();
            foundVerse = pattern.matcher(line).matches();
        }

        return foundVerse;
    }

    /**
     * Helper method. Extracts sections from the book of mormon file provided.
     *
     * @param bookOfMormonFile
     * @return
     */
    private Map<String, String> extractSections(File bookOfMormonFile) {
        Scanner scanner;

        try {
            scanner = new Scanner(bookOfMormonFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find book of mormon file!");
        }

        Map<String, String> sections = new LinkedHashMap<String, String>();
        StringBuilder currentSectionText = null;
        String currentSectionName = null;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (isTitleOfSection(line)) {
                if (currentSectionText != null) {
                    sections.put(currentSectionName, currentSectionText.toString());
                }

                currentSectionName = line;
                currentSectionText = new StringBuilder();
            } else {
                currentSectionText.append(line);
                currentSectionText.append('\n');
            }
        }

        sections.put(currentSectionName, currentSectionText.toString());
        scanner.close();

        return sections;
    }

    /**
     * Helper method. Returns true if the line provided is the title of a section.
     *
     * @param line
     * @return
     */
    private boolean isTitleOfSection(String line) {
        String newLine = line.replaceAll("\\(*\\)", "").replaceAll("\\W+", "");
        return StringUtils.isAllUpperCase(newLine);
    }
}

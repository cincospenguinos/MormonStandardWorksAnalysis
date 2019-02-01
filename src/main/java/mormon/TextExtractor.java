package mormon;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * TextExtractor class
 *
 * Extracts all of the texts and annotates them according to what is included in each file.
 */
public class TextExtractor {

    private static final String BOOK_OF_MORMON_FILENAME = "book_of_mormon.txt";

    private static final String BOOK_OF_MORMON = "The Book of Mormon";

    private File[] _files;
    private Map<String, AnnotatedText> _annotatedTexts; // Maps name to text

    public TextExtractor(File[] files) {
        _files = files;
        _annotatedTexts = new HashMap<String, AnnotatedText>();
    }


    public void extractTexts() {
        for (File f : _files) {
            String filename = f.getName();

            if (filename.equals(BOOK_OF_MORMON_FILENAME)) {
                AnnotatedText bookOfMormon = extractBookOfMormonText(f);
                _annotatedTexts.put(BOOK_OF_MORMON, bookOfMormon);
            }
        }
    }

    /**
     * Extracts the text of the book of mormon given the file to the Book of Mormon and adds it
     * to the collection of annotated texts.
     *
     * @param bookOfMormonFile -
     */
    private AnnotatedText extractBookOfMormonText(File bookOfMormonFile) {
        AnnotatedTextFactory factory = new AnnotatedTextFactory(TextType.BOOK, true);

        Map<String, String> sections = extractSections(bookOfMormonFile);
        for (Map.Entry<String, String> e : sections.entrySet()) {
            String sectionName = e.getKey();
            String sectionText = e.getValue();

            if (sectionHasVerses(sectionText)) {
                System.out.println(sectionName);
            } else {
                factory.addSection(sectionName, sectionText);
            }
        }

        return factory.getAnnotatedText();
    }

    /**
     * Helper method. Returns whether or not the section provided has verses.
     *
     * @param section
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

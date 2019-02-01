package mormon;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * TextExtractor class
 *
 * Extracts all of the texts and annotates them according to what is included in each file.
 */
public class TextExtractor {

    private static final String BOOK_OF_MORMON_FILENAME = "book_of_mormon.txt";

    private static final String BOOK_OF_MORMON = "The Book of Mormon";

    private File[] _files;
    private Map<String, AnnotatedText> annotatedTexts; // Maps name to text

    public TextExtractor(File[] files) {
        _files = files;
    }


    public void extractTexts() {
        for (File f : _files) {
            if (f.getName().equals(BOOK_OF_MORMON_FILENAME)) {
                extractBookOfMormonText(f);
            }
        }
    }

    /**
     * Extracts the text of the book of mormon given the file to the Book of Mormon and adds it
     * to the collection of annotated texts.
     *
     * @param bookOfMormonFile -
     */
    private void extractBookOfMormonText(File bookOfMormonFile) {
        Scanner scanner;

        try {
            scanner = new Scanner(bookOfMormonFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find book of mormon file!");
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (isTitleOfSection(line)) {
                System.out.println(line);
            }
        }

        scanner.close();
    }

    /**
     * Returns true if the line provided is the title of a section (like a scriptural book)
     *
     * @param line
     * @return
     */
    private boolean isTitleOfSection(String line) {
        String newLine = line.replaceAll("\\(*\\)", "").replaceAll("\\W+", "");
        return StringUtils.isAllUpperCase(newLine);
    }
}

package mormon.version;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * VersionInfo
 *
 * Class that builds information about the current version of this application. Helps indicate to the user of the
 * website and data visualization if there was a new version released or not.
 */
public class VersionInfo {

    private String version;
    private List<String> mormonBooks;
    private List<String> nonMormonBooks;

    public VersionInfo() {
        mormonBooks = new ArrayList<>();
        nonMormonBooks = new ArrayList<>();
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void addMormonBooks(Collection<String> mormonBooks) {
        this.mormonBooks.addAll(mormonBooks);
    }

    public void addNonMormonBooks(Collection<String> nonMormonBooks) {
        this.nonMormonBooks.addAll(nonMormonBooks);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

package mormon.model;

import java.util.Objects;

public class NGram {

    private String[] _words;

    public NGram(String word) {
        _words = new String[1];
        _words[0] = word;
    }

    public NGram(String[] words) {
        _words = words;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NGram) {
            NGram other = (NGram)obj;

            if (this._words.length != other._words.length) {
                return false;
            }

            for (int i = 0; i < this._words.length; i++) {
                if (!this._words[i].equals(other._words[i])) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public int hashCode() {
        StringBuilder builder = new StringBuilder();
        for (String str : _words) {
            builder.append(str);
            builder.append("__");
        }
        return Objects.hash(builder.toString());
    }

    public int length() {
        return _words.length;
    }
}

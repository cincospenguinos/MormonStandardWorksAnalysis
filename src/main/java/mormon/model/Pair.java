package mormon.model;

/**
 * Pair class.
 *
 * Java doesn't have one of these (I don't understand why) so I spun my own up.
 *
 * @param <A>
 * @param <B>
 */
public class Pair<A, B> {

    private A _a;
    private B _b;

    public Pair(A a, B b) {
        _a = a;
        _b = b;
    }

    public A getFirst() {
        return _a;
    }

    public B getSecond() {
        return _b;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            Pair other = (Pair) obj;

            if (other._a != null && other._b != null) {
                return other._a.equals(this._a) && other._b.equals(this._b);
            }
        }

        return false;
    }
}

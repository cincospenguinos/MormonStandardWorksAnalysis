package mormon;

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
}

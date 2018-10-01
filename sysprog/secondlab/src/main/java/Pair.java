public class Pair<F, S> {
    private final F f;
    private final S s;


    Pair(F f, S s) {
        this.f = f;
        this.s = s;
    }

    public static <F, S> Pair<F, S> of(F f, S s) {
        return new Pair<>(f, s);
    }

    public F getFirst() {
        return f;
    }

    public S getSecond() {
        return s;
    }
}
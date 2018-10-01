import java.util.*;

public class Automat {
    private final Set<Character> sigma;
    private final char epsilon;
    private final String q0;
    private final Map<String, Map<Character, Set<String>>> delta;

    public Set<String> getFinals() {
        return finals;
    }

    private final Set<String> finals;

    public Automat(Set<Character> sigma,
                   char epsilon,
                   String q0,
                   Map<String, Map<Character, Set<String>>> transitions,
                   Set<String> finals) {
        this.sigma = sigma;
        this.epsilon = epsilon;
        this.q0 = q0;
        this.delta = transitions;
        this.finals = finals;
    }

    public Map<String, Map<Character, Set<String>>> getDelta() {
        return delta;
    }

    public Set<String> getQ0() {
        HashSet<String> s = new HashSet<>();
        s.add(q0);
        return closure(s);
    }


    public boolean equalsTo(Automat a) {
        if (!sigma.equals(a.sigma) || epsilon != a.epsilon) {
            return false;
        }

        Pair<Set<String>, Set<String>> q0 = Pair.of(getQ0(), a.getQ0());
        Queue<Pair<Set<String>, Set<String>>> q = new LinkedList<>();
        Set<Set<String>> v1 = new HashSet<>();
        Set<Set<String>> v2 = new HashSet<>();
        q.add(q0);
        while (!q.isEmpty()) {
            Pair<Set<String>, Set<String>> p = q.poll();
            if (isTerminal(p.getFirst()) != a.isTerminal(p.getSecond())) {
                return false;
            }
            v1.add(p.getFirst());
            v2.add(p.getSecond());
            for (char c : sigma) {
                Set<String> n1 = next(p.getFirst(), c);
                Set<String> n2 = a.next(p.getSecond(), c);
                if (!v1.contains(n1) || !v2.contains(n2)) {
                    q.add(Pair.of(n1, n2));
                }
            }
        }
        return true;
    }

    Set<String> closure(Set<String> states) {
        Set<String> res = new HashSet<>(states);
        Set<String> eps = res;
        do {
            eps = move(eps, epsilon);
            res.addAll(eps);
        } while (!eps.isEmpty());
        return res;
    }

    public Set<String> move(Set<String> states, char c) {
        Set<String> res = new HashSet<>();
        for (String state : states) {
            if (delta.containsKey(state) && delta.get(state).containsKey(c)) {
                res.addAll(delta.get(state).get(c));
            }
        }
        return res;
    }

    public Set<String> next(Set<String> states, char c) {
        return closure(move(states, c));
    }

    public boolean isTerminal(Set<String> states) {
        Set<String> intersection = new HashSet<>(finals);
        intersection.retainAll(states);
        return !intersection.isEmpty();
    }

    public static class Builder {
        private Set<Character> sigma = new HashSet<>();
        private char epsilon = '@';
        private String q0;
        private Map<String, Map<Character, Set<String>>> delta = new HashMap<>();
        private Set<String> finals = new HashSet<>();

        public boolean equalsTo(Builder b) {
            return build().equalsTo(b.build());
        }

        public Builder setQ0(String q0) {
            this.q0 = q0;
            return this;
        }

        public Builder setEpsilon(char epsilon) {
            this.epsilon = epsilon;
            return this;
        }

        public Builder addSigma(String sigma) {
            for (char c : sigma.toCharArray()) {
                this.sigma.add(c);
            }
            return this;
        }

        public Builder addFinals(String... finals) {
            this.finals.addAll(Arrays.asList(finals));
            return this;
        }

        public Builder bind(String from, String to, char... chars) {
            for (char c : chars) {
                if (!delta.containsKey(from)) {
                    delta.put(from, new HashMap<>());
                }
                if (!delta.get(from).containsKey(c)) {
                    delta.get(from).put(c, new HashSet<>());
                }
                delta.get(from).get(c).add(to);
            }
            return this;
        }

        public Automat build() {
            return new Automat(sigma, epsilon, q0, delta, finals);
        }
    }
}

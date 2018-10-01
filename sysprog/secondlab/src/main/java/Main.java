import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        final String SIGMA = "0123456789+-.";
        final char[] DIGITS = "0123456789".toCharArray();
        Automat.Builder a = new Automat.Builder();
        a.setQ0("q0");
        a.addSigma(SIGMA);
        a.setEpsilon('@');
        a.bind("q0", "q1", "+-@".toCharArray());
        a.bind("q1", "q1", DIGITS);
        a.bind("q1", "q2", '.');
        a.bind("q2", "q3", DIGITS);
        a.bind("q3", "q3", DIGITS);
        a.bind("q1", "q4", DIGITS);
        a.bind("q4", "q3", '.');
        a.bind("q3", "q5", '@');
        a.addFinals("q5");
        Automat automat = a.build();
        String word = readFromFile("input.txt");
        finalStates = calculateFinalStates(automat);
        List<Pair<String, Set<String>>> states = new ArrayList<>();
        boolean flag = false;
        for (String s : automat.getDelta().keySet()) {
            Set<String> set = buildSet(automat, word, s);
            Pair<String, Set<String>> p = new Pair<>(s, set);
            states.add(p);
        }

        for (Pair<String, Set<String>> p : states) {
            Set<String> f = automat.getFinals();
            Set<String> finals = calculateFinalStatesFrom(automat, p.getSecond());
            //System.out.println(p.getFirst()+ " " + finals);
            f.retainAll(finals);
            if (!f.isEmpty()) {
                if (finalStates.contains(p.getFirst())) {
                    System.out.println(p.getFirst());
                    flag = true;
                }
            }
        }
        if (!flag) System.out.println(false);
    }

    private static Set<String> finalStates;

    static Set<String> calculateFinalStatesFrom(Automat automat, Set<String> beginningStates) {
        Set<String> states = new HashSet<>(beginningStates);
        List<String> list = new ArrayList<>(states);
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            List<Set<String>> newList = new ArrayList<>(automat.getDelta().getOrDefault(s, new HashMap<>()).values());
            for (Set<String> set : newList) {
                for (String str : set) {
                    if (!states.contains(str)) {
                        states.add(str);
                        list.add(str);
                    }
                }
            }
        }
        return states;
    }

    static Set<String> calculateFinalStates(Automat automat) {
        return calculateFinalStatesFrom(automat, automat.getQ0());
    }

    static String readFromFile(String path) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
        return bufferedReader.readLine();
    }

    private static Set<String> buildSet(Automat a, String word, String firstState) {
        Set<String> r = new HashSet<>();
        r.add(firstState);
        for (int i = 0; i < word.length(); i++) {
            r = a.move(r, word.charAt(i));
            if (r.isEmpty()) break;
        }
        System.out.println(firstState + ": " + r.toString());
        return r;
    }

}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public class Main {
    public static void main(String[] args) {
        Lexeme lexeme = new Lexeme(Lexeme.Type.HEX, 0, 0);
        System.out.println("\n");
        String[] text = {"(println \"Hello world!\")", "(defn square [x]\n" +
                "  (* x x))", "; More basic examples:\n" +
                "\n" +
                "; str will create a string out of all its arguments\n" +
                "(str \"Hello\" \" \" \"World\"); => \"Hello World\"\n" +
                "; Math is straightforward\n" +
                "(+ 1 1) ; => 2\n" +
                "(- 2 1) ; => 1\n" +
                "(* 1 2) ; => 2\n" +
                "(/ 2 1) ; => 2\n" +
                "\n" +
                "; Equality is =\n" +
                "(= 1 1) ; => true\n" +
                "(= 2 1) ; => false", "(if false \"a\" \"b\")"};
        Arrays.stream(text).forEach(
                x -> {
                    runAll(x);
                    if (x != text[text.length - 1])
                        System.out.println();
                }
        );
    }

    private static void runAll(String text) {
        List<Lexeme> lexemes = new ArrayList<>();

        lexemes.addAll(findLexemes(text, Lexeme.Type.COMMENT));
        lexemes.addAll(findLexemes(text, Lexeme.Type.LITERALS));
        //lexemes.addAll(findLexemes(text, Lexeme.Type.MACRO));
        lexemes.addAll(findLexemes(text, Lexeme.Type.KEYWORD));

        lexemes.addAll(findLexemes(text, Lexeme.Type.OPERATOR));
        lexemes.addAll(findLexemes(text, Lexeme.Type.NUMBER));
        //lexemes.addAll(findLexemes(text, Lexeme.Type.TYPE));
        lexemes.addAll(findLexemes(text, Lexeme.Type.PUNCTUATION));

        System.out.println(solve(text, lexemes));
    }

    private static List<Lexeme> findLexemes(String text, Lexeme.Type type) {
        Matcher m = type.compilePattern().matcher(text);
        List<Lexeme> lexemes = new ArrayList<>();
        while (m.find()) {
            if (m.start() != m.end())
                lexemes.add(new Lexeme(type, m.start(), m.end()));
        }
        return lexemes;
    }

    private static String solve(String text, List<Lexeme> lexemes) {
        StringBuilder builder = new StringBuilder();

        lexemes.sort(
                (x, y) ->
                        x.getEnd() > y.getEnd() || x.getEnd() == y.getEnd() && x.getStart() < y.getStart()
                                ? -1
                                : 1);
        int l = text.length();
        for (Lexeme lexeme : lexemes) {
            if (lexeme.getEnd() <= l) {
                builder.append(lexeme.getType() + ": " + text.substring(lexeme.getStart(), lexeme.getEnd()) + "\n");//lexeme.getStart() + " " + lexeme.getEnd()+"\n");
                l = lexeme.getStart();
            }
        }
        return text + "\n" + String.join("\n", myReverse(Arrays.asList(builder.toString().split("\n"))));
    }

    private static List<String> myReverse(List<String> arr) {
        List<String> res = new ArrayList<>();
        for(int i = arr.size() - 1; i>=0;i--){
            res.add(arr.get(i));
        }
        return res;
    }
}

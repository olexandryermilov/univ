import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Lexeme lexeme = new Lexeme(Lexeme.Type.HEX, 0, 0);
        String[] text = {/*"(println \"Hello world!\")", "(defn square [x]\n" +
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
                "(= 2 1) ; => false", "(if false \"a\" \"b\")\n" +
                "0x9999222AA3 0035 0.34 def let letnfdasfs ",*/ readFromFile("input.txt")};
        Arrays.stream(text).forEach(
                x -> {
                    runAll(x);
                    if (x != text[text.length - 1])
                        System.out.println();
                }
        );

    }

    private static String readFromFile(String path) throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
        return bufferedReader.lines().collect(Collectors.joining("\n"));
    }

    private static void runAll(String text) {
        List<Lexeme> lexemes = new ArrayList<>();

        lexemes.addAll(findLexemes(text, Lexeme.Type.COMMENT));
        lexemes.addAll(findLexemes(text, Lexeme.Type.LITERALS));
        lexemes.addAll(findLexemes(text, Lexeme.Type.KEYWORD));

        lexemes.addAll(findLexemes(text, Lexeme.Type.OPERATOR));
        lexemes.addAll(findLexemes(text, Lexeme.Type.HEX));
        lexemes.addAll(findLexemes(text, Lexeme.Type.OCT));
        lexemes.addAll(findLexemes(text, Lexeme.Type.DEC));
        lexemes.addAll(findLexemes(text, Lexeme.Type.FLOAT));
        lexemes.addAll(findLexemes(text, Lexeme.Type.BOOLEAN));
        lexemes.addAll(findLexemes(text, Lexeme.Type.PUNCTUATION));
        //solve(text,lexemes);
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
        boolean[] skip = new boolean[text.length()];
        for (int i = 0; i < skip.length; i++) skip[i] = false;

        lexemes.sort(
                (x, y) ->
                        x.getEnd() > y.getEnd() || x.getEnd() == y.getEnd() && x.getStart() < y.getStart()
                                ? -1
                                : 1);
        int l = text.length();
        for (Lexeme lexeme : lexemes) {
            if (lexeme.getEnd() <= l) {
                System.out.println(lexeme.getType().toString());
                if (!lexeme.getType().toString().startsWith("Comment"))
                    builder.append("<" + text.substring(lexeme.getStart(), lexeme.getEnd()) + ": " + lexeme.getType() + ">\n");
                else
                    builder.append("<" + text.substring(lexeme.getStart(), lexeme.getEnd()).replace("\n", "") + ": " + lexeme.getType() + ">\n");
                for (int i = lexeme.getStart(); i < lexeme.getEnd(); i++) skip[i] = true;
                l = lexeme.getStart();
            }
        }

        return text + "\n" + String.join("\n", myReverse(Arrays.asList(builder.toString().split("\n")))) + getUnknown(text, skip);
    }

    private static String getUnknown(String text, boolean[] skip) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < skip.length; i++) {
            if (!skip[i])
                if ((sb.length() == 0 && text.charAt(i) != ' ') || (sb.length() > 0 && (sb.toString().charAt(sb.toString().length() - 1) != ' ' || text.charAt(i) != ' ')))
                    sb.append(text.charAt(i));
        }
        return "\n<" + cleanString(sb.toString().replace("\n", "")) + ": Unknown>\n";
    }

    private static String cleanString(String text) {
        while (text.contains("  "))
            text = text.replace("  ", " ");
        return text;
    }

    private static List<String> myReverse(List<String> arr) {
        List<String> res = new ArrayList<>();
        for (int i = arr.size() - 1; i >= 0; i--) {
            res.add(arr.get(i));
        }
        return res;
    }
}

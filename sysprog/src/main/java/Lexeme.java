import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Lexeme {
    private final Type type;
    private final int start;
    private final int end;

    public Lexeme(Type type, int start, int end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public Type getType() {
        return type;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public static class TypeName {
        String name;

        public TypeName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return getName();
        }
    }


    public enum Type {
        SINGLE_COMMENT(";(.|)*\\n"),
        COMMENT(new TypeName("Comment"), SINGLE_COMMENT.pattern),
        NAMESPACE(new TypeName("Namespace"), "\\(ns \\w*\\)\\n"),

        STRING("\".*\""),
        CHAR("\'.*\'"),
        LITERALS(new TypeName("Literals"), "\"[^\"]*\""),

        FLOAT("(\\d+\\.\\d*|\\.\\d+|\\d+)([eE][+-]?\\d+)?"),

        HEX("0x[0-9a-fA-F]+[LlUu]*"),
        OCT("0[0-7]+[LlUu]*"),
        DEC("\\d+[LlUu]*"),
        INTEGER(HEX.pattern, OCT.pattern, DEC.pattern),
        NUMBER(new TypeName("Number"), FLOAT.pattern, INTEGER.pattern),

        KEYWORD(
                new TypeName("KeyWord"),
                "if|then|do|let|fn|apply|def|"
                        + "defn|loop|recur|throw|try|catch|finally|monitor-enter|monitor-exit|new|set!|:as|:keys"),

        OPERATOR(new TypeName("Operator"), "~|not|%|^|&|\\*|\\+|=|\\||\\?|:|<|>|\\|/|-"),
        TYPE(
                new TypeName("Type"),
                "(bool|int|uint8|uint16|uint32|uint64|long|float|short|double|char|"
                        + "unsigned|signed|void|wchar_t)[*&]?"),
        PUNCTUATION(new TypeName("Punctuation"), "[()\\[\\],.]");
        private final TypeName typeName;
        private final String pattern;

        Type(TypeName typeName, String... patterns) {
            this.typeName = typeName;
            this.pattern = "(" + ((patterns.length > 0) ? String.join("|", patterns) : patterns[0]) + ")";
            if (!typeName.getName().equals("empty"))
                System.out.println(this.typeName + ": " + this.pattern);
        }

        Type(String... patterns) {
            this(new TypeName("empty"), patterns);
        }


        public Pattern compilePattern() {
            return Pattern.compile(pattern);
        }

        @Override
        public String toString() {
            return this.typeName + ": ";
        }
    }
}

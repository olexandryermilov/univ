package com.yermilov.univ.sysprog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.lang.Character.isLetter;

class Worker {
    private final String VOWELS = "aoeiuAOEIU";
    private final char[] VOWELS_LOWERCASE = {'a', 'o', 'e', 'i', 'u'};
    private final char[] VOWELS_UPPERCASE = {'A', 'O', 'E', 'I', 'U'};

    boolean isWord(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (!(isLetter(word.charAt(i)) || (word.charAt(i) == '\'' && i > 0 && isLetter(word.charAt(i - 1)) &&
                    i < word.length() - 1 && isLetter(word.charAt(i + 1))))) {
                return false;
            }
        }
        return word.length() > 0;
    }

    String[] splitter(String text) {
        return new ArrayList<>(Arrays.asList(text.split("[^a-zA-Z']")))
                .stream()
                .filter(this::isWord).toArray(String[]::new);
    }

    String[] sortByVowels(String[] words) {
        Arrays.sort(words, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int vowelsFirst = 0;
                int vowelsSecond = 0;
                for (int i = 0; i < o1.length(); i++) {
                    if (VOWELS.contains("" + o1.charAt(i))) vowelsFirst++;
                }
                for (int i = 0; i < o2.length(); i++) {
                    if (VOWELS.contains("" + o2.charAt(i))) vowelsSecond++;
                }
                return new Fraction(vowelsFirst, o1.length()).compareTo(new Fraction(vowelsSecond, o2.length()));
            }

            class Fraction implements Comparable<Fraction> {
                int a, b;

                public Fraction(int a, int b) {
                    this.a = a;
                    this.b = b;
                }

                private int signOfInt(int num) {
                    return Integer.compare(num, 0);
                }

                @Override
                public int compareTo(Fraction o) {
                    int diff = this.a * o.b - this.b * o.a;
                    if (diff == 0) return signOfInt(this.b - o.b);
                    return signOfInt(diff);
                }
            }
        });
        return words;
    }
}

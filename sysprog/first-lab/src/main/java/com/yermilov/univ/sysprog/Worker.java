package com.yermilov.univ.sysprog;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

class Worker {
    private final String VOWELS = "aoeiuAOEIUаоуяеёиюії";

    boolean isLetter(char ch) {
        char lcch = Character.toLowerCase(ch);
        return Character.isLetter(ch) || (lcch <= 'я' && lcch >= 'а');
    }

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
        return new ArrayList<>(Arrays.asList(text.split("[^a-zA-Zа-яА-Я']")))
                .stream()
                .distinct()
                .filter(this::isWord)
                .filter(word -> word.length() <= 30)
                .toArray(String[]::new);
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

    String readFromFile(String path) throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
        return bufferedReader.lines().collect(Collectors.joining("\n"));
    }

    public String[] doJob(String path) throws FileNotFoundException {
        String text = readFromFile(path);
        String[] splittedText = splitter(text);
        return sortByVowels(splittedText);
    }
}

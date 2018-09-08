package com.yermilov.univ.sysprog;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class WorkerTest {
    private Worker worker;

    @Before
    public void createWorker() {
        worker = new Worker();
    }

    @Test
    public void splitterTest() {
        final String TEXT = "Hello world\nI think, it is a beautiful day today";
        final String[] words = {"Hello", "world", "I", "think", "it", "is", "a", "beautiful", "day", "today"};
        System.out.println((worker.splitter(TEXT)));
        assertArrayEquals(words, worker.splitter(TEXT));
    }

    @Test
    public void isWordTest_returnsTrue_ForSimpleWord() {
        final String WORD = "Hello";
        assertTrue(worker.isWord(WORD));
    }

    @Test
    public void isWord_returnsTrue_ForWordWithApostrophe() {
        final String WORD = "It's";
        assertTrue(worker.isWord(WORD));
    }

    @Test
    public void isWord_returnsFalse_ForDifferentNotLetters() {
        final String[] WORDS = {" ", "  ", "", "!r", "\n"};
        for (String word : WORDS) {
            assertFalse(worker.isWord(word));
        }
    }

    @Test
    public void sortByVowels_sorts() {
        final String[] UNSORTED_WORDS = {"Hello", "world", "I", "think", "it", "is", "a", "beautiful", "day", "today"};
        final String[] SORTED_WORDS =   {"world", "think", "day", "Hello", "today", "it", "is", "beautiful", "I", "a"};
        assertArrayEquals(SORTED_WORDS, worker.sortByVowels(UNSORTED_WORDS));
    }
}

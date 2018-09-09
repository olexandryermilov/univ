package com.yermilov.univ.sysprog;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

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

    @Test
    public void readFromFile_Reads() throws FileNotFoundException {
        final String FILE_TEXT = "Hello world!\nI am Sasha";
        final String FILE_PATH = "src/test/resources/testFile.txt";
        assertEquals(FILE_TEXT, worker.readFromFile(FILE_PATH));
    }

    @Test
    public void doJob_returnsRightResult() throws FileNotFoundException {
        final String FILE_PATH = "src/test/resources/testFile1.txt";
        final String[] SORTED_WORDS = {"world", "think", "day", "Hello", "today", "it", "is", "beautiful", "I", "a"};
        assertArrayEquals(worker.doJob(FILE_PATH),SORTED_WORDS);
    }
}

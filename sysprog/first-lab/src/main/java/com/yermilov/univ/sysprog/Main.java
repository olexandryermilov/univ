package com.yermilov.univ.sysprog;

import com.yermilov.univ.sysprog.helpers.Messages;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import static com.yermilov.univ.sysprog.helpers.Messages.HELP_MESSAGE;

public class Main {
    private final static String FILE_PATH = "input.txt";
    private final static String HELP = "--help";

    public static void main(String[] args) throws FileNotFoundException {
        String path = FILE_PATH;
        if (args.length == 1) {
            if (args[0].toLowerCase().equals(HELP)) {
                System.out.println(HELP_MESSAGE);
                return;
            } else {
                path = args[0];
                File file = new File(path);
                if (!file.exists()) {
                    System.out.println(Messages.FILE_NOT_EXISTS_MESSAGE);
                    return;
                }

            }
        }
        if (args.length > 1) {
            System.out.println(Messages.WRONG_USAGE_MESSAGE);
            return;
        }
        System.out.println("Please, write path to file");
        Scanner scanner = new Scanner(System.in);
        path = scanner.nextLine();
        File file = new File(path);
        if (!file.exists()) {
            System.out.println(Messages.FILE_NOT_EXISTS_MESSAGE);
            return;
        }
        Worker worker = new Worker();
        System.out.println(Arrays.deepToString(worker.doJob(path)));
    }
}

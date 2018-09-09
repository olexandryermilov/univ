package com.yermilov.univ.sysprog.helpers;

public class Messages {
    public final static String HELP_MESSAGE = "List of commands:\nMain - to parse file named \"input.txt\" in local" +
            " directory\nMain [path to file] - to parse specific file\nMain --help - to see usage";
    public final static String WRONG_USAGE_MESSAGE = "Wrong usage. Should be Main [path to file|--help]";
    public final static String FILE_NOT_EXISTS_MESSAGE = "File not exists. Please, enter the right path to file.";
}

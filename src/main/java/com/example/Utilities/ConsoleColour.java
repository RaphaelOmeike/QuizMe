package com.example.Utilities;

public class ConsoleColour {

    // ANSI escape codes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void printColored(String message, boolean isSuccess) {
        String color = isSuccess ? ANSI_GREEN : ANSI_RED;
        System.out.println(color + message + ANSI_RESET);
    }

    public static void printFailure(String message) {
        printColored(message, false);
    }

    public static void printSuccess(String message) {
        printColored(message, true);
    }
}

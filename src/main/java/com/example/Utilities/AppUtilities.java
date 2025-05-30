package com.example.Utilities;

import java.io.Console;
import java.time.Duration;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.Models.Enums.Gender;

public class AppUtilities {

    public static final Scanner scanner = new Scanner(System.in);
    public static int idIncrement = 21;
    public static int getTrueId(int userInput) {
        return userInput - idIncrement;
    }
    public static int incrementId(int id) {
        return id + idIncrement;
    }
    public static String SanitizeString(String input) {
        String[] arr = input.strip().split(" ");
        String result = "";
        for (var i: arr)
            result += ToSentenceCase(i) + " ";
        return result.strip();
    }

    public static String ToSentenceCase(String input) {
        return Character.toString(input.charAt(0)).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static int getValidatedIntegerInput(String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);

            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                scanner.nextLine(); // clear the leftover newline
                break;
            } else {
                ConsoleColour.printFailure("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // discard the invalid input
            }
        }

        return value;
    }


//    public static float getValidatedFloatInput(String prompt) {
//        float value;
//
//        while (true) {
//            System.out.print(prompt);
//
//            if (scanner.hasNextFloat()) {
//                value = scanner.nextFloat();
//                scanner.nextLine(); // clear the leftover newline
//                break;
//            } else {
//                ConsoleColour.printFailure("Invalid input. Please enter a valid float number.");
//                scanner.nextLine(); // discard the invalid input
//            }
//        }
//
//        return value;
//    }

    public static String getStringInput(String prompt) {
        System.out.print(prompt); // ✅ show the prompt first
        String userInput = scanner.nextLine().trim();
        while (userInput.isEmpty()) {
            ConsoleColour.printFailure("Invalid input, try again.");
            System.out.print(prompt);
            userInput = scanner.nextLine().trim();
        }
        return SanitizeString(userInput);
    }


    public static String getPureStringInput(String prompt) {
        System.out.print(prompt);
        String userInput = scanner.nextLine().trim();
        while (userInput.isEmpty()) {
            ConsoleColour.printFailure("Invalid input, try again.");
            System.out.print(prompt);
            userInput = scanner.nextLine();
        }
        return userInput;
    }

    public static String getLowerStringInput(String prompt) {
        return getPureStringInput(prompt).toLowerCase();
    }


    public static String getOptionalStringInput(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

//    public static String getValidAnswer(String actualLetters) {
//        String answer = AppUtilities.getOptionalStringInput("Enter answer/option: ").toUpperCase();
//
//        //get valid answer
//        while (!actualLetters.contains(answer) || answer.length() != 1) {
//            ConsoleColour.printFailure("Invalid input, try again.");
//            answer = AppUtilities.getOptionalStringInput("Re-enter answer/option: ").toUpperCase();
//        }
//        return answer;
//    }

    public static String getPassword(String prompt) {
        Console console = System.console();
        if (console == null) {
            System.out.println("Console not available. Run this in a real terminal.");
            return null;
        }

        char[] passwordChars = console.readPassword(prompt);
        return new String(passwordChars);
    }

    public static Gender getEnumGender() {
        String prompt = "Choose:\n\t1 - Male\n\t2 - Female\nEnter your gender: ";
        int value;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                scanner.nextLine(); // ← consume the newline
                if (value == 1) {
                    return Gender.MALE;
                } else if (value == 2) {
                    return Gender.FEMALE;
                }
            } else {
                scanner.nextLine(); // ← clear invalid input
            }
            ConsoleColour.printFailure("Invalid input. Please enter a valid integer.");
        }
    }

    public static void PressEnterToContinue() {
        System.out.println("Press enter to continue!!");
        scanner.nextLine();
    }
    
    public static void PrintDotAnimation(int no) {
        for (int i = 0; i < no; i++) {
            System.out.print(".");
            try {
                Thread.sleep(75);
            }
            catch (Exception e) {
                System.out.println("Error occurred in sleeping the system...");
            }
        }
        System.out.println();
        ClearConsole();
    }
    public static void PrintDefaultDotAnimation() {
        PrintDotAnimation(10);
    }

    public static void ClearConsole() {
        // won't work in intellij and vscode ide's
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }


}
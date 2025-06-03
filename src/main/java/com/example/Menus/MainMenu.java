package com.example.Menus;

import org.springframework.stereotype.Component;

import com.example.Utilities.AppUtilities;
import com.example.Utilities.ConsoleColour;

@Component
public class MainMenu {
    private final PlayerMenu _playerMenu;
    private final GameMenu _gameMenu;
    public MainMenu(PlayerMenu playerMenu, GameMenu gameMenu) {
        _playerMenu = playerMenu;
        _gameMenu = gameMenu;
    }
    public void PrintMainMenu() {
        while (true) {
            System.out.println("WELCOME TO QUIZ_ME!!!!!");
            System.out.println("----Main Menu----");
            System.out.println("1----Register");
            System.out.println("2----Login");
            System.out.println("3----Game Quiz me!!");
            System.out.println("\n\n0----To quit");

            int userChoice = AppUtilities.getValidatedIntegerInput("Enter choice: ");
            if (userChoice == 0) {
                ConsoleColour.printFailure("Oops, quit!!!!");
                break;
            }
            HandleInputs(userChoice);
        }

        //not sure of this yet
    }

    public void HandleInputs(int userChoice) {
        switch (userChoice) {
            case 1 -> _playerMenu.RegisterPlayer();
            case 2 -> {
                var user = _playerMenu.Login();
                if (user != null)
                    _playerMenu.LoginMenu();
                //log out handled
            } 
            case 3 -> _gameMenu.BeginQuizme();
            default -> ConsoleColour.printFailure("Invalid input, try again!!!!!!!\n");
        }
    }

    
}

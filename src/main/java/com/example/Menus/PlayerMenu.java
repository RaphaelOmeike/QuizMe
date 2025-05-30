package com.example.Menus;

import org.springframework.stereotype.Component;

import com.example.DTOs.Players.PlayerRequest;
import com.example.DTOs.Users.UserRequest;
import com.example.DTOs.Users.UserResponse;
import com.example.Models.Enums.Gender;
import com.example.Models.User;
import com.example.Services.Interfaces.IPlayerService;
import com.example.Services.Interfaces.IUserService;
import com.example.Utilities.AppUtilities;
import com.example.Utilities.ConsoleColour;

@Component
public class PlayerMenu {
    private final IPlayerService _playerService;
    private final IUserService _userService;
    private final QuizMenu _quizMenu;
    public PlayerMenu(IPlayerService playerService, IUserService userService, QuizMenu quizMenu) {
        _playerService = playerService;
        _userService = userService;
        _quizMenu = quizMenu;
    }

    public void LoginMenu() {
        boolean canContinue = true;
        do {
            System.out.println("Login Menu");
            System.out.println("1----Create quiz");
            System.out.println("2----Load quiz questions");
            System.out.println("3----Preview quiz questions");
            System.out.println("4----View created quizzes");
            System.out.println("5----VIEW Quiz me's (available)");
            System.out.println("6----Log out");

            // make the menus above repeatable broo!! by God's grace and Mother Mary's Intercession,
            // broo. Thank you, Jesus!!

            int userChoice = AppUtilities.getValidatedIntegerInput("\nEnter choice: ");
            if (userChoice != 6) {
                HandleInputs(userChoice);
            }
            else {
                System.out.println("Logging out...");
                _userService.Logout();
                AppUtilities.PrintDefaultDotAnimation();
                canContinue = false;
            }

        } while (canContinue);
    }

    public void HandleInputs(int userChoice) {
        switch (userChoice) {
            case 1 -> _quizMenu.CreateQuiz();
            case 2 -> _quizMenu.LoadQuizQuestions();
            case 3 -> _quizMenu.PreviewQuizQuestions();
            case 4 -> _quizMenu.ViewCreatedQuizzes();
             case 5 -> _quizMenu.HandleQuizmeHistory(); //in progress

            default -> ConsoleColour.printFailure("Invalid input, try again!!!!!!!\n");
        }
    }

    public void RegisterPlayer() {
        String userName = AppUtilities.getPureStringInput("Enter your username (unique): ");
        String name = AppUtilities.getStringInput("Enter your name: ");
        String email = AppUtilities.getLowerStringInput("Enter your email: ");

        String password = AppUtilities.getPassword("Enter your password: ");
        String cPassword = AppUtilities.getPassword("Confirm your password: ");

        while (!password.equals(cPassword) || password.isEmpty()) {
            ConsoleColour.printFailure("Error!! Passwords do not match!!");
            password = AppUtilities.getPassword("Enter your password: ");
            cPassword = AppUtilities.getPassword("Confirm your password: ");
        }

        Gender gender = AppUtilities.getEnumGender();

        if (_userService.CheckUsernameExists(userName)) {
            ConsoleColour.printFailure("Username -  " + userName + " already exists.");
            ConsoleColour.printFailure("Registration failed!!");
            return;
        }
        if (_userService.GetUserByEmail(email) != null) {
            ConsoleColour.printFailure("Email -  " + email + " already exists.");
            ConsoleColour.printFailure("Registration failed!!");
            return;
        }
        _userService.CreateUser(new UserRequest(userName, email, password));
        _playerService.CreatePlayer(new PlayerRequest(name, email, gender));

        ConsoleColour.printSuccess("Registration successful!!");

    }

    public UserResponse Login() {
        String email = AppUtilities.getPureStringInput("Enter your username/email: ");

        String password = AppUtilities.getPassword("Enter your password: ");

        var user = _userService.Login(new UserRequest(email, email.toLowerCase(), password));

        if (user == null)
            return null;
        WelcomeUser(user.Username());
        AppUtilities.PressEnterToContinue();
        AppUtilities.PrintDefaultDotAnimation();

        return user;
        //continue from here
    }

    // public static void Logout() {
    //     System.out.println("Welcome, " + name + "!!");
    // }
    public static void WelcomeUser(String name) {
        System.out.println("Welcome, " + name + "!!");
    }
    public String GetPlayerName(int playerId) {
        return _playerService.GetPlayerById(playerId).Name();
    }
}

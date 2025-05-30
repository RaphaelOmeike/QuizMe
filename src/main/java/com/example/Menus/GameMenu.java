package com.example.Menus;

import java.awt.desktop.QuitResponse;

import com.example.App;
import com.example.Utilities.ConsoleColour;
import org.springframework.stereotype.Component;

import com.example.DTOs.Games.GameRequest;
import com.example.DTOs.Quizzes.QuizResponse;
import com.example.DTOs.Users.UserResponse;
import com.example.Models.Enums.QuizType;
import static com.example.Models.Enums.QuizType.INSTANT;
import com.example.Services.Implementations.GameService;
import com.example.Services.Implementations.PlayerService;
import com.example.Services.Implementations.QuizService;
import com.example.Services.Implementations.UserService;
import com.example.Utilities.AppUtilities;

@Component
public class GameMenu {
    private final PlayerMenu _playerMenu;
    private final QuizMenu _quizMenu;
    private final GameService _gameService;
    public GameMenu(PlayerMenu playerMenu, QuizMenu quizMenu, GameService gameService) {
        _playerMenu = playerMenu;
        _quizMenu = quizMenu;
        _gameService = gameService;
    }
    private int LoginAndGetPlayer(String prompt) {
        EnsurePlayerLogin(prompt);
        return PlayerService._currentPlayer.Id;
    }


    public void BeginQuizme() {
        int player1Id = LoginAndGetPlayer("PLAYER 1 LOGIN");;
        var player1 = PlayerService._currentPlayer;
        var quiz = _quizMenu.GetValidQuizInput();
        if (quiz == null) {
            ConsoleColour.printFailure("Quiz not found!");
            return;
        }
        System.out.println("QUIZ NAME: " + quiz.Name().toUpperCase());
        ConsoleColour.printSuccess("Quiz questions loaded successfully!!!");
        quiz = EnsureQuizQuestionsAreLoaded(quiz);
        int quiz1Id = quiz.Id();

        AppUtilities.PrintDefaultDotAnimation();
        AppUtilities.PressEnterToContinue();

        //player 2
        int player2Id = LoginAndGetPlayer("PLAYER 2 LOGIN");
        var player2 = PlayerService._currentPlayer;
        quiz = _quizMenu.GetValidQuizInput();
        if (quiz == null) {
            ConsoleColour.printFailure("Quiz not found!");
            return;
        }
        quiz = EnsureQuizQuestionsAreLoaded(quiz);
        int quiz2Id = quiz.Id();
        System.out.println("QUIZ NAME: " + quiz.Name().toUpperCase());
        ConsoleColour.printSuccess("Quiz questions loaded successfully!!!");
        var quizType = GetQuizTypeInput();
        int timer = GetValidTimeInput();
        //var game = _gameService.CreateGame(new GameRequest(player1Id, player2Id, quiz1Id, quiz2Id, false ,"ONGOING", quizType));
        System.out.println("LET THE QUIZ BEGIN!!!");
        AppUtilities.PressEnterToContinue();
        AppUtilities.PrintDotAnimation(20);
        //create game
        GameRequest currentGame = new GameRequest(player1Id, player2Id, quiz1Id, quiz2Id, false, "ONGOING", quizType);
        //begin quiz instant or delayed
        var gameId =  _gameService.CreateGame(currentGame);
        if (quizType == QuizType.INSTANT) {
            _quizMenu.ConductInstantGame(gameId, player1.Name, player2.Name, timer);
        } else {
            _quizMenu.ConductDelayedGame(gameId, player1.Name, player2.Name, timer);
        }
    }

    public int GetValidTimeInput() {
        int time = AppUtilities.getValidatedIntegerInput("Enter the no of seconds per question (min: 15): ");
        while (time < 5) {
            ConsoleColour.printFailure("Invalid no of seconds!");
            time = AppUtilities.getValidatedIntegerInput("Enter the no of seconds per question: ");
        }
        return time;
    }
    public QuizResponse EnsureQuizQuestionsAreLoaded(QuizResponse quiz) {
        while (!_quizMenu.HasLoadedQuestions(quiz.Id())) {
            ConsoleColour.printFailure("There are no loaded questions for this quiz!!!");
            quiz = _quizMenu.GetValidQuizInput();
            if (quiz == null)
                break;

        }
        return quiz;
    }

    public void EnsurePlayerLogin(String prompt) {
        System.out.println(prompt);
        var user = _playerMenu.Login();
        while (user == null) {
            System.out.println(prompt);
            user = _playerMenu.Login();
        }
    }

    public static QuizType GetQuizTypeInput() {
        String prompt = "Select:\n\t1----INSTANT\n\t2----DELAYED\n\n";
        
        int choice = AppUtilities.getValidatedIntegerInput(prompt + "Enter choice: ");
        while (!(choice > 0 && choice < 3)) {
            choice = AppUtilities.getValidatedIntegerInput(prompt + "Enter choice: ");
        }
        QuizType quizType = QuizType.INSTANT;
        if (choice == 1) {
            quizType = QuizType.INSTANT;
        }
        else {
            quizType = QuizType.DELAYED;
        }
        return quizType;
    }

    // public void PersistGameResult(int gameId, String result) {

    // }
    
}
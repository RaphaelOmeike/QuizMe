package com.example.Menus;

import java.time.Duration;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

import com.example.DTOs.Games.GameResponse;
import com.example.DTOs.PlayerAttemptOptions.PlayerAttemptOptionResponse;
import com.example.DTOs.PlayerAttemptQuestions.PlayerAttemptQuestionResponse;
import org.springframework.stereotype.Component;

import com.example.DTOs.Options.OptionResponse;
import com.example.DTOs.PlayerAttempts.PlayerAttemptRequest;
import com.example.DTOs.Questions.QuestionResponse;
import com.example.DTOs.Quizzes.QuizRequest;
import com.example.DTOs.Quizzes.QuizResponse;
import com.example.Services.Implementations.PlayerService;
import com.example.Services.Interfaces.IGameService;
import com.example.Services.Interfaces.IOptionService;
import com.example.Services.Interfaces.IPlayerAttemptService;
import com.example.Services.Interfaces.IQuestionService;
import com.example.Services.Interfaces.IQuizService;
import com.example.Utilities.AppUtilities;
import com.example.Utilities.ConsoleColour;

@Component
public class QuizMenu {

    private final IQuizService _quizService;
    private final IQuestionService _questionService;
    private final IOptionService _optionService;
    private final IGameService _gameService;
    private final PlayerAttemptMenu _playerAttemptMenu;


    private static int player2StopIndex = 0;

    public QuizMenu(IQuizService quizService, IQuestionService questionService, IOptionService optionService, IGameService gameService, PlayerAttemptMenu playerAttemptMenu) {
        _quizService = quizService;
        _questionService = questionService;
        _optionService = optionService;
        _gameService = gameService;
        _playerAttemptMenu = playerAttemptMenu;
    }

    public void CreateQuiz() {
        String name = AppUtilities.getStringInput("Enter quiz name: ");

        String fileName = AppUtilities.getOptionalStringInput("Enter quiz question file (optional): ");

        //String email = AppUtilities.getStringInput("Enter your email: ");
        fileName += ".txt";
        _quizService.CreateQuiz(new QuizRequest(name, fileName, PlayerService._currentPlayer.Id));
        ConsoleColour.printSuccess("Quiz created successfully!!");
    }

    public void LoadQuizQuestions() {
        var quiz = GetValidQuizInput();
        if (quiz == null) {
            ConsoleColour.printFailure("Quiz not found!");
            return;
        }
        var fileName = AppUtilities.getStringInput("Enter questions file name: ");
        _quizService.LoadQuizQuestions(quiz.Id(), fileName);
        // System.out.println("Questions loaded successfully");//considered
    }

    public QuizResponse GetValidQuizInput() {
        int userInput;
        QuizResponse quiz = null;
        boolean iscont = true;
        do {
            ViewCreatedQuizzes();
            userInput = AppUtilities.getValidatedIntegerInput("Enter id (0----to quit): ");
            if (userInput == 0)
                break;
            int quizId = AppUtilities.getTrueId(userInput);
            //there is need for GetQuizById method
            iscont = CheckQuizIsByPlayer(PlayerService._currentPlayer.Id, quizId);
            if (!iscont)
                ConsoleColour.printFailure("Invalid quiz ID! Enter valid quiz ID!!");
            else
                quiz = _quizService.GetQuizById(quizId);
        } while (!iscont);
        return quiz;
    }

    public boolean CheckQuizIsByPlayer(int playerId, int quizId) {
        var quizzes = _quizService.GetAllQuizzesByPlayer(playerId);
        for (var quiz : quizzes) {
            if (quiz.Id() == quizId)
                return true;
        }
        return false;
    }

    public void PreviewQuizQuestions() {
        var quiz = GetValidQuizInput();
        if (quiz == null) {
            ConsoleColour.printFailure("Quiz not found!");
            return;
        }
        System.out.println("QUIZ NAME: " + quiz.Name().toUpperCase());
        var questions = _questionService.GetAllQuestionsForQuiz(quiz.Id());
        if (questions.isEmpty()) {
            ConsoleColour.printFailure("No Questions Loaded for this quiz!!!");
            return;
        }
        String[] letts = new String[]{"A", "B", "C", "D", "E", "F", "G"};
        for (var i : questions) {
            System.out.println("Marks: " + i.Marks());
            System.out.println(i.Content());
            var options = _optionService.GetOptionsForQuestion(i.Id());
            int a = 0;
            for (var j : options) {
                System.out.println(letts[a] + " " + j.Content());
                a++;
            }

            String answer = _optionService.GetAnswerToQuestion(i.Id()).Content();
            System.out.println("Answer: " + answer + "\n\n");
        }
    }

    public boolean HasLoadedQuestions(int quizId) {
        return !_questionService.GetAllQuestionsForQuiz(quizId).isEmpty();
    }

    public void ViewCreatedQuizzes() {
        var playerId = PlayerService._currentPlayer.Id;
        var quizzes = _quizService.GetAllQuizzesByPlayer(playerId);
        System.out.println("Created Quizzes:");
        System.out.println("S/N\tName\t\tTotal marks");
        for (QuizResponse i : quizzes) {
            System.out.println(AppUtilities.incrementId(i.Id()) + "\t" + i.Name() + "\t" + i.TotalMarks());
        }
    }


    public void ConductInstantGame(int gameId, String player1Name, String player2Name, int timer) {
        var game = _gameService.GetGameById(gameId);
        var player1Questions = _questionService.GetAllQuestionsForQuiz(game.Player2QuizId());
        var player2Questions = _questionService.GetAllQuestionsForQuiz(game.Player1QuizId());

        var player1AttemptId = _playerAttemptMenu.CreatePlayerAttempt(new PlayerAttemptRequest(game.Player1Id(), game.Player2QuizId()));
        var player2AttemptId = _playerAttemptMenu.CreatePlayerAttempt(new PlayerAttemptRequest(game.Player2Id(), game.Player1QuizId()));

        System.out.println("");
        for (int i = 0; i < player1Questions.size(); i++) {
            PrintTurn(true, player1Name, player2Name);

            var question = player1Questions.get(i);
            var options = _optionService.GetOptionsForQuestion(question.Id());
            var selectedAnswer = PrintQuestionWithOptions(question, options, timer);
            boolean isCorrect;
            if (selectedAnswer == null) {
                ConsoleColour.printFailure("No answer provided!!");
                isCorrect = false;
            } else {
                isCorrect = MarkQuestion(question.Id(), selectedAnswer.Id());
            }
            int selectedOptionId = (selectedAnswer != null) ? selectedAnswer.Id() : -1;
            _playerAttemptMenu.RecordPlayerAttemptQuestion(player1AttemptId, question.Id(), options, selectedOptionId, isCorrect);
            // AppUtilities.PressEnterToContinue(); //i am thinking of adding this
            PrintInstantQuestionOutcome(isCorrect, player1Name);

            for (int j = player2StopIndex; j < player2Questions.size(); j++) {
                PrintTurn(false, player1Name, player2Name);

                question = player2Questions.get(j);
                options = _optionService.GetOptionsForQuestion(question.Id());
                selectedAnswer = PrintQuestionWithOptions(question, options, timer);
                if (selectedAnswer == null) {
                    ConsoleColour.printFailure("No answer provided!!");
                    isCorrect = false;
                } else {
                    isCorrect = MarkQuestion(question.Id(), selectedAnswer.Id());
                }
                selectedOptionId = (selectedAnswer != null) ? selectedAnswer.Id() : -1;
                _playerAttemptMenu.RecordPlayerAttemptQuestion(player2AttemptId, question.Id(), options, selectedOptionId, isCorrect);

                PrintInstantQuestionOutcome(isCorrect, player2Name);
                ++player2StopIndex;
                break;
            }
        }
        System.out.println("Quiz me OVER!!!!!");
        AppUtilities.PrintDotAnimation(22);
        AppUtilities.PressEnterToContinue();

        var player1Quiz = _quizService.GetQuizById(game.Player2QuizId());
        var player2Quiz = _quizService.GetQuizById(game.Player1QuizId());
        String result = _playerAttemptMenu.MarkPlayersQuiz(player1AttemptId, player2AttemptId, player1Name, player2Name, player1Quiz.TotalMarks(), player2Quiz.TotalMarks());

        _gameService.SetGameResult(game.Id(), result);
        _gameService.SetGamePlayerAttempts(game.Id(), player1AttemptId, player2AttemptId);
    }

    public void PrintTurn(boolean player1Turn, String player1Name, String player2Name) {
        String currentPlayer;
        if (player1Turn) {
            currentPlayer = player1Name;
        } else {
            currentPlayer = player2Name;
        }
        System.out.println(currentPlayer + ", it's your turn!!!\n");
        AppUtilities.PrintDefaultDotAnimation();
    }

    public static void PrintInstantQuestionOutcome(boolean isCorrect, String playerName) {
        if (isCorrect) {
            ConsoleColour.printSuccess(playerName + ", you are CORRECT!!!");
        } else {
            ConsoleColour.printFailure(playerName + ", you are WRONG!!!");
        }
        AppUtilities.PrintDefaultDotAnimation();
    }

//
//    public OptionResponse PrintQuestionWithOptions(QuestionResponse question, List<OptionResponse> options, int timer) {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        Scanner scanner = new Scanner(System.in); // Use local Scanner
//
//        ConsoleColour.printFailure("\nALERT!!!\nYou have " + timer + " seconds to answer each question:");
//        System.out.println(question.Content());
//
//        String letters = "ABCDEFG";
//        for (int i = 0; i < options.size(); i++) {
//            System.out.println(letters.charAt(i) + " " + options.get(i).Content());
//        }
//        String validLetters = letters.substring(0, options.size());
//
//        Future<String> future = executor.submit(() -> {
//            String input;
//            while (!Thread.currentThread().isInterrupted()) {
//                System.out.print("Enter answer/option: ");
//                if (!scanner.hasNextLine()) return null;
//                input = scanner.nextLine().trim().toUpperCase();
//
//                if (input.length() == 1 && validLetters.contains(input)) {
//                    return input;
//                } else {
//                    System.out.println("Invalid input. Try again:");
//                }
//            }
//            return null;
//        });
//
//        // Optional: print countdown WITHOUT interfering with input
//        Thread countdown = new Thread(() -> {
//            try {
//                for (int j = timer; j > 0; j--) {
//                    Thread.sleep(1000);
//                }
//            } catch (InterruptedException ignored) {
//            }
//        });
//        countdown.start();
//
//        String response = null;
//        try {
//            response = future.get(timer, TimeUnit.SECONDS);
//            countdown.interrupt();
//        } catch (TimeoutException e) {
//            System.out.println("\nTime's up!");
//            future.cancel(true);
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//
//        executor.shutdownNow();
//
//        if (response == null) {
//            ConsoleColour.printFailure("No answer selected!");
//            return null;
//        }
//
//        int index = validLetters.indexOf(response);
//        if (index == -1 || index >= options.size()) {
//            ConsoleColour.printFailure("Invalid answer!");
//            return null;
//        }
//
//        return options.get(index);
//    }

    public OptionResponse PrintQuestionWithOptions(QuestionResponse question, List<OptionResponse> options, int timer) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        ConsoleColour.printFailure("\nALERT!!!\nYou have " + timer + " seconds to answer each question:");
        System.out.println(question.Content());

        String letters = "ABCDEFG";
        for (int i = 0; i < options.size(); i++) {
            System.out.println(letters.charAt(i) + " " + options.get(i).Content());
        }
        String validLetters = letters.substring(0, options.size());

        // Use a dedicated Scanner (do not share it globally)
        Future<String> future = executor.submit(() -> {
            Scanner localScanner = new Scanner(System.in);
            String input;
            while (true) {
                System.out.print("Enter answer/option: ");
                if (!localScanner.hasNextLine()) return null;
                input = localScanner.nextLine().trim().toUpperCase();

                if (input.length() == 1 && validLetters.contains(input)) {
                    return input;
                } else {
                    ConsoleColour.printFailure("Invalid input. Try again:");
                }
            }
        });

        String response = null;
        try {
            response = future.get(timer, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            ConsoleColour.printFailure("\nTime's up, bro!");
            future.cancel(true);
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }

        executor.shutdownNow();

        if (response == null) {
            ConsoleColour.printFailure("No answer selected!");
            return null;
        }

        int index = validLetters.indexOf(response);
        if (index == -1 || index >= options.size()) {
            ConsoleColour.printFailure("Invalid answer!");
            return null;
        }

        return options.get(index);
    }




//    public OptionResponse PrintQuestionWithOptions(QuestionResponse question, List<OptionResponse> options, int timer) {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//
//        ConsoleColour.printFailure("\nALERT!!!\nYou have " + timer + " seconds to answer each question:");
//        System.out.println(question.Content());
//        String letters = "ABCDEFG";
//        for (int i = 0; i < options.size(); i++) {
//            System.out.println(letters.charAt(i) + " " + options.get(i).Content());
//        }
//        String actualLetters = letters.substring(0, options.size());
//        Future<String> future = executor.submit(() -> AppUtilities.getValidAnswer(actualLetters));
//
//        for (int j = timer; j > 0; j--) {
//            System.out.print("Time left: " + j + " seconds...Enter answer/option: \r");
//            try {
//                Thread.sleep(1000);
//                if (future.isDone()) break;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        String response = "";
//        try {
//            response = future.get(0, TimeUnit.MILLISECONDS); // try to get input
//
//        } catch (TimeoutException e) {
//            System.out.println("\nTime's up, bro!");
//            future.cancel(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        var optionIndex = actualLetters.indexOf(response);
//
//        if (optionIndex == -1) {
//            ConsoleColour.printFailure("No valid answer selected!");
//            return null;
//        }
//        executor.shutdownNow();
//        return options.get(optionIndex);
//    }

//    public static String getValidAnswer(String prompt, String actualLetters) {
//        String answer = AppUtilities.getOptionalStringInput("Enter answer/option: ").toUpperCase();
//
//        //get valid answer
//        while (!actualLetters.contains(answer) || answer.length() != 1) {
//            ConsoleColour.printFailure("Invalid input, try again.");
//            answer = AppUtilities.getOptionalStringInput("Re-enter answer/option: ").toUpperCase();
//        }
//        return answer;
//    }

    public boolean MarkQuestion(int questionId, int userOptionId) {
        var answer = _optionService.GetAnswerToQuestion(questionId);
        return answer.Id() == userOptionId;
    }

    // public void PrintQuestion()



    public void ConductDelayedGame(int gameId, String player1Name, String player2Name, int timer) {
        var game = _gameService.GetGameById(gameId);
        var player1Questions = _questionService.GetAllQuestionsForQuiz(game.Player2QuizId());
        var player2Questions = _questionService.GetAllQuestionsForQuiz(game.Player1QuizId());
        
        var player1AttemptId = _playerAttemptMenu.CreatePlayerAttempt(new PlayerAttemptRequest(game.Player1Id(), game.Player2QuizId()));
        var player2AttemptId = _playerAttemptMenu.CreatePlayerAttempt(new PlayerAttemptRequest(game.Player2Id(), game.Player1QuizId()));


        for (int i = 0; i < player1Questions.size(); i++) {
            PrintTurn(true, player1Name, player2Name);
            var question = player1Questions.get(i);
            var options = _optionService.GetOptionsForQuestion(question.Id());
            var selectedAnswer = PrintQuestionWithOptions(question, options, timer);
            boolean isCorrect;
            if (selectedAnswer == null) {
                ConsoleColour.printFailure("No answer provided!!");
                isCorrect = false;
            } else {
                isCorrect = MarkQuestion(question.Id(), selectedAnswer.Id());
            }
            //boolean isCorrect = MarkQuestion(question.Id(), selectedAnswer.Id());
            int selectedOptionId = (selectedAnswer != null) ? selectedAnswer.Id() : -1;
            _playerAttemptMenu.RecordPlayerAttemptQuestion(player1AttemptId, question.Id(), options, selectedOptionId, isCorrect);

        }
        for (int j = 0; j < player2Questions.size(); j++) {
            PrintTurn(false, player1Name, player2Name);
            var question = player2Questions.get(j);
            var options = _optionService.GetOptionsForQuestion(question.Id());
            var selectedAnswer = PrintQuestionWithOptions(question, options, timer);
            boolean isCorrect;
            if (selectedAnswer == null) {
                ConsoleColour.printFailure("No answer provided!!");
                isCorrect = false;
            } else {
                isCorrect = MarkQuestion(question.Id(), selectedAnswer.Id());
            }
            //boolean isCorrect = MarkQuestion(question.Id(), selectedAnswer.Id());
            int selectedOptionId = (selectedAnswer != null) ? selectedAnswer.Id() : -1;
            _playerAttemptMenu.RecordPlayerAttemptQuestion(player2AttemptId, question.Id(), options, selectedOptionId, isCorrect);
        }
        
        System.out.println("Quiz me OVER!!!!!");
        AppUtilities.PrintDotAnimation(22);
        AppUtilities.PressEnterToContinue();

        var player1Quiz = _quizService.GetQuizById(game.Player2QuizId());
        var player2Quiz = _quizService.GetQuizById(game.Player1QuizId());
        String result = _playerAttemptMenu.MarkPlayersQuiz(player1AttemptId, player2AttemptId, player1Name, player2Name, player1Quiz.TotalMarks(), player2Quiz.TotalMarks());
        
        _gameService.SetGameResult(game.Id(), result);
        _gameService.SetGamePlayerAttempts(game.Id(), player1AttemptId, player2AttemptId);
    }


    //work on playerattempt integration into the games entity

    public void ViewQuizmes() {
        var player = PlayerService._currentPlayer;
        if (player == null) {
            return;
        }
        var games = _gameService.GetGamesByPlayer(player.Id);
        System.out.println("QUIZ MEs BY " + player.Name + "!!");
        for (GameResponse game: games) {
            System.out.println("Id: " + AppUtilities.incrementId(game.Id()));
            System.out.println("Result:\n" + game.Result());
            System.out.println("Quiz mode: " + game.QuizMode());
            System.out.println();
        }
    }

    public void HandleQuizmeHistory() {
        var game = GetValidGameInput();
        if (game == null) {
            ConsoleColour.printFailure("Quiz ME game not found!!");
            return;
        }
        int playerAttemptId = DeterminePlayerAttempt(PlayerService._currentPlayer.Id, game);
        _playerAttemptMenu.DisplayPlayerAttempt(playerAttemptId);
    }

    public int DeterminePlayerAttempt(int playerId, GameResponse game) {
        if (game.Player1Id() == playerId) {
            return game.Player1AttemptId();
        }
        return game.Player2AttemptId();
    }

    public GameResponse GetValidGameInput() {
        int userInput;
        GameResponse game = null;
        boolean iscont = true;
        do {
            ViewQuizmes();
            userInput = AppUtilities.getValidatedIntegerInput("Enter id (0----to quit): ");
            if (userInput == 0)
                break;
            int gameId = AppUtilities.getTrueId(userInput);
            //there is need for GetQuizById method
            iscont = CheckGameIsByPlayer(PlayerService._currentPlayer.Id, gameId);
            if (!iscont)
                ConsoleColour.printFailure("Invalid game ID! Enter valid game ID!!");
            else
                game = _gameService.GetGameById(gameId);
        } while (!iscont);
        return game;
    }

    public boolean CheckGameIsByPlayer(int playerId, int gameId) {
        var games = _gameService.GetGamesByPlayer(playerId);

        for (GameResponse game: games) {
            if (game.Id() == gameId)
                return true;
        }
        return false;
    }

}

package com.example.Menus;

import java.util.List;

import com.example.DTOs.PlayerAttemptOptions.PlayerAttemptOptionResponse;
import com.example.DTOs.PlayerAttemptQuestions.PlayerAttemptQuestionResponse;
import com.example.DTOs.Questions.QuestionResponse;
import com.example.Models.PlayerAttemptQuestion;
import com.example.Services.Implementations.PlayerService;
import com.example.Services.Interfaces.*;
import org.springframework.stereotype.Component;

import com.example.DTOs.Options.OptionResponse;
import com.example.DTOs.PlayerAttemptOptions.PlayerAttemptOptionRequest;
import com.example.DTOs.PlayerAttemptQuestions.PlayerAttemptQuestionRequest;
import com.example.DTOs.PlayerAttempts.PlayerAttemptRequest;
import com.example.DTOs.PlayerAttempts.PlayerAttemptResponse;
import com.example.DTOs.Players.PlayerResponse;
import com.example.Utilities.AppUtilities;
import com.example.Utilities.ConsoleColour;

@Component
public class PlayerAttemptMenu {
    private final IPlayerAttemptQuestionService _playerAttemptQuestionService;
    private final IPlayerAttemptOptionService _playerAttemptOptionService;
    private final IPlayerAttemptService _playerAttemptService;
    private final IQuestionService _questionService;
    private final IOptionService _optionService;
    public PlayerAttemptMenu(IPlayerAttemptQuestionService playerAttemptQuestionService,
            IPlayerAttemptOptionService playerAttemptOptionService, IPlayerAttemptService playerAttemptService, IQuestionService questionService, IOptionService optionService) {
        _playerAttemptQuestionService = playerAttemptQuestionService;
        _playerAttemptOptionService = playerAttemptOptionService;
        _playerAttemptService = playerAttemptService;
        _questionService = questionService;
        _optionService = optionService;
    }
    public void RecordPlayerAttemptQuestion(int playerAttemptId, int questionId, List<OptionResponse> options, int selectedOptionId, boolean isCorrect) {
        var playerQuestion = _playerAttemptQuestionService.CreatePlayerAttemptQuestion(new PlayerAttemptQuestionRequest(playerAttemptId, questionId, isCorrect));
        for (var i : options) {
            boolean isChosen = false;
            if (i.Id() == selectedOptionId)
                isChosen = true;
            _playerAttemptOptionService.CreatePlayerAttemptOption(new PlayerAttemptOptionRequest(playerQuestion, i.Id(), isChosen));
        }
    }

    public int CreatePlayerAttempt(PlayerAttemptRequest request) {
        return _playerAttemptService.CreatePlayerAttempt(request);
    }

    public String MarkPlayersQuiz(int player1AttemptId, int player2AttemptId, String player1Name, String player2Name, float player1TotalMarks, float player2TotalMarks) {
        var player1Result = _playerAttemptService.MarkQuiz(player1AttemptId);

        
        var player2Result = _playerAttemptService.MarkQuiz(player2AttemptId);

        return DetermineWinner(player1Result, player2Result, player1Name, player2Name, player1TotalMarks, player2TotalMarks);

    }

    public String DetermineWinner(PlayerAttemptResponse player1, PlayerAttemptResponse player2, String player1Name, String player2Name, float player1TotalMarks, float player2TotalMarks) {
        float player1Score = GetPercentageScore(player1.MarksObtained(), player1TotalMarks);
        float player2Score = GetPercentageScore(player2.MarksObtained(), player2TotalMarks);

        String result = "";
        String player1Output = player1Name + ", your score: " + player1.MarksObtained() + "/" + player1TotalMarks + " = " + player1Score;
        result += player1Output + "\n";

//        changes made right here
        player1Output = player2Name + ", your score: " + player2.MarksObtained() + "/" + player2TotalMarks + " = " + player2Score;
        result += player1Output + "\n";

        if (player1Score > player2Score) {
            result += player1Name + " IS THE WINNER!!!!";
            ConsoleColour.printSuccess(result);
            ConsoleColour.printFailure(player2Name + " YOU CAN DO BETTER ON THE NEXT QUIZ MEE!!!!");
        }
        else if (player2Score > player1Score) {
            result += player2Name + " IS THE WINNER!!!!";
            ConsoleColour.printSuccess(result);
            ConsoleColour.printFailure(player1Name + " YOU CAN DO BETTER ON THE NEXT QUIZ MEE!!!!");
        }

        else {
            result += player1Name + " and " + player2Name + "!!!!!\nTHIS IS A TIE!!!!";
            ConsoleColour.printSuccess(result);
            ConsoleColour.printFailure("WE NEED A WINNER ON THE NEXT QUIZ MEE!!!!");
        }

        return result;
    }

    public float GetPercentageScore(float score, float total) {
        return score / total * 100;
    }


    public void DisplayPlayerAttempt(int playerAttemptId) {
        var playerAttempt = _playerAttemptService.GetPlayerAttemptById(playerAttemptId);

        if (playerAttempt == null) {
            ConsoleColour.printFailure("Error!!!!\nPlayer attempt not found!");
            return;
        }
        System.out.println(playerAttempt.MarksObtained());
        var playerAttemptQuestions = _playerAttemptQuestionService.GetPlayerAttemptQuestions(playerAttempt.Id());

        for (var i:  playerAttemptQuestions) {
            var options = _playerAttemptOptionService.GetPlayerAttemptOptions(i.Id());
            if (options == null) continue;
            PrintPlayerAttemptQuestionWithOptions(i, options);
        }
    }

    public void PrintPlayerAttemptQuestionWithOptions(PlayerAttemptQuestionResponse playerAttemptQuestion, List<PlayerAttemptOptionResponse> options) {
        var question = _questionService.GetQuestionById(playerAttemptQuestion.QuestionId());
        System.out.println("\nMarks: " + question.Marks());
        System.out.println(question.Content());
        String letters = "ABCDEFG";
        int selectedOptionId = 0;
        for (int i = 0; i < options.size(); i++) {
            var option = _optionService.GetOptionById(options.get(i).OptionId());
            System.out.println(letters.charAt(i) + " " + option.Content());
            if (options.get(i).IsChosen()) {
                selectedOptionId = option.Id();
            }
        }
        var answer = _optionService.GetAnswerToQuestion(question.Id());
        var selectedOption = _optionService.GetOptionById(selectedOptionId);
        boolean isCorrect;
        if (selectedOption != null) {
            System.out.println("Selected Option: " + selectedOption.Content());
            isCorrect = answer.Id() == selectedOption.Id();
        }
        else {
            ConsoleColour.printFailure("Selected Option: None selected");
            isCorrect = false;
        }
        System.out.println("Answer: " + answer.Content());
        QuizMenu.PrintInstantQuestionOutcome(isCorrect, PlayerService._currentPlayer.Name);
    }



}

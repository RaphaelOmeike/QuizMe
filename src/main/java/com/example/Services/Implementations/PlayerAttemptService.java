package com.example.Services.Implementations;

import org.springframework.stereotype.Service;

import com.example.DTOs.PlayerAttemptQuestions.PlayerAttemptQuestionResponse;
import com.example.DTOs.PlayerAttempts.PlayerAttemptRequest;
import com.example.DTOs.PlayerAttempts.PlayerAttemptResponse;
import com.example.Models.PlayerAttempt;
import com.example.Repository.Interfaces.IPlayerAttemptRepository;
import com.example.Repository.Interfaces.IQuestionRepository;
import com.example.Services.Interfaces.*;


@Service
public class PlayerAttemptService implements IPlayerAttemptService {

    private final IPlayerAttemptRepository _playerAttemptRepository;
    private final IPlayerAttemptQuestionService _playerAttemptQuestionService;
    private final IPlayerAttemptOptionService _playerAttemptOptionService;
    private final IOptionService _optionService;
    private final IQuestionRepository _questionRepository;

    public PlayerAttemptService(IPlayerAttemptRepository playerAttemptRepository,
                                IPlayerAttemptQuestionService playerAttemptQuestionService,
                                IPlayerAttemptOptionService playerAttemptOptionService,
                                IOptionService optionService,
                                IQuestionRepository questionRepository) {
        _playerAttemptRepository = playerAttemptRepository;
        _playerAttemptQuestionService = playerAttemptQuestionService;
        _playerAttemptOptionService = playerAttemptOptionService;
        _optionService = optionService;
        _questionRepository = questionRepository;
    }

    @Override
    public int CreatePlayerAttempt(PlayerAttemptRequest request) {
        PlayerAttempt playerAttempt = new PlayerAttempt(_playerAttemptRepository.GenerateId(), request.PlayerId(), request.QuizId(), 0);
        _playerAttemptRepository.Create(playerAttempt);
        return playerAttempt.Id;
    }

    @Override
    public PlayerAttemptResponse GetPlayerAttemptById(int playerAttemptId) {
        var playerAttempt = _playerAttemptRepository.GetById(playerAttemptId);
        if (playerAttempt == null)
            return null;

        return new PlayerAttemptResponse(playerAttempt.Id, playerAttempt.PlayerId, playerAttempt.QuizId, playerAttempt.MarksObtained);
    }

    @Override
    public void UpdateMarksObtained(int playerAttemptId, float marksObtained) {
        var playerAttempt = _playerAttemptRepository.GetById(playerAttemptId);
        if (playerAttempt == null)
            return;
        playerAttempt.MarksObtained =  marksObtained;
    }

//    @Override
//    public int StartQuiz(PlayerAttemptRequest request) {
//
//        return 0;
//    }
    @Override
    public PlayerAttemptResponse MarkQuiz(int playerAttemptId) {
        //continue
        var playerAttemptQuestions = _playerAttemptQuestionService.GetPlayerAttemptQuestions(playerAttemptId);
        if (playerAttemptQuestions == null) {
            System.out.println("Error!! No questions available for player attempt " + playerAttemptId);
            return null;
        }
        float totalMarksObtained = 0;
        for (PlayerAttemptQuestionResponse i : playerAttemptQuestions) {
            var chosenOption = _playerAttemptOptionService.GetChosenOptionForPlayerAttemptQuestion(i.Id());
            var correctOption = _optionService.GetAnswerToQuestion(i.QuestionId());
            var question = _questionRepository.GetById(i.QuestionId());
            if (chosenOption == null)
                continue;
            else if (chosenOption.OptionId() == correctOption.Id() && question != null) {
                totalMarksObtained += question.Marks;
            }
        }
        var playerAttempt = _playerAttemptRepository.GetById(playerAttemptId);
        playerAttempt.MarksObtained = totalMarksObtained;
        return new PlayerAttemptResponse(playerAttempt.Id, playerAttempt.PlayerId, playerAttempt.QuizId, playerAttempt.MarksObtained);
    }
}

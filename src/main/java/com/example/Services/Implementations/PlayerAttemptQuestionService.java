package com.example.Services.Implementations;

import com.example.DTOs.PlayerAttemptQuestions.PlayerAttemptQuestionRequest;
import com.example.DTOs.PlayerAttemptQuestions.PlayerAttemptQuestionResponse;
import com.example.Models.PlayerAttemptQuestion;
import com.example.Repository.Interfaces.IPlayerAttemptQuestionRepository;
import com.example.Services.Interfaces.IPlayerAttemptQuestionService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PlayerAttemptQuestionService implements IPlayerAttemptQuestionService {
    private final IPlayerAttemptQuestionRepository _playerAttemptQuestionRepository;

    public PlayerAttemptQuestionService(IPlayerAttemptQuestionRepository playerAttemptQuestionRepository) {
        _playerAttemptQuestionRepository = playerAttemptQuestionRepository;
    }

    @Override
    public int CreatePlayerAttemptQuestion(PlayerAttemptQuestionRequest request) {
        PlayerAttemptQuestion playerAttemptQuestion = new PlayerAttemptQuestion(_playerAttemptQuestionRepository.GenerateId(), request.PlayerAttemptId(), request.QuestionId(), request.IsCorrect());
        _playerAttemptQuestionRepository.Create(playerAttemptQuestion);
        return playerAttemptQuestion.Id;
    }

    @Override
    public PlayerAttemptQuestionResponse GetPlayerAttemptQuestionById(int playerAttemptQuestionId) {
        var playerAttemptQuestion = _playerAttemptQuestionRepository.GetById(playerAttemptQuestionId);
        if (playerAttemptQuestion == null)
            return null;

        return new PlayerAttemptQuestionResponse(playerAttemptQuestion.Id, playerAttemptQuestion.PlayerAttemptId, playerAttemptQuestion.QuestionId, playerAttemptQuestion.IsCorrect);
    }

    @Override
    public List<PlayerAttemptQuestionResponse> GetPlayerAttemptQuestions(int playerAttemptId) {
        var questions = _playerAttemptQuestionRepository.GetAll();
        List<PlayerAttemptQuestionResponse> playerQuestions = new ArrayList<>();
        for (PlayerAttemptQuestion i: questions)
        {
            if (i.PlayerAttemptId == playerAttemptId)
            {
                playerQuestions.add(new PlayerAttemptQuestionResponse(i.Id, i.PlayerAttemptId, i.QuestionId, i.IsCorrect));
            }
        }
        return playerQuestions;
    }
}

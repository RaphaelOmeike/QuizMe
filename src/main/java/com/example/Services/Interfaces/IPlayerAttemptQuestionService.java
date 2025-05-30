package com.example.Services.Interfaces;

import java.util.List;

import com.example.DTOs.PlayerAttemptQuestions.PlayerAttemptQuestionRequest;
import com.example.DTOs.PlayerAttemptQuestions.PlayerAttemptQuestionResponse;

public interface IPlayerAttemptQuestionService {
    int CreatePlayerAttemptQuestion(PlayerAttemptQuestionRequest request);

    PlayerAttemptQuestionResponse GetPlayerAttemptQuestionById(int playerAttemptQuestionId);
    List<PlayerAttemptQuestionResponse> GetPlayerAttemptQuestions(int playerAttemptId);
}

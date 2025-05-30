package com.example.Services.Interfaces;

import com.example.DTOs.PlayerAttempts.PlayerAttemptRequest;
import com.example.DTOs.PlayerAttempts.PlayerAttemptResponse;

public interface IPlayerAttemptService {
    int CreatePlayerAttempt(PlayerAttemptRequest request);
    PlayerAttemptResponse GetPlayerAttemptById(int playerAttemptId);
    void UpdateMarksObtained(int playerAttemptId, float marksObtained);
//    int StartQuiz(PlayerAttemptRequest request);
    PlayerAttemptResponse MarkQuiz(int playerAttemptId);

}

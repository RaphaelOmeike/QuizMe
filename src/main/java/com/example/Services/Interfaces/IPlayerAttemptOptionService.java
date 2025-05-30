package com.example.Services.Interfaces;

import java.util.List;

import com.example.DTOs.PlayerAttemptOptions.PlayerAttemptOptionRequest;
import com.example.DTOs.PlayerAttemptOptions.PlayerAttemptOptionResponse;

public interface IPlayerAttemptOptionService {
    int CreatePlayerAttemptOption(PlayerAttemptOptionRequest request);
    PlayerAttemptOptionResponse GetPlayerAttemptOptionById(int playerAttemptOptionId);
    List<PlayerAttemptOptionResponse> GetPlayerAttemptOptions(int playerAttemptQuestionId);
    PlayerAttemptOptionResponse GetChosenOptionForPlayerAttemptQuestion(int playerAttemptQuestionId);
}

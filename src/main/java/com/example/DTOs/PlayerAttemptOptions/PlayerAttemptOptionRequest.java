package com.example.DTOs.PlayerAttemptOptions;

public record PlayerAttemptOptionRequest(int PlayerAttemptQuestionId, int OptionId, boolean IsChosen) {

}
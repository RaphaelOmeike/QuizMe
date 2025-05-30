package com.example.DTOs.PlayerAttemptQuestions;

public record PlayerAttemptQuestionRequest(int PlayerAttemptId, int QuestionId, boolean IsCorrect) {

}
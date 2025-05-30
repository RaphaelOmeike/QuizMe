package com.example.DTOs.Games;

import com.example.Models.Enums.QuizType;

public record GameResponse(int Id, int Player1Id, int Player2Id, int Player1QuizId, int Player2QuizId, Boolean Player1Won, String Result, QuizType QuizMode, int Player1AttemptId, int Player2AttemptId) {

}

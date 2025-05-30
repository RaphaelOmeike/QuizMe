package com.example.DTOs.Games;

import com.example.Models.Enums.QuizType;

public record GameRequest(int Player1Id, int Player2Id, int Player1QuizId, int Player2QuizId, Boolean Player1Won, String Result, QuizType QuizMode) {
}



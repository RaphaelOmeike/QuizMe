package com.example.Models;

import com.example.Models.Common.BaseEntity;
import com.example.Models.Enums.QuizType;

public class Game extends BaseEntity {

    public int Player1Id;
    public int Player2Id;

    public int Player1QuizId;
    public int Player2QuizId;

    public Boolean Player1Won;

    public String Result;
    public QuizType QuizMode;

    public int Player1AttemptId;
    public int Player2AttemptId;

    public Game(int id, int player1Id, int player2Id, int player1QuizId, int player2QuizId, Boolean player1Won, String result, QuizType quizMode) {
        super(id);
        Player1Id = player1Id;
        Player2Id = player2Id;
        Player1QuizId = player1QuizId;
        Player2QuizId = player2QuizId;
        Player1Won = player1Won;
        Result = result;
        QuizMode = quizMode;
    }

}

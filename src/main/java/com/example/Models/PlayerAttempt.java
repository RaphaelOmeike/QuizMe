package com.example.Models;

import com.example.Models.Common.BaseEntity;

public class PlayerAttempt extends BaseEntity {
    public int PlayerId;
    //public Player Player;
    public int QuizId;
    //public Quiz Quiz;
    public float MarksObtained;
    public PlayerAttempt(int id, int playerId, int quizId, float marksObtained) {
        super(id);
        PlayerId = playerId;
        QuizId = quizId;
        MarksObtained = marksObtained;
    }
}

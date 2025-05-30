package com.example.Models;


import com.example.Models.Common.BaseEntity;

public class PlayerAttemptQuestion extends BaseEntity {
    public int PlayerAttemptId;
    //public PlayerAttempt PlayerAttempt;
    public int QuestionId;
    //public Question Question;
    public boolean IsCorrect;
    //public List<PlayerAttemptOption> PlayerAttemptOptions = new ArrayList<>();
    public PlayerAttemptQuestion(int id, int playerAttemptId, int questionId, boolean isCorrect) {
        super(id);
        PlayerAttemptId = playerAttemptId;
        QuestionId = questionId;
        IsCorrect = isCorrect;
    }
}

package com.example.Models;

import com.example.Models.Common.BaseEntity;

public class QuestionOption extends BaseEntity {
    public int QuestionId;
    //public Question Question;
    public int OptionId;
    //public Option Option;
    public boolean IsAnswer;
    public QuestionOption(int id, int questionId, int optionId, boolean isAnswer) {
        super(id);
        QuestionId = questionId;
        OptionId = optionId;
        IsAnswer = isAnswer;
    }
}

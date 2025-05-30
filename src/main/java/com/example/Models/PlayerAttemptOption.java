package com.example.Models;

import com.example.Models.Common.BaseEntity;
public class PlayerAttemptOption extends BaseEntity {
    public int PlayerAttemptQuestionId;
    //public PlayerAttemptQuestion PlayerAttemptQuestion;
    public int OptionId;
    //public Option Option;
    public boolean IsChosen;

    public PlayerAttemptOption(int id, int playerAttemptQuestionId, int optionId, boolean isChosen) {
        super(id);
        PlayerAttemptQuestionId = playerAttemptQuestionId;
        OptionId = optionId;
        IsChosen = isChosen;
    }
}

package com.example.Models;
import java.time.Duration;

import jdk.jfr.Timespan;

import com.example.Models.Common.BaseEntity;

public class Quiz extends BaseEntity {
    public String Name;
//    public Duration DurationPerQuestion;
    public float TotalMarks;
    public int PlayerId;
    //public List<Question> Questions = new ArrayList<>();
    public Quiz(int id, String name, float totalMarks, int playerId) {
        super(id);
        Name = name;
        TotalMarks = totalMarks;
        PlayerId = playerId;
    }
}

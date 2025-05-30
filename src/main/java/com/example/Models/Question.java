package com.example.Models;

import com.example.Models.Common.BaseEntity;

public class Question extends BaseEntity {
    public String Content;
    public int Marks;
    public int QuizId;
    //public Quiz Quiz;
    //public List<QuestionOption> QuestionOptions = new ArrayList<>();
    public Question(int id, String content, int marks, int quizId) {
        super(id);
        Content = content;
        Marks = marks;
        QuizId = quizId;
    }
}

package com.example.Services.Interfaces;

import java.util.List;

import com.example.DTOs.Questions.QuestionRequest;
import com.example.DTOs.Questions.QuestionResponse;

public interface IQuestionService {
    int CreateQuestion(QuestionRequest request);
    QuestionResponse GetQuestionById(int Id);
    List<QuestionResponse> GetAllQuestionsForQuiz(int quizId);
    List<Float> ReadQuestionsFromFile(String fileName, int quizId); //returns no of fquestions read
    void DeleteAllQuestionsForQuiz(int quizId);
}

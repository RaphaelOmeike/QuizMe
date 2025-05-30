package com.example.Services.Interfaces;

import java.util.List;

import com.example.DTOs.Quizzes.QuizRequest;
import com.example.DTOs.Quizzes.QuizResponse;

public interface IQuizService {
    int CreateQuiz(QuizRequest request);
    //void DeleteQuiz(int quizId);
    //void MarkQuiz();
    public QuizResponse GetQuizById(int Id);
    void LoadQuizQuestions(int quizId, String questionFileName);
    List<QuizResponse> GetAllQuizzesByPlayer(int playerId);
    void DeleteQuizQuestions(int quizId);
}

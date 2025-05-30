package com.example.Services.Interfaces;

import java.util.List;

import com.example.DTOs.QuestionOptions.QuestionOptionRequest;
import com.example.DTOs.QuestionOptions.QuestionOptionResponse;

public interface IQuestionOptionService {
    int CreateQuestionOption(QuestionOptionRequest request);
    QuestionOptionResponse GetQuestionOption(int questionOptionId);
    List<QuestionOptionResponse> GetQuestionOptionsForQuestion(int questionId);
    boolean CheckOptionUsedByOtherQuestions(int optionId);
    void DeleteQuestionOptionsForQuestion(int questionId);
}

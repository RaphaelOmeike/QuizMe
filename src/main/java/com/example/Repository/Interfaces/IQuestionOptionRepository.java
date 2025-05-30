package com.example.Repository.Interfaces;

import java.util.List;

import com.example.Models.QuestionOption;

public interface IQuestionOptionRepository {
    void Create(QuestionOption questionOption);
    QuestionOption GetById(int id);
    List<QuestionOption> GetAll();
    void Update(QuestionOption questionOption);
    void Delete(QuestionOption questionOption);
    int GenerateId();
    void WriteToFile(QuestionOption questionOption);
    void ReadFromFile();
    void RefreshFile();
}

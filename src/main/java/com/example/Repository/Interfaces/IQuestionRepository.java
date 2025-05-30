package com.example.Repository.Interfaces;

import java.util.List;

import com.example.Models.Question;

public interface IQuestionRepository {
    void Create(Question question);
    Question GetById(int id);
    List<Question> GetAll();
    void Update(Question question);
    void Delete(Question question);
    int GenerateId();
    
    void WriteToFile(Question question);
    void ReadFromFile();
    void RefreshFile();
}

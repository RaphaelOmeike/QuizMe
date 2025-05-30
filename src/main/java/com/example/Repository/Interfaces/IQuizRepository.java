package com.example.Repository.Interfaces;

import java.util.List;

import com.example.Models.Quiz;

public interface IQuizRepository {
    void Create(Quiz quiz);
    Quiz GetById(int id);
    List<Quiz> GetAll();
    void Update(Quiz quiz);
    void Delete(Quiz quiz);
    int GenerateId();
    void WriteToFile(Quiz quiz);
    void ReadFromFile();
    void RefreshFile();
}

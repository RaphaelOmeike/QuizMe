package com.example.Repository.Interfaces;

import java.util.List;

import com.example.Models.PlayerAttemptQuestion;

public interface IPlayerAttemptQuestionRepository {
    void Create(PlayerAttemptQuestion playerAttemptQuestion);
    PlayerAttemptQuestion GetById(int id);
    List<PlayerAttemptQuestion> GetAll();
    void Update(PlayerAttemptQuestion playerAttemptQuestion);
    void Delete(PlayerAttemptQuestion playerAttemptQuestion);
    int GenerateId();
    void WriteToFile(PlayerAttemptQuestion playerAttemptQuestion);
    void ReadFromFile();
    void RefreshFile();
}

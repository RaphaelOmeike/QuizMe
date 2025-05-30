package com.example.Repository.Interfaces;

import java.util.List;

import com.example.Models.PlayerAttempt;

public interface IPlayerAttemptRepository {
    void Create(PlayerAttempt playerAttempt);
    PlayerAttempt GetById(int id);
    List<PlayerAttempt> GetAll();
    void Update(PlayerAttempt playerAttempt);
    void Delete(PlayerAttempt playerAttempt);
    int GenerateId();
    void WriteToFile(PlayerAttempt playerAttempt);
    void ReadFromFile();
    void RefreshFile();
}

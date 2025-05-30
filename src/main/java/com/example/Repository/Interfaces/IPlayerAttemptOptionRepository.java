package com.example.Repository.Interfaces;

import java.util.List;

import com.example.Models.PlayerAttemptOption;

public interface IPlayerAttemptOptionRepository {
    void Create(PlayerAttemptOption playerAttemptOption);
    PlayerAttemptOption GetById(int id);
    List<PlayerAttemptOption> GetAll();
    int GenerateId();
    void Update(PlayerAttemptOption playerAttemptOption);
    void Delete(PlayerAttemptOption playerAttemptOption);
    void WriteToFile(PlayerAttemptOption playerAttemptOption);
    void ReadFromFile();
    void RefreshFile();
}

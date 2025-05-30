package com.example.Repository.Interfaces;

import java.util.List;

import com.example.Models.Game;

public interface IGameRepository {
    void Create(Game game);
    Game GetById(int id);
    List<Game> GetAll();
    void Update(Game game);
    void Delete(Game game);
    int GenerateId();

    void WriteToFile(Game game);
    void ReadFromFile();
    void RefreshFile();
}

package com.example.Repository.Interfaces;
import java.util.List;

import com.example.Models.Player;

public interface IPlayerRepository {
    void Create(Player player);
    Player GetById(int id);
    Player GetByEmail(String email);
    List<Player> GetAll();
    void Update(Player player);
    void Delete(Player player);
    int GenerateId();
    void WriteToFile(Player player);
    void ReadFromFile();
    void RefreshFile();
}

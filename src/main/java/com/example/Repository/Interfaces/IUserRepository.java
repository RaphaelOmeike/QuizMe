package com.example.Repository.Interfaces;

import java.util.List;

import com.example.Models.User;

public interface IUserRepository {
    void Create(User user);
    User GetById(int id);
    User GetByEmail(String email);
    User GetByUsername(String username);
    int GenerateId();
    List<User> GetAll();
    void Update(User user);
    void Delete(User user);
    void WriteToFile(User user);
    void ReadFromFile();
    void RefreshFile();
}

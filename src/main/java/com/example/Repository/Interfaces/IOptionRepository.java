package com.example.Repository.Interfaces;

import java.util.List;

import com.example.Models.Option;

public interface IOptionRepository {
    void Create(Option option);
    Option GetById(int id);
    List<Option> GetAll();
    void Update(Option option);
    void Delete(Option option);
    int GenerateId();
    Option GetByName(String name);
    void WriteToFile(Option option);
    void ReadFromFile();
    void RefreshFile();
}

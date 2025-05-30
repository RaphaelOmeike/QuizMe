package com.example.Repository.Implementations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Repository;

import com.example.Constants.FileNames;
import com.example.Data.QuizMeContext;
import com.example.Models.PlayerAttempt;
import com.example.Repository.Interfaces.IPlayerAttemptRepository;
import com.google.gson.Gson;

@Repository
public final class PlayerAttemptRepository implements IPlayerAttemptRepository {
    String FilePath = FileNames.BasePath + "\\" + FileNames.PlayerAttempts;
    List<PlayerAttempt> PlayerAttempts = QuizMeContext.PlayerAttempts;
    public PlayerAttemptRepository() {
        ReadFromFile();
    }
    @Override
    public void Create(PlayerAttempt playerAttempt) {
        PlayerAttempts.add(playerAttempt);
        WriteToFile(playerAttempt);
    }

    @Override
    public PlayerAttempt GetById(int id) {
        for (PlayerAttempt i : PlayerAttempts) {
            if (i.Id == id) {
                return i;
            }
        }
        return null;
    }

    @Override
    public List<PlayerAttempt> GetAll() {
        return PlayerAttempts;
    }

    @Override
    public void Update(PlayerAttempt playerAttempt) {
        PlayerAttempt playerAttemptExisting = GetById(playerAttempt.Id);
        if (playerAttemptExisting == null)
            return;
        playerAttemptExisting.PlayerId = playerAttempt.PlayerId;
        //playerAttemptExisting.Player = playerAttempt.Player;
        playerAttemptExisting.QuizId = playerAttempt.QuizId;
        //playerAttemptExisting.Quiz = playerAttempt.Quiz;
        playerAttemptExisting.MarksObtained = playerAttempt.MarksObtained;
        RefreshFile();
    }
    @Override
    public void Delete(PlayerAttempt playerAttempt) {
        PlayerAttempts.remove(playerAttempt);
        RefreshFile();
    }

    @Override
    public int GenerateId() {
        return PlayerAttempts.size() + 1;
    }

    @Override
    public void WriteToFile(PlayerAttempt playerAttempt) {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;

            try (FileWriter outputFile = new FileWriter(file, true)) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(playerAttempt);

                if (fileIsEmpty) {
                    outputFile.write(jsonString);
                } else {
                    outputFile.write("\n" + jsonString);
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while writing playerAttempts to file.");
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void ReadFromFile() {
        File myFile = new File(FilePath);
        if (!myFile.exists()){
            System.out.println("PlayerAttempts file does not exist.");
            return;
        }
        try (Scanner inputFile = new Scanner(myFile)) {
            while (inputFile.hasNext()) {
                Gson gson = new Gson();
                String json = inputFile.nextLine();
                if (json.isEmpty()) continue;
                PlayerAttempt playerAttempt = gson.fromJson(json, PlayerAttempt.class);
                PlayerAttempts.add(playerAttempt);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading playerAttempts from file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void RefreshFile() {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;
            try (FileWriter outputFile = new FileWriter(file)) {
                for (PlayerAttempt i : PlayerAttempts) {
                    Gson gson = new Gson();

                    String jsonString = gson.toJson(i);
                    if (fileIsEmpty) {
                        outputFile.write(jsonString);
                        fileIsEmpty = false;
                    } else {
                        outputFile.write("\n" + jsonString);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing playerAttempts to file.");
            System.out.println(e.getMessage());
        }
    }
}

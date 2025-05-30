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
import com.example.Models.PlayerAttemptOption;
import com.example.Repository.Interfaces.IPlayerAttemptOptionRepository;
import com.google.gson.Gson;

@Repository
public final class PlayerAttemptOptionRepository implements IPlayerAttemptOptionRepository{
    String FilePath = FileNames.BasePath + "\\" + FileNames.PlayerAttemptOptions;
    List<PlayerAttemptOption> PlayerAttemptOptions = QuizMeContext.PlayerAttemptOptions;
    public PlayerAttemptOptionRepository() {
        ReadFromFile();
    }
    @Override
    public void Create(PlayerAttemptOption playerAttemptOption) {
        PlayerAttemptOptions.add(playerAttemptOption);
        WriteToFile(playerAttemptOption);
    }

    @Override
    public PlayerAttemptOption GetById(int id) {
        for (PlayerAttemptOption i : PlayerAttemptOptions) {
            if (i.Id == id) {
                return i;
            }
        }
        return null;
    }

    @Override
    public List<PlayerAttemptOption> GetAll() {
        return PlayerAttemptOptions;
    }

    @Override
    public void Update(PlayerAttemptOption playerAttemptOption) {
        PlayerAttemptOption playerAttemptOptionExisting = GetById(playerAttemptOption.Id);
        if (playerAttemptOptionExisting == null)
            return;
        playerAttemptOption.PlayerAttemptQuestionId = playerAttemptOptionExisting.PlayerAttemptQuestionId;
        // playerAttemptOption.PlayerAttemptQuestion = playerAttemptOptionExisting.PlayerAttemptQuestion;
        playerAttemptOption.OptionId = playerAttemptOptionExisting.OptionId;
        // playerAttemptOption.Option = playerAttemptOptionExisting.Option;
        playerAttemptOption.IsChosen = playerAttemptOptionExisting.IsChosen;
        RefreshFile();
    }

    @Override
    public void Delete(PlayerAttemptOption playerAttemptOption) {
        PlayerAttemptOptions.remove(playerAttemptOption);
        RefreshFile();
    }

    @Override
    public void WriteToFile(PlayerAttemptOption playerAttemptOption) {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;

            try (FileWriter outputFile = new FileWriter(file, true)) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(playerAttemptOption);

                if (fileIsEmpty) {
                    outputFile.write(jsonString);
                } else {
                    outputFile.write("\n" + jsonString);
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while writing playerAttemptOptions to file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void ReadFromFile() {
        File myFile = new File(FilePath);
        if (!myFile.exists()){
            System.out.println("PlayerAttemptOptions file does not exist.");
            return;
        }
        try (Scanner inputFile = new Scanner(myFile)) {
            while (inputFile.hasNext()) {
                Gson gson = new Gson();
                String json = inputFile.nextLine();
                if (json.isEmpty()) continue;
                PlayerAttemptOption playerAttemptOption = gson.fromJson(json, PlayerAttemptOption.class);
                PlayerAttemptOptions.add(playerAttemptOption);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading playerAttemptOptions from file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void RefreshFile() {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;
            try (FileWriter outputFile = new FileWriter(file)) {
                for (PlayerAttemptOption i : PlayerAttemptOptions) {
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
            System.out.println("An error occurred while writing playerAttemptOptions to file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int GenerateId() {
        return PlayerAttemptOptions.size() + 1;
    }

}

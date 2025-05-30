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
import com.example.Models.Player;
import com.example.Repository.Interfaces.IPlayerRepository;
import com.google.gson.Gson;

@Repository
public final class PlayerRepository implements IPlayerRepository{
    String FilePath = FileNames.BasePath + "\\" + FileNames.Players;
    List<Player> Players = QuizMeContext.Players;
    public PlayerRepository() {
        ReadFromFile();
    }
    @Override
    public void Create(Player player) {
        Players.add(player);
        WriteToFile(player);
    }

    @Override
    public Player GetById(int id) {
        for (Player i : Players) {
            if (i.Id == id) {
                return i;
            }
        }
        return null;
    }

    @Override
    public Player GetByEmail(String email) {
        for (Player i : Players) {
            if (i.Email.equals(email)) {
                return i;
            }
        }
        return null;
    }

    @Override
    public List<Player> GetAll() {
        return Players;
    }

    @Override
    public void Update(Player player) {
        Player playerExisting = GetById(player.Id);
        if (playerExisting == null)
            return;
        playerExisting.Name = player.Name;
        playerExisting.Email = player.Email;
        playerExisting.Gender = player.Gender;
        //playerExisting.Quizzes = player.Quizzes;
        RefreshFile();
    }
    @Override
    public void Delete(Player player) {
        Players.remove(player);
        RefreshFile();
    }

    @Override
    public int GenerateId() {
        return Players.size() + 1;
    }

    @Override
    public void WriteToFile(Player player) {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;

            try (FileWriter outputFile = new FileWriter(file, true)) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(player);

                if (fileIsEmpty) {
                    outputFile.write(jsonString);
                } else {
                    outputFile.write("\n" + jsonString);
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while writing players to file.");
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void ReadFromFile() {
        File myFile = new File(FilePath);
        if (!myFile.exists()){
            System.out.println("Players file does not exist.");
            return;
        }
        try (Scanner inputFile = new Scanner(myFile)) {
            while (inputFile.hasNext()) {
                Gson gson = new Gson();
                String json = inputFile.nextLine();
                if (json.isEmpty()) continue;
                Player player = gson.fromJson(json, Player.class);
                Players.add(player);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading players from file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void RefreshFile() {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;
            try (FileWriter outputFile = new FileWriter(file)) {
                for (Player i : Players) {
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
            System.out.println("An error occurred while writing players to file.");
            System.out.println(e.getMessage());
        }
    }
}

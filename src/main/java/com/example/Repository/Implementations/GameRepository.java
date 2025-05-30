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
import com.example.Models.Game;
import com.example.Repository.Interfaces.IGameRepository;
import com.google.gson.Gson;

@Repository
public final class GameRepository implements IGameRepository {
    String FilePath = FileNames.BasePath + "\\" + FileNames.Games;
    List<Game> Games = QuizMeContext.Games;
    public GameRepository() {
        ReadFromFile();
    }
    @Override
    public void Create(Game game) {
        Games.add(game);
        WriteToFile(game);
    }

    @Override
    public Game GetById(int id) {
        for (Game i : Games) {
            if (i.Id == id) {
                return i;
            }
        }
        return null;
    }
    @Override
    public List<Game> GetAll() {
        return Games;
    }

    @Override
    public void Update(Game game) {
        Game gameExisting = GetById(game.Id);
        if (gameExisting == null)
            return;
        
        gameExisting.Player1Id = game.Player1Id;
        gameExisting.Player2Id = game.Player2Id;
        gameExisting.Player1QuizId = game.Player1QuizId;
        gameExisting.Player2QuizId = game.Player2QuizId;
        gameExisting.Player1Won = game.Player1Won;
        gameExisting.Result = game.Result;
        //gameExisting.QuestionGames = game.QuestionGames;
        RefreshFile();
    }
    @Override
    public void Delete(Game game) {
        Games.remove(game);
        RefreshFile();
    }

    @Override
    public void WriteToFile(Game game) {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;

            try (FileWriter outputFile = new FileWriter(file, true)) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(game);

                if (fileIsEmpty) {
                    outputFile.write(jsonString);
                } else {
                    outputFile.write("\n" + jsonString);
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while writing games to file.");
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void ReadFromFile() {
        File myFile = new File(FilePath);
        if (!myFile.exists()){
            System.out.println("Games file does not exist.");
            return;
        }
        try (Scanner inputFile = new Scanner(myFile)) {
            while (inputFile.hasNext()) {
                Gson gson = new Gson();
                String json = inputFile.nextLine();
                if (json.isEmpty()) continue;
                Game game = gson.fromJson(json, Game.class);
                Games.add(game);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading games from file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void RefreshFile() {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;

            try (FileWriter outputFile = new FileWriter(file)) {
                for (Game i : Games) {
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
            System.out.println("An error occurred while writing games to file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int GenerateId() {
        return Games.size() + 1;
    }
}

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
import com.example.Models.PlayerAttemptQuestion;
import com.example.Repository.Interfaces.IPlayerAttemptQuestionRepository;
import com.google.gson.Gson;

@Repository
public final class PlayerAttemptQuestionRepository implements IPlayerAttemptQuestionRepository {
    String FilePath = FileNames.BasePath + "\\" + FileNames.PlayerAttemptQuestions;
    List<PlayerAttemptQuestion> PlayerAttemptQuestions = QuizMeContext.PlayerAttemptQuestions;
    public PlayerAttemptQuestionRepository() {
        ReadFromFile();
    }
    @Override
    public void Create(PlayerAttemptQuestion playerAttemptQuestion) {
        PlayerAttemptQuestions.add(playerAttemptQuestion);
        WriteToFile(playerAttemptQuestion);
    }

    @Override
    public PlayerAttemptQuestion GetById(int id) {
        for (PlayerAttemptQuestion i : PlayerAttemptQuestions) {
            if (i.Id == id) {
                return i;
            }
        }
        return null;
    }

    @Override
    public List<PlayerAttemptQuestion> GetAll() {
        return PlayerAttemptQuestions;
    }

    @Override
    public void Update(PlayerAttemptQuestion playerAttemptQuestion) {
        PlayerAttemptQuestion playerAttemptQuestionExisting = GetById(playerAttemptQuestion.Id);
        if (playerAttemptQuestionExisting == null)
            return;
        playerAttemptQuestionExisting.PlayerAttemptId = playerAttemptQuestion.PlayerAttemptId;
        //playerAttemptQuestionExisting.PlayerAttempt = playerAttemptQuestion.PlayerAttempt;
        playerAttemptQuestionExisting.QuestionId = playerAttemptQuestion.QuestionId;
        // playerAttemptQuestionExisting.Question = playerAttemptQuestion.Question;
        playerAttemptQuestionExisting.IsCorrect = playerAttemptQuestion.IsCorrect;
        // playerAttemptQuestionExisting.PlayerAttemptOptions = playerAttemptQuestion.PlayerAttemptOptions;
        RefreshFile();
    }
    @Override
    public void Delete(PlayerAttemptQuestion playerAttemptQuestion) {
        PlayerAttemptQuestions.remove(playerAttemptQuestion);
        RefreshFile();
    }

    @Override
    public void WriteToFile(PlayerAttemptQuestion playerAttemptQuestion) {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;

            try (FileWriter outputFile = new FileWriter(file, true)) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(playerAttemptQuestion);

                if (fileIsEmpty) {
                    outputFile.write(jsonString);
                } else {
                    outputFile.write("\n" + jsonString);
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while writing playerAttemptQuestions to file.");
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void ReadFromFile() {
        File myFile = new File(FilePath);
        if (!myFile.exists()){
            System.out.println("PlayerAttemptQuestions file does not exist.");
            return;
        }
        try (Scanner inputFile = new Scanner(myFile)) {
            while (inputFile.hasNext()) {
                Gson gson = new Gson();
                String json = inputFile.nextLine();
                if (json.isEmpty()) continue;
                PlayerAttemptQuestion playerAttemptQuestion = gson.fromJson(json, PlayerAttemptQuestion.class);
                PlayerAttemptQuestions.add(playerAttemptQuestion);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading playerAttemptQuestions from file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void RefreshFile() {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;
            try (FileWriter outputFile = new FileWriter(file)) {
                for (PlayerAttemptQuestion i : PlayerAttemptQuestions) {
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
            System.out.println("An error occurred while writing playerAttemptQuestions to file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int GenerateId() {
        return PlayerAttemptQuestions.size() + 1;
    }
}

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
import com.example.Models.QuestionOption;
import com.example.Repository.Interfaces.IQuestionOptionRepository;
import com.google.gson.Gson;

@Repository
public final class QuestionOptionRepository implements IQuestionOptionRepository {
    String FilePath = FileNames.BasePath + "\\" + FileNames.QuestionOptions;
    List<QuestionOption> QuestionOptions = QuizMeContext.QuestionOptions;
    public QuestionOptionRepository() {
        ReadFromFile();
    }
    @Override
    public void Create(QuestionOption questionOption) {
        QuestionOptions.add(questionOption);
        WriteToFile(questionOption);
    }

    @Override
    public QuestionOption GetById(int id) {
        for (QuestionOption i : QuestionOptions) {
            if (i.Id == id) {
                return i;
            }
        }
        return null;
    }

    @Override
    public List<QuestionOption> GetAll() {
        return QuestionOptions;
    }

    @Override
    public void Update(QuestionOption questionOption) {
        QuestionOption questionOptionExisting = GetById(questionOption.Id);
        if (questionOptionExisting == null)
            return;
        questionOptionExisting.QuestionId = questionOption.QuestionId;
        //questionOptionExisting.Question = questionOption.Question;
        questionOptionExisting.OptionId = questionOption.OptionId;
        //questionOptionExisting.Option = questionOption.Option;
        questionOptionExisting.IsAnswer = questionOption.IsAnswer;
        RefreshFile();
    }
    @Override
    public void Delete(QuestionOption questionOption) {
        QuestionOptions.remove(questionOption);
        RefreshFile();
    }

    @Override
    public void WriteToFile(QuestionOption questionOption) {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;

            try (FileWriter outputFile = new FileWriter(file, true)) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(questionOption);

                if (fileIsEmpty) {
                    outputFile.write(jsonString);
                } else {
                    outputFile.write("\n" + jsonString);
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while writing questionOptions to file.");
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void ReadFromFile() {
        File myFile = new File(FilePath);
        if (!myFile.exists()){
            System.out.println("QuestionOptions file does not exist.");
            return;
        }
        try (Scanner inputFile = new Scanner(myFile)) {
            while (inputFile.hasNext()) {
                Gson gson = new Gson();
                String json = inputFile.nextLine();
                if (json.isEmpty()) continue;
                QuestionOption questionOption = gson.fromJson(json, QuestionOption.class);
                QuestionOptions.add(questionOption);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading questionOptions from file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void RefreshFile() {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;
            try (FileWriter outputFile = new FileWriter(file)) {
                for (QuestionOption i : QuestionOptions) {
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
            System.out.println("An error occurred while writing questionOptions to file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int GenerateId() {
        return QuestionOptions.size() + 1;
    }
}

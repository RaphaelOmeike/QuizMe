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
import com.example.Models.Question;
import com.example.Repository.Interfaces.IQuestionRepository;
import com.google.gson.Gson;

@Repository
public final class QuestionRepository implements IQuestionRepository {
    String FilePath = FileNames.BasePath + "\\" + FileNames.Questions;
    List<Question> Questions = QuizMeContext.Questions;
    public QuestionRepository() {
        ReadFromFile();
    }
    @Override
    public void Create(Question question) {
        Questions.add(question);
        WriteToFile(question);
    }

    @Override
    public Question GetById(int id) {
        for (Question i : Questions) {
            if (i.Id == id) {
                return i;
            }
        }
        return null;
    }

    @Override
    public List<Question> GetAll() {
        return Questions;
    }

    @Override
    public void Update(Question question) {
        Question questionExisting = GetById(question.Id);
        if (questionExisting == null)
            return;
        questionExisting.Content = question.Content;
        questionExisting.QuizId = question.QuizId;
        // questionExisting.Quiz = question.Quiz;
        // questionExisting.QuestionOptions = question.QuestionOptions;
        RefreshFile();
    }
    @Override
    public void Delete(Question question) {
        Questions.remove(question);
        RefreshFile();
    }

    @Override
    public void WriteToFile(Question question) {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;

            try (FileWriter outputFile = new FileWriter(file, true)) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(question);

                if (fileIsEmpty) {
                    outputFile.write(jsonString);
                } else {
                    outputFile.write("\n" + jsonString);
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while writing questions to file.");
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void ReadFromFile() {
        File myFile = new File(FilePath);
        if (!myFile.exists()){
            System.out.println("Questions file does not exist.");
            return;
        }
        try (Scanner inputFile = new Scanner(myFile)) {
            while (inputFile.hasNext()) {
                Gson gson = new Gson();
                String json = inputFile.nextLine();
                if (json.isEmpty()) continue;
                Question question = gson.fromJson(json, Question.class);
                Questions.add(question);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading questions from file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void RefreshFile() {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;
            try (FileWriter outputFile = new FileWriter(file)) {
                for (Question i : Questions) {
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
            System.out.println("An error occurred while writing questions to file.");
            System.out.println(e.getMessage());
        }
    }
    @Override
    public int GenerateId() {
        return Questions.size() + 1;
    }
}

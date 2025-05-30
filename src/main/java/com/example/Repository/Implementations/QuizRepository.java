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
import com.example.Models.Quiz;
import com.example.Repository.Interfaces.IQuizRepository;
import com.google.gson.Gson;


@Repository
public final class QuizRepository implements IQuizRepository {
    String FilePath = FileNames.BasePath + "\\" + FileNames.Quizzes;
    List<Quiz> Quizzes = QuizMeContext.Quizzes;
    public QuizRepository() {
        ReadFromFile();
    }
    @Override
    public void Create(Quiz quiz) {
        Quizzes.add(quiz);
        WriteToFile(quiz);
    }

    @Override
    public Quiz GetById(int id) {
        for (Quiz i : Quizzes) {
            if (i.Id == id) {
                return i;
            }
        }
        return null;
    }

    @Override
    public List<Quiz> GetAll() {
        return Quizzes;
    }

    @Override
    public void Update(Quiz quiz) {
        Quiz quizExisting = GetById(quiz.Id);
        if (quizExisting == null)
            return;
        quizExisting.Name = quiz.Name;
//        quizExisting.DurationPerQuestion = quiz.DurationPerQuestion;
        quizExisting.TotalMarks = quiz.TotalMarks;
        quizExisting.PlayerId = quiz.PlayerId;
        // quizExisting.Questions = quiz.Questions;
        RefreshFile();
    }
    @Override
    public void Delete(Quiz quiz) {
        Quizzes.remove(quiz);
        RefreshFile();
    }

    @Override
    public void WriteToFile(Quiz quiz) {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;

            try (FileWriter outputFile = new FileWriter(file, true)) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(quiz);

                if (fileIsEmpty) {
                    outputFile.write(jsonString);
                } else {
                    outputFile.write("\n" + jsonString);
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while writing quizzes to file.");
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void ReadFromFile() {
        File myFile = new File(FilePath);
        if (!myFile.exists()){
            System.out.println("Quizzes file does not exist.");
            return;
        }
        try (Scanner inputFile = new Scanner(myFile)) {
            while (inputFile.hasNext()) {
                Gson gson = new Gson();
                String json = inputFile.nextLine();
                if (json.isEmpty()) continue;
                Quiz quiz = gson.fromJson(json, Quiz.class);
                Quizzes.add(quiz);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading quizzes from file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void RefreshFile() {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;
            try (FileWriter outputFile = new FileWriter(file)) {
                for (Quiz i : Quizzes) {
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
            System.out.println("An error occurred while writing quizzes to file.");
            System.out.println(e.getMessage());
        }
    }
    @Override
    public int GenerateId() {
        return Quizzes.size() + 1;
    }
}

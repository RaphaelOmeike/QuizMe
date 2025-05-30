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
import com.example.Models.Option;
import com.example.Repository.Interfaces.IOptionRepository;
import com.google.gson.Gson;

@Repository
public final class OptionRepository implements IOptionRepository {
    String FilePath = FileNames.BasePath + "\\" + FileNames.Options;
    List<Option> Options = QuizMeContext.Options;
    public OptionRepository() {
        ReadFromFile();
    }
    @Override
    public void Create(Option option) {
        Options.add(option);
        WriteToFile(option);
    }

    @Override
    public Option GetById(int id) {
        for (Option i : Options) {
            if (i.Id == id) {
                return i;
            }
        }
        return null;
    }

    @Override
    public Option GetByName(String name) {
        for (Option i : Options) {
            if (i.Content.equals(name.strip())) {
                return i;
            }
        }
        return null;
    }
    @Override
    public List<Option> GetAll() {
        return Options;
    }

    @Override
    public void Update(Option option) {
        Option optionExisting = GetById(option.Id);
        if (optionExisting == null)
            return;
        optionExisting.Content = option.Content;
        //optionExisting.QuestionOptions = option.QuestionOptions;
        RefreshFile();
    }
    @Override
    public void Delete(Option option) {
        Options.remove(option);
        RefreshFile();
    }

    @Override
    public void WriteToFile(Option option) {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;

            try (FileWriter outputFile = new FileWriter(file, true)) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(option);

                if (fileIsEmpty) {
                    outputFile.write(jsonString);
                } else {
                    outputFile.write("\n" + jsonString);
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while writing options to file.");
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void ReadFromFile() {
        File myFile = new File(FilePath);
        if (!myFile.exists()){
            System.out.println("Options file does not exist.");
            return;
        }
        try (Scanner inputFile = new Scanner(myFile)) {
            while (inputFile.hasNext()) {
                Gson gson = new Gson();
                String json = inputFile.nextLine();
                if (json.isEmpty()) continue;
                Option option = gson.fromJson(json, Option.class);
                Options.add(option);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading options from file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void RefreshFile() {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;
            try (FileWriter outputFile = new FileWriter(file)) {
                for (Option i : Options) {
                    Gson gson = new Gson();

                    String jsonString = gson.toJson(i);
                    if (fileIsEmpty) {
                        outputFile.write(jsonString);
                    } else {
                        outputFile.write("\n" + jsonString);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing options to file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int GenerateId() {
        return Options.size() + 1;
    }
}

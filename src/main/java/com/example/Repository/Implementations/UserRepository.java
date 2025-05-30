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
import com.example.Models.User;
import com.example.Repository.Interfaces.IUserRepository;
import com.google.gson.Gson;

@Repository
public final class UserRepository implements IUserRepository{
    String FilePath = FileNames.BasePath + "\\" + FileNames.Users;
    List<User> Users = QuizMeContext.Users;
    public UserRepository() {
        ReadFromFile();
    }
    @Override
    public void Create(User user) {
        Users.add(user);
        WriteToFile(user);
    }

    @Override
    public User GetById(int id) {
        for (User i : Users) {
            if (i.Id == id) {
                return i;
            }
        }
        return null;
    }

    @Override
    public User GetByEmail(String email) {
        for (User i : Users) {
            if (i.Email.equalsIgnoreCase(email)) {
                return i;
            }
        }
        return null;
    }

    @Override
    public User GetByUsername(String username) {
        for (User i : Users) {
            if (i.Username.equals(username)) {
                return i;
            }
        }
        return null;
    }

    @Override
    public int GenerateId() {
        return Users.size() + 1;
    }

    @Override
    public List<User> GetAll() {
        return Users;
    }

    @Override
    public void Update(User user) {
        User userExisting = GetById(user.Id);
        if (userExisting == null)
            return;
        userExisting.Username = user.Username;
        userExisting.Email = user.Email;
        userExisting.setPasswordHash(user.getPasswordHash());
        RefreshFile();
    }
    @Override
    public void Delete(User user) {
        Users.remove(user);
        RefreshFile();
    }

    @Override
    public void WriteToFile(User user) {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;

            try (FileWriter outputFile = new FileWriter(file, true)) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(user);

                if (fileIsEmpty) {
                    outputFile.write(jsonString);
                } else {
                    outputFile.write("\n" + jsonString);
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred while writing users to file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void ReadFromFile() {
        File myFile = new File(FilePath);
        if (!myFile.exists()){
            System.out.println("Users file does not exist.");
            return;
        }
        try (Scanner inputFile = new Scanner(myFile)) {
            while (inputFile.hasNext()) {
                Gson gson = new Gson();
                String json = inputFile.nextLine();
                if (json.isEmpty()) continue;
                User user = gson.fromJson(json, User.class);
                Users.add(user);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading users from file.");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void RefreshFile() {
        try {
            File file = new File(FilePath);
            boolean fileIsEmpty = !file.exists() || file.length() == 0;
            try (FileWriter outputFile = new FileWriter(file)) {
                for (User i : Users) {
                    Gson gson = new Gson();

                    String jsonString = gson.toJson(i);
                    outputFile.write(System.lineSeparator() + jsonString);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing users to file.");
            System.out.println(e.getMessage());
        }
    }
}

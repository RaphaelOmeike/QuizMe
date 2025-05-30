package com.example.Models;
import java.util.ArrayList;
import java.util.List;

import com.example.Models.Common.BaseEntity;
import com.example.Models.Enums.Gender;
public class Player extends BaseEntity {
    public String Name;
    public String Email;
    public Gender Gender;
    //boolean is current player
    //public List<Quiz> Quizzes = new ArrayList<>();
    public Player(int id, String name, String email, Gender gender) {
        super(id);
        Name = name;
        Email = email;
        Gender = gender;
    }
}

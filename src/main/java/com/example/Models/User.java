package com.example.Models;

import com.example.Models.Common.BaseEntity;
public class User extends BaseEntity {
    public String Username;
    public String Email;
    private String PasswordHash;

    public void setPasswordHash(String passwordHash) {
        PasswordHash = passwordHash;
    }
    public String getPasswordHash() {
        return PasswordHash;
    }

    public User(int id, String username, String email) {
        super(id);
        Username = username;
        Email = email;
    }
}

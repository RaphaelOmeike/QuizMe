package com.example.Services.Interfaces;

import com.example.DTOs.Users.UserRequest;
import com.example.DTOs.Users.UserResponse;

public interface IUserService {
    UserResponse Login(UserRequest request);
    void Logout();

    boolean CheckUsernameExists(String username);
    int CreateUser(UserRequest request);
    UserResponse GetUserById(int userId);
    
    UserResponse GetUserByEmail(String email);

    //boolean ChangePassword(UserRequest request);
}

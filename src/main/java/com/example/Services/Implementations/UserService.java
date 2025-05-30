package com.example.Services.Implementations;

import org.springframework.stereotype.Service;

import com.example.DTOs.Users.UserRequest;
import com.example.DTOs.Users.UserResponse;
import com.example.Models.User;
import com.example.Repository.Interfaces.IPlayerRepository;
import com.example.Repository.Interfaces.IUserRepository;
import com.example.Services.Interfaces.IUserService;

@Service
public class UserService implements IUserService {

    private final IUserRepository _userRepository;
    private final IPlayerRepository _playerRepository;
    public static User _currentUser;
    public UserService(IUserRepository userRepository, IPlayerRepository playerRepository) {
        _userRepository = userRepository;
        _playerRepository = playerRepository;
    }

    public UserResponse Login(UserRequest request) {
        var userExists = _userRepository.GetByEmail(request.Email());
        if (userExists == null) {
            var user = _userRepository.GetByUsername(request.Email());
            if (user == null) {
                System.out.println("Invalid login credentials.");
                return null;
            }
            userExists = user;
        }

        if (!userExists.getPasswordHash().equals(request.Password())) {
            System.out.println("Invalid login credentials.");
            return null;
        }
        _currentUser = userExists;
        PlayerService._currentPlayer = _playerRepository.GetByEmail(userExists.Email);
        System.out.println("Logged in successfully.");
        return new UserResponse(userExists.Id, userExists.Username, userExists.Email);
    }

    @Override
    public void Logout() {
        _currentUser = null;
        System.out.println("Logout successful.");
    }

    @Override
    public boolean CheckUsernameExists(String username) {
        return _userRepository.GetByUsername(username) != null;
    }

    @Override
    public int CreateUser(UserRequest request) {
//        if (CheckUsernameExists(request.Username())) {
//            System.out.println("Username already exists.");
//            return
//        }
        User user = new User(_userRepository.GenerateId(), request.Username(), request.Email());
        user.setPasswordHash(request.Password());
        _userRepository.Create(user);
        return user.Id;
    }

    @Override
    public UserResponse GetUserById(int userId) {
        var user = _userRepository.GetById(userId);
        if (user == null)
            return null;

        return new UserResponse(user.Id, user.Username, user.Email);
    }

    @Override
    public UserResponse GetUserByEmail(String email) {
        var user = _userRepository.GetByEmail(email);
        if (user == null)
            return null;

        return new UserResponse(user.Id, user.Username, user.Email);
    }


}

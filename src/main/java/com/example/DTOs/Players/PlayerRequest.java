package com.example.DTOs.Players;

import com.example.Models.Enums.Gender;

public record PlayerRequest(String Name, String Email, Gender Gender) {

}

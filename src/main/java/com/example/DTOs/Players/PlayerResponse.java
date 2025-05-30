package com.example.DTOs.Players;

import com.example.Models.Enums.Gender;

public record PlayerResponse(int Id, String Name, String Email, Gender Gender) {

}

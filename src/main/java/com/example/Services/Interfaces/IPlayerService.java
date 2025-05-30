package com.example.Services.Interfaces;

import com.example.DTOs.Players.PlayerRequest;
import com.example.DTOs.Players.PlayerResponse;

public interface IPlayerService {
    int CreatePlayer(PlayerRequest request);
    PlayerResponse GetPlayerById(int playerId);
    // void changecurrentplayer(old, new);
}

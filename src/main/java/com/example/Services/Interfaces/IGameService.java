package com.example.Services.Interfaces;

import java.util.List;

import com.example.DTOs.Games.GameRequest;
import com.example.DTOs.Games.GameResponse;

public interface IGameService {
    int CreateGame(GameRequest request);
    GameResponse GetGameById(int gameId);
    void SetGameResult(int gameId, String result);
    public void SetGamePlayerAttempts(int gameId, int player1AttemptId, int player2AttemptId);
    List<GameResponse> GetGamesByPlayer(int playerId);
}

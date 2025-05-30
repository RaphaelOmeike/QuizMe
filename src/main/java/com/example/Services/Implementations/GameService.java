package com.example.Services.Implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.DTOs.Games.GameRequest;
import com.example.DTOs.Games.GameResponse;
import com.example.Models.Game;
import com.example.Repository.Interfaces.IGameRepository;
import com.example.Services.Interfaces.IGameService;

@Service
public class GameService implements IGameService {
    private final IGameRepository _gameRepository;
    public GameService(IGameRepository gameRepository) {
        _gameRepository = gameRepository;
    }

    @Override
    public int CreateGame(GameRequest request) {
        Game game = new Game(_gameRepository.GenerateId(), request.Player1Id(), request.Player2Id(), request.Player1QuizId(), request.Player2QuizId(), request.Player1Won(), request.Result(), request.QuizMode());
        _gameRepository.Create(game);
        return game.Id;
    }

    @Override
    public GameResponse GetGameById(int gameId) {
        var game = _gameRepository.GetById(gameId);
        if (game == null)
            return null;
        
        return new GameResponse(game.Id, game.Player1Id, game.Player2Id, game.Player1QuizId, game.Player2QuizId, game.Player1Won, game.Result, game.QuizMode, game.Player1AttemptId, game.Player2AttemptId);
    }

    @Override
    public List<GameResponse> GetGamesByPlayer(int playerId) {
        List<Game> games = _gameRepository.GetAll();
        List<GameResponse> result = new ArrayList<>();

        for (var game: games) {
            if (game.Player1Id == playerId || game.Player2Id == playerId) {
                result.add(new GameResponse(game.Id, game.Player1Id, game.Player2Id, game.Player1QuizId, game.Player2QuizId, game.Player1Won, game.Result, game.QuizMode, game.Player1AttemptId, game.Player2AttemptId));
            }
        }
        return result;
    }

    @Override
    public void SetGameResult(int gameId, String result) {
        Game game = _gameRepository.GetById(gameId);
        if (game == null)
            return;
        game.Result = result;
        _gameRepository.RefreshFile();
    }

    @Override
    public void SetGamePlayerAttempts(int gameId, int player1AttemptId, int player2AttemptId) {
        Game game = _gameRepository.GetById(gameId);
        if (game == null)
            return;
        game.Player1AttemptId = player1AttemptId;
        game.Player2AttemptId = player2AttemptId;
        _gameRepository.RefreshFile();
    }
    
}

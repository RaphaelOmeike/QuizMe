package com.example.Services.Implementations;

import org.springframework.stereotype.Service;

import com.example.DTOs.Players.PlayerRequest;
import com.example.DTOs.Players.PlayerResponse;
import com.example.Models.Player;
import com.example.Repository.Interfaces.IPlayerRepository;
import com.example.Services.Interfaces.IPlayerService;

@Service
public class PlayerService implements IPlayerService {

    public PlayerService(IPlayerRepository playerRepository) {
        _playerRepository = playerRepository;
    }

    private final IPlayerRepository _playerRepository;
    public static Player _currentPlayer;
    @Override
    public int CreatePlayer(PlayerRequest request) {
        Player player = new Player(_playerRepository.GenerateId(), request.Name(), request.Email(), request.Gender());
        _playerRepository.Create(player);
        return player.Id;
    }

    @Override
    public PlayerResponse GetPlayerById(int playerId) {
        var player = _playerRepository.GetById(playerId);
        if (player == null)
            return null;

        return new PlayerResponse(player.Id, player.Name, player.Email, player.Gender);
    }

}

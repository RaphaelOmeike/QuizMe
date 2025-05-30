package com.example.Services.Implementations;

import com.example.DTOs.PlayerAttemptOptions.PlayerAttemptOptionRequest;
import com.example.DTOs.PlayerAttemptOptions.PlayerAttemptOptionResponse;
import com.example.Models.PlayerAttemptOption;
import com.example.Repository.Interfaces.IPlayerAttemptOptionRepository;
import com.example.Services.Interfaces.IPlayerAttemptOptionService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PlayerAttemptOptionService implements IPlayerAttemptOptionService {
    private final IPlayerAttemptOptionRepository _playerAttemptOptionRepository;

    public PlayerAttemptOptionService(IPlayerAttemptOptionRepository playerAttemptOptionRepository) {
        _playerAttemptOptionRepository = playerAttemptOptionRepository;
    }

    @Override
    public int CreatePlayerAttemptOption(PlayerAttemptOptionRequest request) {
        PlayerAttemptOption playerAttemptOption = new PlayerAttemptOption(_playerAttemptOptionRepository.GenerateId(), request.PlayerAttemptQuestionId(), request.OptionId(), request.IsChosen());
        _playerAttemptOptionRepository.Create(playerAttemptOption);
        return playerAttemptOption.Id;
    }

    @Override
    public PlayerAttemptOptionResponse GetPlayerAttemptOptionById(int playerAttemptOptionId) {
        var playerAttemptOption = _playerAttemptOptionRepository.GetById(playerAttemptOptionId);
        if (playerAttemptOption == null)
            return null;
        return new PlayerAttemptOptionResponse(playerAttemptOption.Id, playerAttemptOption.PlayerAttemptQuestionId, playerAttemptOption.OptionId, playerAttemptOption.IsChosen);
    }

    @Override
    public List<PlayerAttemptOptionResponse> GetPlayerAttemptOptions(int playerAttemptQuestionId) {
        var playerAttemptQuestionOptions = _playerAttemptOptionRepository.GetAll();
        List<PlayerAttemptOptionResponse> result = new ArrayList<>();

        for (var i: playerAttemptQuestionOptions) {
            if (i.PlayerAttemptQuestionId == playerAttemptQuestionId) {
                PlayerAttemptOptionResponse res = new PlayerAttemptOptionResponse(i.Id, i.PlayerAttemptQuestionId, i.OptionId, i.IsChosen);
                result.add(res);
            }
        }
        return result;
    }

    @Override
    public PlayerAttemptOptionResponse GetChosenOptionForPlayerAttemptQuestion(int playerAttemptQuestionId) {
        List<PlayerAttemptOptionResponse> options = GetPlayerAttemptOptions(playerAttemptQuestionId);
        for (PlayerAttemptOptionResponse i: options) {
            if (i.IsChosen()) {
                return new PlayerAttemptOptionResponse(i.Id(), i.PlayerAttemptQuestionId(), i.OptionId(), i.IsChosen());
            }
        }
        return null;
    }
}

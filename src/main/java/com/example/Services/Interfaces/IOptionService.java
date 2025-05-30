package com.example.Services.Interfaces;

import java.util.List;

import com.example.DTOs.Options.OptionRequest;
import com.example.DTOs.Options.OptionResponse;

public interface IOptionService {
    int CreateOption(OptionRequest request);
    OptionResponse GetOptionById(int optionId);
    OptionResponse GetOptionByName(String optionName);
    OptionResponse GetAnswerToQuestion(int questionId);
    void DeleteOptionsForQuestion(int questionId);
    List<OptionResponse> GetOptionsForQuestion(int questionId);
}

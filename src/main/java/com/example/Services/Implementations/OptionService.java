package com.example.Services.Implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.DTOs.Options.OptionRequest;
import com.example.DTOs.Options.OptionResponse;
import com.example.DTOs.QuestionOptions.QuestionOptionResponse;
import com.example.Models.Option;
import com.example.Repository.Interfaces.IOptionRepository;
import com.example.Services.Interfaces.IOptionService;
import com.example.Services.Interfaces.IQuestionOptionService;

@Service
public class OptionService implements IOptionService{
    private final IOptionRepository _optionRepository;
    private final IQuestionOptionService _questionOptionService;
    public OptionService(IOptionRepository optionRepository, IQuestionOptionService questionOptionService) {
        _optionRepository = optionRepository;
        _questionOptionService = questionOptionService;
    }

    @Override
    public int CreateOption(OptionRequest request) {
        Option option = new Option(_optionRepository.GenerateId(), request.Content());
        _optionRepository.Create(option);
        return option.Id;
    }

    @Override
    public OptionResponse GetOptionById(int optionId) {
        var option = _optionRepository.GetById(optionId);
        if (option == null)
            return null;
        
        return new OptionResponse(option.Id, option.Content);
    }

    @Override //working on this
    public OptionResponse GetOptionByName(String optionName) {
        var option = _optionRepository.GetByName(optionName.strip());
        if (option == null)
            return null;
        return new OptionResponse(option.Id, option.Content);
    }

    @Override
    public OptionResponse GetAnswerToQuestion(int questionId) {
        List<QuestionOptionResponse> options = _questionOptionService.GetQuestionOptionsForQuestion(questionId);
        OptionResponse option = null;
        for (var i: options) {
            if (i.IsAnswer()) {
                Option opt = _optionRepository.GetById(i.OptionId());
                option = new OptionResponse(opt.Id, opt.Content);
            }
        }
        return option;
    }

    @Override
    public List<OptionResponse> GetOptionsForQuestion(int questionId) {
        List<QuestionOptionResponse> options = _questionOptionService.GetQuestionOptionsForQuestion(questionId);
        List<OptionResponse> actualOptions = new ArrayList<>();
        for (var i: options) {
            Option opt = _optionRepository.GetById(i.OptionId());
            actualOptions.add(new OptionResponse(opt.Id, opt.Content));
        }
        return actualOptions;
    }

    @Override
    public void DeleteOptionsForQuestion(int questionId) {
        List<OptionResponse> options = GetOptionsForQuestion(questionId);
        for (var i: options) {
            if (_questionOptionService.CheckOptionUsedByOtherQuestions(i.Id()))
                _optionRepository.Delete(_optionRepository.GetById(i.Id()));
        }
    }
}

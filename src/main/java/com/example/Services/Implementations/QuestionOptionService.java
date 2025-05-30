package com.example.Services.Implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.DTOs.QuestionOptions.QuestionOptionRequest;
import com.example.DTOs.QuestionOptions.QuestionOptionResponse;
import com.example.Models.QuestionOption;
import com.example.Repository.Interfaces.IQuestionOptionRepository;
import com.example.Services.Interfaces.IQuestionOptionService;

@Service
public class QuestionOptionService implements IQuestionOptionService {
    private final IQuestionOptionRepository _questionOptionRepository;
    public QuestionOptionService(IQuestionOptionRepository questionOptionRepository) {
        _questionOptionRepository = questionOptionRepository;
    }

    @Override
    public int CreateQuestionOption(QuestionOptionRequest request) {
        QuestionOption questionOption = new QuestionOption(_questionOptionRepository.GenerateId(), request.QuestionId(), request.OptionId(), request.IsAnswer());
        _questionOptionRepository.Create(questionOption);

        return questionOption.Id;
    }

    @Override
    public QuestionOptionResponse GetQuestionOption(int questionOptionId) {
        var questionOption = _questionOptionRepository.GetById(questionOptionId);
        return new QuestionOptionResponse(questionOption.Id, questionOption.QuestionId, questionOption.OptionId, questionOption.IsAnswer);
    }

    @Override
    public List<QuestionOptionResponse> GetQuestionOptionsForQuestion(int questionId) {
        var questionOptions = _questionOptionRepository.GetAll();
        List<QuestionOptionResponse> result = new ArrayList<>();

        for (var i: questionOptions) {
            if (i.QuestionId == questionId) {
                QuestionOptionResponse res = new QuestionOptionResponse(i.Id, i.QuestionId, i.OptionId, i.IsAnswer);
                result.add(res);
            }
        }
        return result;
    }

    @Override
    public boolean CheckOptionUsedByOtherQuestions(int optionId) {
        var questionOptions = _questionOptionRepository.GetAll();
        int count = 0;
        for (var i: questionOptions) {
            if (i.OptionId == optionId)
                count++;
            if (count >= 2)
                return false;
        }
        return true;
    }

    @Override
    public void DeleteQuestionOptionsForQuestion(int questionId) {
        var questionOptions = _questionOptionRepository.GetAll();
        List<QuestionOption> toDelete = new ArrayList<>();

        for (var i: questionOptions) {
            if (i.QuestionId == questionId) {
                toDelete.add(i);
            }
        }
        for (var i: toDelete) {
            _questionOptionRepository.Delete(i);
        }
    }
}

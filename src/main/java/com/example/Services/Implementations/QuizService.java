package com.example.Services.Implementations;

import java.awt.desktop.QuitResponse;
import java.util.ArrayList;
import java.util.List;

import com.example.Services.Interfaces.IPlayerAttemptService;
import com.example.Utilities.ConsoleColour;
import org.springframework.stereotype.Service;

import com.example.DTOs.QuestionOptions.QuestionOptionResponse;
import com.example.DTOs.Questions.QuestionResponse;
import com.example.DTOs.Quizzes.QuizRequest;
import com.example.DTOs.Quizzes.QuizResponse;
import com.example.Models.Quiz;
import com.example.Repository.Interfaces.IQuizRepository;
import com.example.Services.Interfaces.IQuestionService;
import com.example.Services.Interfaces.IQuizService;

@Service
public class QuizService implements IQuizService {

    private final IQuizRepository _quizRepository;
    private final IQuestionService _questionService;
    private final IPlayerAttemptService _playerAttemptService;

    public QuizService(IQuizRepository quizRepository, IQuestionService questionService, IPlayerAttemptService playerAttemptService) {
        _quizRepository = quizRepository;
        _questionService = questionService;
        _playerAttemptService = playerAttemptService;
    }

    @Override
    public int CreateQuiz(QuizRequest request) {
        Quiz quiz = new Quiz(_quizRepository.GenerateId(), request.Name(), 0, request.PlayerId());
        _quizRepository.Create(quiz);
        var loadQuestions = _questionService.ReadQuestionsFromFile(request.QuestionFileName(), quiz.Id);
        if (loadQuestions != null) {
            quiz.TotalMarks = loadQuestions.getFirst();
            _quizRepository.RefreshFile();
        }
        return quiz.Id;
    }

    @Override
    public QuizResponse GetQuizById(int Id) {
        var quiz = _quizRepository.GetById(Id);
        if (quiz == null)
        {
            return null;
        }
        return new QuizResponse(quiz.Id, quiz.Name, quiz.TotalMarks, quiz.PlayerId);
    }

    @Override
    public void LoadQuizQuestions(int quizId, String questionFileName) {
        if (_playerAttemptService.CheckPlayerAttemptExistsForQuiz(quizId)) {
            ConsoleColour.printFailure("Quiz Questions already used in a QUIZ ME!!");
            return;
        }
        Quiz quiz = _quizRepository.GetById(quizId);
        DeleteQuizQuestions(quizId); //very wicked deletion
        questionFileName += ".txt";
        var loadQuestions = _questionService.ReadQuestionsFromFile(questionFileName, quiz.Id);
        if (loadQuestions != null) {
            quiz.TotalMarks = loadQuestions.getFirst();
            _quizRepository.RefreshFile();
        }
    }
    public void DeleteQuizQuestions(int quizId) {
//        Quiz quiz = _quizRepository.GetById(quizId);
        //_quizRepository.Create(quiz);
        _questionService.DeleteAllQuestionsForQuiz(quizId);
    }

    //i need to get all quizzes by player

    @Override
    public List<QuizResponse> GetAllQuizzesByPlayer(int playerId) {
        var quizzes = _quizRepository.GetAll();
        List<QuizResponse> result = new ArrayList<>();

        for (var i: quizzes) {
            if (i.PlayerId == playerId) {
                QuizResponse res = new QuizResponse(i.Id, i.Name, i.TotalMarks, i.PlayerId);
                result.add(res);
            }
        }
        return result;
    }
}

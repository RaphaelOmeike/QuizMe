package com.example.Services.Implementations;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.Utilities.ConsoleColour;
import org.springframework.stereotype.Service;

import com.example.Constants.FileNames;
import com.example.DTOs.Options.OptionRequest;
import com.example.DTOs.QuestionOptions.QuestionOptionRequest;
import com.example.DTOs.Questions.QuestionRequest;
import com.example.DTOs.Questions.QuestionResponse;
import com.example.Models.Question;
import com.example.Repository.Interfaces.IQuestionRepository;
import com.example.Services.Interfaces.IOptionService;
import com.example.Services.Interfaces.IQuestionOptionService;
import com.example.Services.Interfaces.IQuestionService;

@Service
public class QuestionService implements IQuestionService{
    private final IQuestionRepository _questionRepository;
    private final IOptionService _optionService;
    private final IQuestionOptionService _questionOptionService;

    public QuestionService(IQuestionRepository questionRepository, IOptionService optionService, IQuestionOptionService questionOptionService) {
        _questionRepository = questionRepository;
        _optionService = optionService;
        _questionOptionService = questionOptionService;
    }

    @Override
    public int CreateQuestion(QuestionRequest request) {
        Question question = new Question(_questionRepository.GenerateId(), request.Content(), request.Marks(), request.QuizId());
        _questionRepository.Create(question);
        return question.Id;
    }

    @Override
    public QuestionResponse GetQuestionById(int Id) {
        var question = _questionRepository.GetById(Id);
        if (question == null)
        {
            return null;
        }
        return new QuestionResponse(question.Id, question.Content, question.Marks, question.QuizId);
    }

    @Override
    public List<QuestionResponse> GetAllQuestionsForQuiz(int quizId) {
        var questions = _questionRepository.GetAll();
        List<QuestionResponse> quizQuestions = new ArrayList<>();
        for (Question i: questions)
        {
            if (i.QuizId == quizId)
            {
                quizQuestions.add(new QuestionResponse(i.Id, i.Content, i.Marks, i.QuizId));
            }
        }
        return quizQuestions;
    }


    @Override
    public void DeleteAllQuestionsForQuiz(int quizId) {
        var questions = _questionRepository.GetAll();
        List<Question> toDelete = new ArrayList<>();

        for (Question i : questions) {
            if (i.QuizId == quizId) {
                toDelete.add(i);
            }
        }
        //fetching and deleting at the same time. how na?

        //2 separate collections in this case
        for (Question i : toDelete) {
            _questionRepository.Delete(i);
            _optionService.DeleteOptionsForQuestion(i.Id);
            _questionOptionService.DeleteQuestionOptionsForQuestion(i.Id);
        }

    }

    @Override
    public List<Float> ReadQuestionsFromFile(String fileName, int quizId) {
        File myFile = new File(FileNames.QuestionsPath + "\\" + fileName);
        int questionNo = 0;
        float totalMarks = 0;
        if (!myFile.exists()){
            ConsoleColour.printFailure("Quiz Questions file does not exist.");
            ConsoleColour.printFailure("Error, invalid filename specified...");
            return null;
        }
        QuestionResponse question = null;
        try (Scanner inputFile = new Scanner(myFile)) {
            while (inputFile.hasNextLine()) {
                //Praise
                String line = inputFile.nextLine();
                String content = "";
                int beginIndex = 1;
                if (line.startsWith("#")) {
                    int marks = 1;
                    char markCharacter = line.charAt(1);
                    if (Character.isDigit(markCharacter)) {
                        beginIndex = 2;
                        marks = Integer.parseInt(String.valueOf(markCharacter));
                    }
                    totalMarks += marks;
                    content = line.substring(beginIndex);
                    var questionId = CreateQuestion(new QuestionRequest(content, marks, quizId));
                    question = GetQuestionById(questionId);
                    questionNo++;
                }
                else if (line.startsWith("*") && question != null) {
                    boolean isAnswer = false;
                    if (line.startsWith("**")) {
                        isAnswer = true;
                        beginIndex = 2;
                    }
                    content = line.substring(beginIndex).strip();
                    //get option by name here in order to persist such once
                    //one option maybe used by sddmucltiple questions
                    int optionId = 0;
                    var optionExists = _optionService.GetOptionByName(content);
                    if (optionExists == null)
                        optionId =_optionService.CreateOption(new OptionRequest(content));
                    else
                        optionId = optionExists.Id();
                    
                    _questionOptionService.CreateQuestionOption(new QuestionOptionRequest(question.Id(), optionId, isAnswer));
                }
            }
            ConsoleColour.printSuccess("Questions loaded successfully from file.");
        }
        catch (FileNotFoundException e) {
            ConsoleColour.printFailure("An error occurred while loading questions from file.");
            System.out.println(e.getMessage());
        }
        var result = new ArrayList<Float>();
        result.add(totalMarks);
        result.add((float) questionNo);
        return result;
    }
}

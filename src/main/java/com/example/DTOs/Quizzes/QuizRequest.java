package com.example.DTOs.Quizzes;
import java.time.Duration;
import jdk.jfr.Timespan;

public record QuizRequest(String Name, String QuestionFileName, int PlayerId) {

}

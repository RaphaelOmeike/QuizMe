package com.example.DTOs.Quizzes;
import java.time.Duration;

public record QuizResponse(int Id, String Name, float TotalMarks, int PlayerId) {

}

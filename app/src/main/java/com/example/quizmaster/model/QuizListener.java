package com.example.quizmaster.model;

public interface QuizListener {
    void goNextQuiz(int score);
    void onClickEndQuiz();
}

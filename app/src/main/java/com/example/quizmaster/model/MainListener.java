package com.example.quizmaster.model;

import java.util.List;

public interface MainListener {
    void showImage(int imageID);
    void resetRecycleView(List<Quiz> quizList);
}

package com.example.quizmaster.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quiz implements Serializable {


    public QuizStatus quizStatus;
    public String title;
    public List<Question> questions = new ArrayList<>();
    public String Image;

    public Quiz() {
    }

    public Quiz(QuizStatus quizStatus, String title) {
        this.quizStatus = quizStatus;
        this.title = title;
    }

    public Quiz(QuizStatus quizStatus, String title, List<Question> questions) {
        this.quizStatus = quizStatus;
        this.title = title;
        this.questions = questions;
    }

    public Quiz(QuizStatus quizStatus, String title, List<Question> questions, String image) {
        this.quizStatus = quizStatus;
        this.title = title;
        this.questions = questions;
        Image = image;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public static List<String> getListImages() {
        List<String> listImageID = new ArrayList<>();
        for (int i = 0; i < getImages().length; i++){
            listImageID.add(getImages()[i]);
        }
        return listImageID;
    }

    public QuizStatus getQuizStatus() {
        return quizStatus;
    }

    public void setQuizStatus(QuizStatus quizStatus) {
        this.quizStatus = quizStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static ArrayList<Quiz> getData() {

        ArrayList<Quiz> dataList = new ArrayList<>();

        String[] titles = getTitles();

        for (int i = 0; i < titles.length; i++) {
            Quiz quiz;
            if (i==3) {
                quiz = new Quiz(QuizStatus.NON, titles[i], Question.getListOfQuestions(), getImages()[i]);
            } else if (i != 2){
                quiz = new Quiz(QuizStatus.LOSE, titles[i], Question.getListOfQuestions(), getImages()[i]);
            } else {
                quiz = new Quiz(QuizStatus.WON, titles[i], Question.getListOfQuestions(), getImages()[i]);
            }
            dataList.add(quiz);
        }
        return dataList;
    }


    public static String[] getImages() {

        String[] images = {
                "https://images.unsplash.com/photo-1538685634737-24b83e3fa2f8?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=675&q=80",
                "https://images.unsplash.com/photo-1595277057098-a16a98d66904?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80",
                "https://images.unsplash.com/photo-1541832039-cab7e4310f28?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
                "https://images.unsplash.com/photo-1474540412665-1cdae210ae6b?ixlib=rb-1.2.1&auto=format&fit=crop&w=1362&q=80",
                "https://images.unsplash.com/photo-1514888166141-950e58139e80?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjExOTYyOH0&auto=format&fit=crop&w=1351&q=80"
        };


        return images;
    }

    public static String[] getTitles(){
        String[] titles = {"Cat", "Dog", "Deer", "Parrot"};
        return titles;
    }
}

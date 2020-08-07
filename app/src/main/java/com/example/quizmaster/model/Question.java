package com.example.quizmaster.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question  implements Serializable {
    public QuestionType questionType;
    public String question;
    private String choice1;
    private String choice2;
    private String choice3;
    public String answer;

    public Question() {
    }

    public Question(QuestionType questionType, String question, String choice1, String choice2, String choice3, String answer) {
        this.questionType = questionType;
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.answer = answer;
    }

    public Question(QuestionType questionType, String question, String answer) {
        this.questionType = questionType;
        this.question = question;
        this.choice1 = "";
        this.choice2 = "";
        this.choice3 = "";
        this.answer = answer;
    }


    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public static  List<Question> getListOfQuestions(){
        List<Question> questionList = new ArrayList<>();
        questionList.add(getTextQuestion());
        questionList.add(getNumberQuestion());
        questionList.add(getMultiChoiceQuestion());
        return questionList;
    }
    public static Question getTextQuestion(){
        return new Question(QuestionType.TEXT,"What is the capital in Belguim?","Brussels");
    }
    public static Question getNumberQuestion(){
        return new Question(QuestionType.NUMBER, "2 + 2 =","4");
    }
    public static Question getMultiChoiceQuestion(){
        return new Question(QuestionType.MULTICHOICE,"Which is the capital in France?","Berlin","Madrid", "Paris","Paris");
    }
}

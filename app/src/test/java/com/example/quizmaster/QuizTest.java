package com.example.quizmaster;

import com.example.quizmaster.model.Question;
import com.example.quizmaster.model.QuestionType;
import com.example.quizmaster.model.Quiz;
import com.example.quizmaster.model.QuizStatus;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class QuizTest {
    private Quiz q1;

    @Before
    public void setup() {
        q1 = new Quiz();
    }

    @Test
    public void testSetTitle(){
        q1.setTitle("Test question");

        assertEquals("Test question", q1.getTitle());
    }

    @Test
    public void testSetQuestions(){
        Question question = new Question(QuestionType.TEXT,"What is the capital in Belguim?","Brussels");
        List<Question> questionList = new ArrayList<>();
        questionList.add(question);

        q1.setQuestions(questionList);
        //TODO test setQuestions
        assertSame(question, q1.getQuestions().get(0));
    }

    @Test
    public void testSettQuizStatus(){
        q1.setQuizStatus(QuizStatus.NON);

        assertEquals(QuizStatus.NON, q1.getQuizStatus());
    }

    @Test
    public void testSetImage(){
        q1.setImage("https://upload.wikimedia.org/wikipedia/commons/8/85/Logo-Test.png");

        assertEquals("https://upload.wikimedia.org/wikipedia/commons/8/85/Logo-Test.png", q1.getImage());
    }


}

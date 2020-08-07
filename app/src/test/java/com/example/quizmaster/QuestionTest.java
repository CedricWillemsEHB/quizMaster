package com.example.quizmaster;

import com.example.quizmaster.model.Question;
import com.example.quizmaster.model.QuestionType;
import com.example.quizmaster.model.Quiz;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class QuestionTest {
    private Question q1;

    @Before
    public void setup() {
        q1 = new Question();
    }

    @Test
    public void testGetTextQuestion(){
        q1 = Question.getTextQuestion();
        Question questionText = new Question(QuestionType.TEXT,"What is the capital in Belguim?","Brussels");


        assertEquals(questionText.getQuestion(), q1.getQuestion());
        assertEquals(questionText.getChoice1(), q1.getChoice1());
        assertEquals(questionText.getChoice2(), q1.getChoice2());
        assertEquals(questionText.getChoice3(), q1.getChoice3());
        assertEquals(questionText.getAnswer(), q1.getAnswer());
        assertEquals(questionText.getQuestionType(), q1.getQuestionType());
        assertEquals(QuestionType.TEXT, q1.getQuestionType());
        assertTrue(q1.getChoice1().isEmpty());
        assertTrue(q1.getChoice2().isEmpty());
        assertTrue(q1.getChoice3().isEmpty());
    }

    @Test
    public void testNumberQuestion(){
        q1 = Question.getNumberQuestion();
        Question questionNumber = new Question(QuestionType.NUMBER, "2 + 2 =","4");


        assertEquals(questionNumber.getQuestion(), q1.getQuestion());
        assertEquals(questionNumber.getChoice1(), q1.getChoice1());
        assertEquals(questionNumber.getChoice2(), q1.getChoice2());
        assertEquals(questionNumber.getChoice3(), q1.getChoice3());
        assertEquals(questionNumber.getAnswer(), q1.getAnswer());
        assertEquals(questionNumber.getQuestionType(), q1.getQuestionType());
        assertEquals(QuestionType.NUMBER, q1.getQuestionType());
        assertTrue(q1.getChoice1().isEmpty());
        assertTrue(q1.getChoice2().isEmpty());
        assertTrue(q1.getChoice3().isEmpty());
        assertEquals(4, Integer.parseInt(q1.getAnswer()));
    }

    @Test
    public void testGetMultiChoiceQuestion(){
        q1 = Question.getMultiChoiceQuestion();
        Question questionMultiChoice = new Question(QuestionType.MULTICHOICE,"Which is the capital in France?","Berlin","Madrid", "Paris","Paris");


        assertEquals(questionMultiChoice.getQuestion(), q1.getQuestion());
        assertEquals(questionMultiChoice.getChoice1(), q1.getChoice1());
        assertEquals(questionMultiChoice.getChoice2(), q1.getChoice2());
        assertEquals(questionMultiChoice.getChoice3(), q1.getChoice3());
        assertEquals(questionMultiChoice.getAnswer(), q1.getAnswer());
        assertEquals(questionMultiChoice.getQuestionType(), q1.getQuestionType());
        assertEquals(QuestionType.MULTICHOICE, q1.getQuestionType());
        assertFalse(q1.getChoice1().isEmpty());
        assertFalse(q1.getChoice2().isEmpty());
        assertFalse(q1.getChoice3().isEmpty());
    }

    @Test
    public void testSetQuestion(){
        q1.setQuestion("Test question");

        assertEquals("Test question", q1.getQuestion());
    }

    @Test
    public void testSetAnswer(){
        q1.setAnswer("Test answer");

        assertEquals("Test answer", q1.getAnswer());
    }

    @Test
    public void testSeQuestionType(){
        q1.setQuestionType(QuestionType.TEXT);

        assertEquals(QuestionType.TEXT, q1.getQuestionType());
    }

    @Test
    public void testSetChoice1(){
        q1.setChoice1("Test choice");

        assertEquals("Test choice", q1.getChoice1());
    }

    @Test
    public void testSetChoice2(){
        q1.setChoice2("Test choice");

        assertEquals("Test choice", q1.getChoice2());
    }

    @Test
    public void testSetChoice3(){
        q1.setChoice3("Test choice");

        assertEquals("Test choice", q1.getChoice3());
    }

}

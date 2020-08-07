package com.example.quizmaster.model;

import android.provider.BaseColumns;

public final  class QuizDb {

    public QuizDb() {
    }

    public static class QuizTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_QUIZ_STATUS = "quiz_status";
        public static final String COLUMN_IMAGE = "image";
    }

    public static class QuestionsTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_QUESTION_TYPE = "question_type";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_QUIZ_ID = "quiz_id";
    }
}

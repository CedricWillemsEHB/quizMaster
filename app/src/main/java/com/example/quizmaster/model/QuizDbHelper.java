package com.example.quizmaster.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String TAG = QuizDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "MyQuiz.db";
    private static final int DATABASE_VERSION = 1;
    private boolean firstUse = true;

    private SQLiteDatabase db;
    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;

        final  String SQL_CREATE_QUIZ_TABLE = "CREATE TABLE " +
                QuizDb.QuizTable.TABLE_NAME + " ( " +
                QuizDb.QuizTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizDb.QuizTable.COLUMN_TITLE + " TEXT, " +
                QuizDb.QuizTable.COLUMN_QUIZ_STATUS + " TEXT, " +
                QuizDb.QuizTable.COLUMN_IMAGE  + " TEXT " +
                ")";

        final  String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuizDb.QuestionsTable.TABLE_NAME + " ( " +
                QuizDb.QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizDb.QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuizDb.QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuizDb.QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuizDb.QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuizDb.QuestionsTable.COLUMN_QUESTION_TYPE + " TEXT, " +
                QuizDb.QuestionsTable.COLUMN_ANSWER + " TEXT, " +
                QuizDb.QuestionsTable.COLUMN_QUIZ_ID + " INTEGER, " +
                "FOREIGN KEY ("+QuizDb.QuestionsTable.COLUMN_QUIZ_ID +") REFERENCES "+QuizDb.QuizTable.TABLE_NAME+"("+QuizDb.QuizTable._ID+")" +
                "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_QUIZ_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        if(firstUse){
            updateQuizDb(0);
            firstUse = false;
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + QuizDb.QuizTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuizDb.QuestionsTable.TABLE_NAME);

        // create new tables
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public void updateDb(List<Quiz> quizList){
        Log.i(TAG , "updateDb");
        // fill database
        updateQuizDb(quizList);
    }

    public void addDummyQuiz(int startQuizId){
        Log.i(TAG , "addDummyQuiz");
        updateQuizDb(startQuizId);
    }

    private void updateQuizDb(List<Quiz> quizList) {
        Log.i(TAG , "updateQuizDb");
        onUpgrade(db,DATABASE_VERSION, DATABASE_VERSION);
        for (int i = 0; i < quizList.size(); i++) {
            addQuiZ(quizList.get(i));
            for (int y = 0; y < quizList.get(i).getQuestions().size(); y++) {
                addQuestion(quizList.get(i).getQuestions().get(y), i+1);
            }
        }
    }

    private void updateQuizDb(int startQuizId) {
        Log.i(TAG , "updateQuizDb");
        ArrayList<Quiz> quizArrayList = Quiz.getData();
        for (int i = 0; i < quizArrayList.size(); i++) {
            addQuiZ(quizArrayList.get(i));
            for (int y = 0; y < quizArrayList.get(i).getQuestions().size(); y++) {
                addQuestion(quizArrayList.get(i).getQuestions().get(y), startQuizId +1);
            }
            startQuizId++;
        }
    }

    private void addQuestion(Question question, int i) {
        ContentValues cv = new ContentValues();
        cv.put(QuizDb.QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuizDb.QuestionsTable.COLUMN_OPTION1, question.getChoice1());
        cv.put(QuizDb.QuestionsTable.COLUMN_OPTION2, question.getChoice2());
        cv.put(QuizDb.QuestionsTable.COLUMN_OPTION3, question.getChoice3());
        cv.put(QuizDb.QuestionsTable.COLUMN_QUESTION_TYPE, "" + question.getQuestionType());
        cv.put(QuizDb.QuestionsTable.COLUMN_ANSWER, question.getAnswer());
        cv.put(QuizDb.QuestionsTable.COLUMN_QUIZ_ID, i);
        db.insert(QuizDb.QuestionsTable.TABLE_NAME, null, cv);
    }

    private void addQuiZ(Quiz quiz) {
        ContentValues cv = new ContentValues();
        cv.put(QuizDb.QuizTable.COLUMN_TITLE, quiz.getTitle());
        cv.put(QuizDb.QuizTable.COLUMN_QUIZ_STATUS, "" + quiz.getQuizStatus());
        cv.put(QuizDb.QuizTable.COLUMN_IMAGE, quiz.getImage());
        db.insert(QuizDb.QuizTable.TABLE_NAME, null, cv);
    }

    public List<Quiz> getAllQuiz(){
        List<Quiz> quizList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursorQuiz = db.rawQuery("SELECT * FROM " + QuizDb.QuizTable.TABLE_NAME, null);
        Cursor cursorQuestion = null;
        int i = 1;
        if(cursorQuiz.moveToFirst()){
            do {
                Quiz quiz = new Quiz();
                quiz.setTitle(cursorQuiz.getString(cursorQuiz.getColumnIndex(QuizDb.QuizTable.COLUMN_TITLE)));
                switch (cursorQuiz.getString(cursorQuiz.getColumnIndex(QuizDb.QuizTable.COLUMN_QUIZ_STATUS))){
                    case "NON":
                        quiz.setQuizStatus(QuizStatus.NON);
                        break;
                    case "WON":
                        quiz.setQuizStatus(QuizStatus.WON);
                        break;
                    case "LOSE":
                        quiz.setQuizStatus(QuizStatus.LOSE);
                        break;
                }
                quiz.setImage(cursorQuiz.getString(cursorQuiz.getColumnIndex(QuizDb.QuizTable.COLUMN_IMAGE)));
                cursorQuestion = db.rawQuery("SELECT * FROM " + QuizDb.QuestionsTable.TABLE_NAME + " WHERE " +
                        QuizDb.QuestionsTable.COLUMN_QUIZ_ID + " = " + i + ";", null);
                List<Question> questionList = new ArrayList<>();
                if (cursorQuestion.moveToFirst()){
                    do {
                        Question question = new Question();
                        question.setQuestion(cursorQuestion.getString(cursorQuestion.getColumnIndex(QuizDb.QuestionsTable.COLUMN_QUESTION)));
                        question.setChoice1(cursorQuestion.getString(cursorQuestion.getColumnIndex(QuizDb.QuestionsTable.COLUMN_OPTION1)));
                        question.setChoice2(cursorQuestion.getString(cursorQuestion.getColumnIndex(QuizDb.QuestionsTable.COLUMN_OPTION2)));
                        question.setChoice3(cursorQuestion.getString(cursorQuestion.getColumnIndex(QuizDb.QuestionsTable.COLUMN_OPTION3)));

                        switch (cursorQuestion.getString(cursorQuestion.getColumnIndex(QuizDb.QuestionsTable.COLUMN_QUESTION_TYPE))){
                            case "MULTICHOICE":
                                question.setQuestionType(QuestionType.MULTICHOICE);
                                break;
                            case "NUMBER":
                                question.setQuestionType(QuestionType.NUMBER);
                                break;
                            case "TEXT":
                                question.setQuestionType(QuestionType.TEXT);
                                break;
                        }
                        question.setAnswer(cursorQuestion.getString(cursorQuestion.getColumnIndex(QuizDb.QuestionsTable.COLUMN_ANSWER)));
                        questionList.add(question);
                    } while (cursorQuestion.moveToNext());
                }
                i++;
                quiz.setQuestions(questionList);
                quizList.add(quiz);
            } while (cursorQuiz.moveToNext());
        }
        cursorQuiz.close();
        return quizList;
    }

}

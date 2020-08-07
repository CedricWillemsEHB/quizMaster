package com.example.quizmaster.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quizmaster.R;
import com.example.quizmaster.model.Constants;
import com.example.quizmaster.model.Question;
import com.example.quizmaster.model.QuestionType;
import com.example.quizmaster.model.Quiz;
import com.example.quizmaster.model.QuizListener;

import java.io.Serializable;
import java.util.Locale;

/**
 * Source: https://codinginflow.com/tutorials/android/quiz-app-with-sqlite/part-1-layouts
 */

public class QuizActivity extends AppCompatActivity implements QuizListener {
    private static String TAG = QuizActivity.class.getSimpleName();

    private FragmentManager manager;
    Toolbar toolbar;
    Quiz quiz;
    Question currentQuestion;
    int score = 0;
    int position = 0;
    boolean showResult = false;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadLocale();
        setContentView(R.layout.activity_quiz);
        quiz = (Quiz) getIntent().getSerializableExtra(Constants.KEY_MYQUIZ);
        manager = getSupportFragmentManager();
        currentQuestion = quiz.getQuestions().get(position);
        Log.i(TAG, currentQuestion.getQuestion());
        addFragmentBoard();
        addFragmentQuiz();
        setUpToolBar();
    }

    private void removeFragmentQuiz() {
        FragmentTransaction transaction = manager.beginTransaction();
        QuestionType questionType = null;
        if(position == quiz.getQuestions().size()){
            questionType = quiz.getQuestions().get(position -1).getQuestionType();
        }else if(position>0){
            questionType = quiz.getQuestions().get(position - 1).getQuestionType();
        }
        if (questionType != null){
            switch (questionType){
                case MULTICHOICE:
                    QuizMultiFragment quizMultiFragment = (QuizMultiFragment) manager.findFragmentByTag(Constants.KEY_QUIZ_FRAG);
                    if (quizMultiFragment != null){
                        transaction.remove(quizMultiFragment);
                    }
                    break;
                case TEXT:
                    QuizTextFragment quizTextFragment = (QuizTextFragment) manager.findFragmentByTag(Constants.KEY_QUIZ_FRAG);
                    if (quizTextFragment != null){
                        transaction.remove(quizTextFragment);
                    }
                    break;
                case NUMBER:
                    QuizNumberFragment quizNumberFragment = (QuizNumberFragment) manager.findFragmentByTag(Constants.KEY_QUIZ_FRAG);
                    if (quizNumberFragment != null){
                        transaction.remove(quizNumberFragment);
                    }
                    break;
            }
        }
        transaction.commit();
    }

    private void addFragmentQuiz() {
        removeFragmentQuiz();
        FragmentTransaction transaction = manager.beginTransaction();

        switch (currentQuestion.getQuestionType()){
            case NUMBER:
                QuizNumberFragment quizNumberFragment = new QuizNumberFragment();
                quizNumberFragment.setQuestion(currentQuestion);
                transaction.add(R.id.fragment_quiz, quizNumberFragment, Constants.KEY_QUIZ_FRAG);
                break;
            case TEXT:
                QuizTextFragment quizTextFragment = new QuizTextFragment();
                quizTextFragment.setQuestion(currentQuestion);
                transaction.add(R.id.fragment_quiz, quizTextFragment, Constants.KEY_QUIZ_FRAG);
                break;
            case MULTICHOICE:
                QuizMultiFragment quizMultiFragment = new QuizMultiFragment();
                quizMultiFragment.setQuestion(currentQuestion);
                transaction.add(R.id.fragment_quiz, quizMultiFragment, Constants.KEY_QUIZ_FRAG);
                break;
        }
        transaction.commit();
    }

    private String getStringBoard(){
        return getResources().getString(R.string.question) + " " + (position +1) + " / " + quiz.getQuestions().size() + ".";
    }

    private void addFragmentBoard() {
        BoardFragment boardFragment = null;
        FragmentTransaction transaction = manager.beginTransaction();
        String board = getStringBoard();
        switch (quiz.getQuizStatus()){
            case LOSE:
                boardFragment = new BoardFragment(getResources().getString(R.string.hi_you_back),board);
                break;
            case NON:
                boardFragment = new BoardFragment(getResources().getString(R.string.welcome_good_luck),board);
                break;
        }
        transaction.add(R.id.fragment_board, boardFragment, Constants.KEY_BOARD_FRAG);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_board,
//                boardFragment).commit();
        transaction.commit();
    }

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //change actionbar
        toolbar.setTitle(quiz.getTitle());
//        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);
    }

    private void setQuiz(Quiz quiz){
        this.quiz = quiz;
    }

    private void showChangeLanguageDialog(){
        //array of languages to display in alert dialog
        final  String[] listItems = {"Nederlands", "English", "Français"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(QuizActivity.this);
        mBuilder.setTitle("Choose Language...");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    //Nederlands
                    setLocale("nl");
                    recreate();
                } else if (i==1){
                    //Engels
                    setLocale("en");
                    recreate();
                }else if (i==2){
                    //Engels
                    setLocale("FR");
                    recreate();
                }

                //dismiss alert dialog when language selected
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        //show alert dialog
        mDialog.show();
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        //save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", language);
        editor.apply();
    }

    // load language saved in shared preferences
    public void loadLocale() {
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        setLocale(language);
    }

    public void addResultFragment(String urlImage){
        showResult = true;
        FragmentTransaction transaction = manager.beginTransaction();
        QuizResultFragment quizResultFragment;
        quizResultFragment = new QuizResultFragment(urlImage);

        transaction.add(R.id.fragment_quiz, quizResultFragment, Constants.KEY_QUIZ_FRAG);
        transaction.commit();

    }

    @Override
    public void goNextQuiz(int point) {
        sendPositionToBoard();
        this.score += point;
        if (this.quiz.getQuestions().size() != this.position ){
            this.position++;
        }

        if (this.quiz.getQuestions().size() > this.position){

            Log.i(TAG, this.score +" goNextQuiz : goNext");
            this.currentQuestion = this.quiz.getQuestions().get(position);
            addFragmentQuiz();

        } else if(this.score ==  this.quiz.getQuestions().size()) {
            //TODO player won
            Log.i(TAG, this.score+" goNextQuiz : goWin");
            removeFragmentQuiz();
            Log.i(TAG, this.score+" url : " + quiz.getImage());
            addResultFragment(quiz.getImage());
            sendTitleToBoard(getResources().getString(R.string.congrat_you_won));
        } else {
            //TODO player lost
            Log.i(TAG, this.score +" goNextQuiz : goLost");
            removeFragmentQuiz();
            addResultFragment(null);
            sendTitleToBoard(getResources().getString(R.string.sorry_you_lost));
        }

    }

    @Override
    public void onClickEndQuiz() {
        finishQuiz(true);
    }

    private void sendTitleToBoard(String title){
        BoardFragment fragmentB = (BoardFragment) manager.findFragmentByTag(Constants.KEY_BOARD_FRAG);
        fragmentB.setTitleBoard(title);
    }

    private void sendPositionToBoard(){
        BoardFragment fragmentB = (BoardFragment) manager.findFragmentByTag(Constants.KEY_BOARD_FRAG);
        fragmentB.setPositionBoard(getStringBoard());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent before if");
        if(showResult){
            QuizResultFragment quizResultFragment = (QuizResultFragment) manager.findFragmentByTag(Constants.KEY_QUIZ_FRAG);;
            quizResultFragment.SGD.onTouchEvent(event);
            Log.i(TAG, "onTouchEvent");
        }
        boolean ret = super.dispatchTouchEvent(event);
        return ret;
    }

    private void finishQuiz(boolean isFinished) {
        Intent resultIntent = new Intent();
        if (isFinished){
            resultIntent.putExtra(Constants.KEY_QUIZ_RESULT, (Serializable)quiz);
            resultIntent.putExtra(Constants.KEY_QUIZ_SCORE ,score);
            setResult(RESULT_OK, resultIntent);
        } else {
            setResult(RESULT_CANCELED, resultIntent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            if (this.quiz.getQuestions().size() == this.position ){
                finishQuiz(true);
            } else {
                finishQuiz(false);
            }

        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}

package com.example.quizmaster.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.URL;

public class Constants {
    public static final String KEY_MYQUIZ = "MyQuiz";
    public static final  String KEY_CONTAINER_FRAG = "fragContainer";
    public static final  String KEY_BOARD_FRAG = "fragBoard";
    public static final  String KEY_QUIZ_FRAG = "fragQuiz";
    public static final  String KEY_QUIZ_RESULT = "quizResult";
    public static final  String KEY_QUIZ_SCORE = "quizScore";
    public static final  String KEY_IMAGE_LIST = "imageList";
    public static final int QUIZ_CODE = 1;


    public static Bitmap getBitmap(String s){
        URL newurl = null;
        Bitmap mIcon_val = null;
        try {
            newurl = new URL(s);
            mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mIcon_val;
    }

}

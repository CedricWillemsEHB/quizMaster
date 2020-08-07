package com.example.quizmaster.app;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizmaster.R;
import com.example.quizmaster.model.Question;
import com.example.quizmaster.model.QuizListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizMultiFragment extends Fragment {
    private final static String TAG = QuizMultiFragment.class.getSimpleName();

    View view;
    TextView tvQuestionTitle;
    Button btnPrevious, btnNext;
    private RadioGroup rbGroup;
    private RadioButton rb1, rb2, rb3;
    Question question;
    boolean firstClick;
    RadioButton rbSelected;
    int score;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quiz_multi, container, false);

        tvQuestionTitle = view.findViewById(R.id.tvQuestion_title);
        btnPrevious = view.findViewById(R.id.btn_previous);
        btnNext = view.findViewById(R.id.btn_next);
        rbGroup = view.findViewById(R.id.radio_group);
        rb1 = view.findViewById(R.id.radio_button_1);
        rb2 = view.findViewById(R.id.radio_button_2);
        rb3 = view.findViewById(R.id.radio_button_3);

        if(question != null){
            tvQuestionTitle.setText(question.getQuestion());
            List<String> stringList = new ArrayList<>();
            stringList.add(question.getChoice1());
            stringList.add(question.getChoice2());
            stringList.add(question.getChoice3());
            Collections.shuffle(stringList);

            rb1.setText(stringList.get(0));
            rb2.setText(stringList.get(1));
            rb3.setText(stringList.get(2));
        } else {
            Toast.makeText(view.getContext(), R.string.quiz_not_find, Toast.LENGTH_SHORT);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!firstClick) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(view.getContext(), R.string.please_select, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
        return view;
    }

    private void showNextQuestion() {
        //TODO change question
        QuizListener quizListener = (QuizListener) getActivity();
        quizListener.goNextQuiz(score);
    }

    private void checkAnswer() {
        firstClick = true;

        RadioButton rbSelected = view.findViewById(rbGroup.getCheckedRadioButtonId());
        Log.i(TAG, rbSelected.getText().toString() + " : " + question.getAnswer());
        if(rbSelected.getText().toString().equals(question.getAnswer())){
            score = 1;
        } else {
            score = 0;
        }
        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        if(rb1.getText().toString().equals(question.getAnswer())){
            rb1.setTextColor(Color.GREEN);
        } else if(rb2.getText().toString().equals(question.getAnswer())){
            rb2.setTextColor(Color.GREEN);
        } else if(rb3.getText().toString().equals(question.getAnswer())){
            rb3.setTextColor(Color.GREEN);
        }
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}

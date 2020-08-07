package com.example.quizmaster.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizmaster.R;
import com.example.quizmaster.model.Question;
import com.example.quizmaster.model.QuizListener;

public class QuizTextFragment extends Fragment {

    View view;
    TextView tvQuestionTitle;
    Button btnPrevious, btnNext;
    EditText etQuestionAnswer;
    Question question;
    int score;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quiz_text, container, false);

        tvQuestionTitle = view.findViewById(R.id.tvQuestion_title);
        btnPrevious = view.findViewById(R.id.btn_previous);
        btnNext = view.findViewById(R.id.btn_next);
        etQuestionAnswer = view.findViewById(R.id.etQuestion_answer);

        if(question != null){
            tvQuestionTitle.setText(question.getQuestion());
        } else {
            Toast.makeText(view.getContext(), R.string.quiz_not_find, Toast.LENGTH_SHORT);
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!etQuestionAnswer.getText().toString().isEmpty()) {
                    showNextQuestion();
                } else {
                    Toast.makeText(view.getContext(), R.string.please_write, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void showNextQuestion() {
        if(etQuestionAnswer.getText().toString().equals(question.getAnswer())){
            score = 1;
        } else {
            score = 0;
        }
        QuizListener quizListener = (QuizListener) getActivity();
        quizListener.goNextQuiz(score);
    }


    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}

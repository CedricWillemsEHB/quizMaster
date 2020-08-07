package com.example.quizmaster.app;

import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizmaster.R;
import com.example.quizmaster.model.LoadImage;
import com.example.quizmaster.model.QuizListener;

public class QuizResultFragment extends Fragment {
    private static final String TAG = QuizResultFragment.class.getSimpleName();

    View view;
    ImageView ivResult;
    Button btnGoBack;
    String urlImage;
    Matrix matrix = new Matrix();
    Float scale = 1f;
    ScaleGestureDetector SGD;

    public QuizResultFragment(String urlImage) {
        this.urlImage = urlImage;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_quiz_result, container, false);
        ivResult = view.findViewById(R.id.iv_result);
        btnGoBack = view.findViewById(R.id.btn_go_back);
        if (urlImage != null){
            if(!urlImage.isEmpty()){
                LoadImage loadImage = new LoadImage(this.ivResult);
                loadImage.execute(urlImage);
            }
        }
        SGD = new ScaleGestureDetector(view.getContext(), new ScaleListener());
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endQuiz();
            }
        });
        return view;
    }

    private void endQuiz() {
        QuizListener quizListener = (QuizListener) getActivity();
        quizListener.onClickEndQuiz();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale = scale * detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale,5f));
            matrix.setScale(scale,scale);
            ivResult.setImageMatrix(matrix);
            return true;
        }
    }


}

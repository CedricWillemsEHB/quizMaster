package com.example.quizmaster.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.R;
import com.example.quizmaster.adapter.MultiRowAdapter;
import com.example.quizmaster.model.Constants;
import com.example.quizmaster.model.MainListener;
import com.example.quizmaster.model.Quiz;
import com.example.quizmaster.model.QuizStatus;

import java.io.Serializable;
import java.util.List;

public class QuizListFragment extends Fragment implements MultiRowAdapter.MultiRowListener {
    private final static String TAG = QuizListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    View view;
    List<Quiz> quizList;
    boolean isFinished = false;
    int positionQuiz;

    public QuizListFragment(List<Quiz> quizList) {
        this.quizList = quizList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_quiz_list, container, false);

        // Add the following lines to create RecyclerView
        setUpRecyclerView();
        return view;
    }

    public List<Quiz> getQuizList() {
        return quizList;
    }

    public void setQuizList(List<Quiz> quizList) {
        this.quizList = quizList;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.rvQiuzList);
        MultiRowAdapter adapter = new MultiRowAdapter(view.getContext(), quizList, this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(view.getContext());
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
    }


    @Override
    public void onClickRow(final int position) {
        AlertDialog.Builder alert;
        switch (quizList.get(position).getQuizStatus()){
            case NON:
                //TODO: ask the player if they want to do the quiz
                alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle(R.string.new_quiz_title);
                alert.setMessage(R.string.try_this);
                alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startQuiz(position);
                    }
                });
                alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing
                    }
                });
                alert.create().show();
                break;
            case WON:
                //congrat the player
                alert = new AlertDialog.Builder(view.getContext());
                alert.setMessage(R.string.congratulation);
                alert.setNeutralButton(R.string.bravo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText( view.getContext(), R.string.still_champ, Toast.LENGTH_SHORT).show();
                        Log.i("NeutralClick","Ok");
                    }
                });
                alert.create().show();
                break;
            case LOSE:
                //ask the player if they want to try again
                alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle(R.string.you_sure);
                alert.setMessage(R.string.try_again);
                alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startQuiz(position);
                    }
                });
                alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing
                    }
                });
                alert.create().show();
                break;
        }
    }

    private void startQuiz(int position){
        Intent intent = new Intent(getContext(), QuizActivity.class);
        intent.putExtra(Constants.KEY_MYQUIZ, (Serializable) quizList.get(position));
        startActivityForResult(intent, Constants.QUIZ_CODE);
        Toast.makeText( view.getContext(), "Lets start then", Toast.LENGTH_SHORT).show();
        this.positionQuiz = position;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult " +requestCode);
        if (requestCode == Constants.QUIZ_CODE) {
            Log.i(TAG, "onActivityResult / requestCode");

            if (resultCode == getActivity().RESULT_OK) {
                Log.i(TAG, "onActivityResult / requestCode / RESULT_OK");

                Quiz quizResult = (Quiz) data.getSerializableExtra(Constants.KEY_QUIZ_RESULT);
                int score = data.getIntExtra( Constants.KEY_QUIZ_SCORE, 0);
                Log.i(TAG, quizResult.getTitle());
                if(score == quizResult.getQuestions().size()){
                    Log.i(TAG, "onActivityResult / requestCode / RESULT_OK / scoreWin");
                    quizResult.setQuizStatus(QuizStatus.WON);
                } else {
                    Log.i(TAG, "onActivityResult / requestCode / RESULT_OK / scoreLose");
                    quizResult.setQuizStatus(QuizStatus.LOSE);
                }
                quizList.set(this.positionQuiz, quizResult);
                MainListener mainListener = (MainListener) getActivity();
                mainListener.resetRecycleView(quizList);
            }
            if (resultCode == getActivity().RESULT_CANCELED) {
                Log.i(TAG, "onActivityResult / requestCode / RESULT_CANCELED");
                Toast.makeText(getContext(), R.string.quiz_not_saved,Toast.LENGTH_SHORT).show();
            }
        }
    }
}

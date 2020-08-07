package com.example.quizmaster.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quizmaster.R;

public class BoardFragment extends Fragment {
    TextView tvWelcome;
    TextView tvBoard;
    View view;
    String welcome, board;

    public BoardFragment(String welcome, String board) {
        this.welcome = welcome;
        this.board = board;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_board, container, false);
        setUpBoard();
        return view;
    }

    private void setUpBoard() {
        tvWelcome = (TextView) view.findViewById(R.id.tvWelcoming);
        if(welcome != null){
            tvWelcome.setText(welcome);
        } else {
            tvWelcome.setText(R.string.welcome);
        }
        tvBoard = (TextView) view.findViewById(R.id.tvBoard);
        if(board != null){
            tvBoard.setText(board);
        } else {
            tvBoard.setText("Test board");
        }
    }

    public String getWelcome() {
        return welcome;
    }

    private void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getBoard() {
        return board;
    }

    private void setBoard(String board) {
        this.board = board;
    }

    public void setData(String welcome, String board){
        setPositionBoard(board);
        setTitleBoard(welcome);
    }

    public void setPositionBoard(String board){
        setBoard(board);
        tvBoard.setText(this.board);
    }

    public void setTitleBoard(String title){
        setWelcome(title);
        tvWelcome.setText(this.welcome);
    }
}

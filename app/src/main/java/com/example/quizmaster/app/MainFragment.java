package com.example.quizmaster.app;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.quizmaster.R;

import java.util.Locale;

public class MainFragment extends Fragment {
    Button btnSettings;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        btnSettings = view.findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettings();
            }
        });
        return view;
    }

    private void showSettings() {
        //TODO change to fragment settings
        showChangeLanguageDialog();
    }
    private void showChangeLanguageDialog() {
        //array of languages to display in alert dialog
        final String[] listItems = {"Nederlands", "English", "Français"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        mBuilder.setTitle("Choose Language...");

        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    //Nederlands
                    setLocale("nl");
                    getActivity().recreate();
                } else if (i == 1) {
                    //Engels
                    setLocale("en");
                    getActivity().recreate();
                } else if (i == 2) {
                    //Engels
                    setLocale("FR");
                    getActivity().recreate();
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
        getActivity().getBaseContext().getResources().updateConfiguration(configuration, getActivity().getBaseContext().getResources().getDisplayMetrics());
        //save data to shared preferences
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", getActivity().MODE_PRIVATE).edit();
        editor.putString("My_Lang", language);
        editor.apply();
    }

}

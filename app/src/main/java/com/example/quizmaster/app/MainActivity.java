package com.example.quizmaster.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.quizmaster.R;
import com.example.quizmaster.model.Constants;
import com.example.quizmaster.model.Quiz;
import com.example.quizmaster.model.QuizDbHelper;
import com.example.quizmaster.model.MainListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.navigation.NavigationView;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    private FragmentManager manager;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    Fragment currentFragment;
    FrameLayout frameLayout;
    List<Quiz> quizList = new ArrayList<>();
    QuizDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);
        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init(null, null, null);
            SSLEngine engine = sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        manager = getSupportFragmentManager();
        dbHelper = new QuizDbHelper(this);
        quizList = dbHelper.getAllQuiz();
//        findViewById(R.id.text);
        setUpToolBar();
        setUpDrawer();
        frameLayout = (FrameLayout) findViewById(R.id.fragment_container);
        if (savedInstanceState == null) {
            currentFragment = new MainFragment();
            manager.beginTransaction().replace(R.id.fragment_container,
                    currentFragment,Constants.KEY_CONTAINER_FRAG).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        } else{
            currentFragment = manager.findFragmentById(R.id.fragment_container);
        }
    }

    @Override
    public void showImage(int imageID) {
        ImageView imageView = (ImageView) findViewById(R.id.img_full);
        imageView.setImageResource(imageID);
    }

    @Override
    public void resetRecycleView(List<Quiz> quizList) {
        this.quizList = quizList;
        dbHelper.updateDb(this.quizList);
        currentFragment = new QuizListFragment();
        manager.beginTransaction().replace(R.id.fragment_container,
                currentFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){

            case R.id.mi_add_quiz:
                //TODO go to a page to make new quiz
                quizList.addAll(Quiz.getData());
                dbHelper.updateDb(quizList);
                if(currentFragment.getClass().getSimpleName().equals(QuizListFragment.class.getSimpleName())){
                    resetRecycleView(quizList);
                }
                break;

            case  R.id.mi_settings:
                showChangeLanguageDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpDrawer() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //change actionbar
        toolbar.setTitle(getResources().getString(R.string.game_name));
//        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);
    }

    private void showChangeLanguageDialog(){
        //array of languages to display in alert dialog
        final  String[] listItems = {"Nederlands", "English", "Français"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                currentFragment = new MainFragment();
                manager.beginTransaction().replace(R.id.fragment_container,
                        currentFragment, Constants.KEY_CONTAINER_FRAG).commit();
                break;
            case R.id.nav_quiz:
                currentFragment = new QuizListFragment();
                manager.beginTransaction().replace(R.id.fragment_container,
                        currentFragment, Constants.KEY_CONTAINER_FRAG).commit();
                break;
            case R.id.nav_trophy:
                currentFragment = new TrophyFragment(quizList);
                manager.beginTransaction().replace(R.id.fragment_container,
                        currentFragment, Constants.KEY_CONTAINER_FRAG).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, R.string.share, Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, R.string.send, Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult " + requestCode);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
package com.example.quizmaster.app;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.quizmaster.R;
import com.example.quizmaster.adapter.TrophyAdapeter;
import com.example.quizmaster.model.Constants;
import com.example.quizmaster.model.LoadImage;
import com.example.quizmaster.model.Quiz;
import com.example.quizmaster.model.QuizStatus;
import com.example.quizmaster.model.ZoomOut;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TrophyFragment extends Fragment implements TrophyAdapeter.TrophyOnCLickListener {
    private static String TAG = TrophyFragment.class.getSimpleName();

    private ViewPager mViewPager;
    private List<String> mImageId;
    View view;
    ImageView imageView;
    Matrix matrix = new Matrix();
    Float scale = 1f;
    ScaleGestureDetector SGD;

    public TrophyFragment() {
    }

    public TrophyFragment(List<Quiz> quizList) {
        mImageId = setUpQuizToImage(quizList);
    }

    private List<String> setUpQuizToImage(List<Quiz> quizList) {
        List<String> stringList = new ArrayList<>();
        for(int i = 0; i < quizList.size(); i++){
            if(quizList.get(i).getQuizStatus() == QuizStatus.WON){
                stringList.add(quizList.get(i).getImage());
            }
        }
        return stringList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trophy, container, false);

        if (savedInstanceState != null) {
            mImageId = savedInstanceState.getStringArrayList(Constants.KEY_IMAGE_LIST);
        }

        int orientation = view.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            setUpRecyclerViewTrophy();
            imageView = view.findViewById(R.id.img_full);
            SGD = new ScaleGestureDetector(view.getContext(), new ScaleListener());
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            setUpViewPage();
            mViewPager.setPageTransformer(true, new  ZoomOut());
        }
        return view;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale = scale * detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale,5f));
            matrix.setScale(scale,scale);
            imageView.setImageMatrix(matrix);
            return true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState()");

        outState.putStringArrayList(Constants.KEY_IMAGE_LIST, (ArrayList<String>) mImageId);
    }

    private void setUpRecyclerViewTrophy() {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvTrophy);
        TrophyAdapeter trophyAdapeter = new TrophyAdapeter(view.getContext(),mImageId, this);
        recyclerView.setAdapter(trophyAdapeter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(view.getContext());
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

    }

    private void setUpViewPage() {
        // Locate the ViewPager in activity_main.xml
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        // Pass params to ViewPagerAdapter Class
        PagerAdapter adapter = new ViewPagerAdapter(view.getContext(), mImageId);

        // Bind the ViewPager Adapter to the ViewPager
        mViewPager.setAdapter(adapter);
    }


    @Override
    public void onTrophyClick(int position) {
        Log.e(TAG, "onTrophyClick");
        LoadImage loadImage = new LoadImage(imageView);
        loadImage.execute(mImageId.get(position));
        //imageView.setImageBitmap(Constants.getBitmap(mImageId.get(position)));
    }
}

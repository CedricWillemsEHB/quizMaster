package com.example.quizmaster.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.example.quizmaster.R;
import com.example.quizmaster.model.Constants;
import com.example.quizmaster.model.LoadImage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 *  Source :  https://app.pluralsight.com/library/courses/android-animations/table-of-contents
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;

    private List<String> mImageURL;

    private LayoutInflater mInflater;

    public ViewPagerAdapter(Context context, List<String> imageId) {

        this.mContext = context;
        this.mImageURL = imageId;
    }

    @Override
    public int getCount() {
        return mImageURL.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        ImageView imageFashion;

        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = mInflater.inflate(R.layout.viewpager_item, container,
                false);


        // Locate the ImageView in viewpager_item.xml
        imageFashion = (ImageView)itemView.findViewById(R.id.imageViewItem);

        // set the ImageView resource
        LoadImage loadImage = new LoadImage(imageFashion);
        loadImage.execute(mImageURL.get(position));
        //imageFashion.setImageBitmap(Constants.getBitmap(mImageURL[position]));

        // Add viewpager_item.xml to ViewPager
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((RelativeLayout) object);

    }

}

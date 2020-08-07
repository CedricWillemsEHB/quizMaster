package com.example.quizmaster.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.R;
import com.example.quizmaster.model.Constants;
import com.example.quizmaster.model.LoadImage;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class TrophyAdapeter  extends RecyclerView.Adapter<TrophyAdapeter.MyViewHolder>{
    private static final String TAG = MultiRowAdapter.class.getSimpleName();

    private List<String> mData;
    private LayoutInflater mInflater;
    private TrophyOnCLickListener mTrophyOnCLickListener;

    public TrophyAdapeter(Context context, List<String> mData, TrophyOnCLickListener trophyOnCLickListener) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.mTrophyOnCLickListener = trophyOnCLickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup oneRow = (ViewGroup) mInflater.inflate(R.layout.list_item_trophy, parent, false);
        TrophyAdapeter.MyViewHolder holderPrime = new TrophyAdapeter.MyViewHolder(oneRow, mTrophyOnCLickListener);
        return holderPrime;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String currentImg = mData.get(position);
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgThumb, imgFull;
        String imageSource;
        TrophyOnCLickListener trophyOnCLickListener;
        public MyViewHolder(@NonNull View itemView, TrophyOnCLickListener trophyOnCLickListener) {
            super(itemView);
            imgThumb = (ImageView) itemView.findViewById(R.id.img_trophy);
            imgFull = (ImageView) itemView.findViewById(R.id.img_full);
            this.trophyOnCLickListener = trophyOnCLickListener;
            itemView.setOnClickListener(this);
        }

        public String getImageSource() {
            return imageSource;
        }

        public void setImageSource(String imageSource) {
            this.imageSource = imageSource;
        }

        public ImageView getImgThumb() {
            return imgThumb;
        }

        public void setImgThumb(ImageView imgThumb) {
            this.imgThumb = imgThumb;
        }


        public void setData(String image) {
            URL newurl = null;
            Bitmap mIcon_val = null;
            LoadImage loadImage = new LoadImage(imgThumb);
            loadImage.execute(image);
            //imgThumb.setImageBitmap(Constants.getBitmap(image));
            imageSource = image;
        }

        @Override
        public void onClick(View view) {
            trophyOnCLickListener.onTrophyClick(getAdapterPosition());
        }
    }
    public interface TrophyOnCLickListener{
        void onTrophyClick(int position);
    }
}

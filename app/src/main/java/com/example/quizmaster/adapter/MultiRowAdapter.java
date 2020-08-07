package com.example.quizmaster.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.R;
import com.example.quizmaster.model.LoadImage;
import com.example.quizmaster.model.Quiz;

import java.util.List;


public class MultiRowAdapter extends RecyclerView.Adapter<MultiRowAdapter.MyViewHolder> {
    private static final String TAG = MultiRowAdapter.class.getSimpleName();
    private static final int NON_ROW = 0;
    private static final int WON_ROW = 1;
    private static final int LOSE_ROW = 2;


    private List<Quiz> mData;
    private LayoutInflater mInflater;
    private MultiRowListener mMultiRowListener;
    public MultiRowAdapter(Context context, List<Quiz> mData, MultiRowListener mMultiRowListener) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.mMultiRowListener = mMultiRowListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case NON_ROW:
                Log.i(TAG, "onCreateViewHolder : non");
                ViewGroup noneRow = (ViewGroup) mInflater.inflate(R.layout.list_item_quiz, parent, false);
                MyViewHolder_NON nonePrime = new MyViewHolder_NON(noneRow, mMultiRowListener);
                return nonePrime;
            case WON_ROW:
                Log.i(TAG, "onCreateViewHolder : won");
                ViewGroup primeRow = (ViewGroup) mInflater.inflate(R.layout.list_item_quiz_won, parent, false);
                MyViewHolder_WON holderPrime = new MyViewHolder_WON(primeRow, mMultiRowListener);
                return holderPrime;

            case LOSE_ROW:
                Log.i(TAG, "onCreateViewHolder : lost");
                ViewGroup nonPrimeview = (ViewGroup) mInflater.inflate(R.layout.list_item_quiz_lose, parent, false);
                MyViewHolder_LOST holderNonPrime = new MyViewHolder_LOST(nonPrimeview, mMultiRowListener);
                return holderNonPrime;

            default:
                Log.i(TAG, "onCreateViewHolder : default");
                ViewGroup defaultview = (ViewGroup) mInflater.inflate(R.layout.list_item_quiz, parent, false);
                MyViewHolder_NON holderDefault = new MyViewHolder_NON(defaultview, mMultiRowListener);
                return holderDefault;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder " + position);
        Quiz currentObj = mData.get(position);

        switch (holder.getItemViewType()){
            case NON_ROW:
                Log.i(TAG, "onBindViewHolder : non");
                MyViewHolder_NON holder_none = (MyViewHolder_NON) holder;
                holder_none.setData(currentObj);
                break;
            case WON_ROW:
                Log.i(TAG, "onBindViewHolder : won");
                MyViewHolder_WON holder_prime = (MyViewHolder_WON) holder;
                holder_prime.setData(currentObj);
                break;
            case LOSE_ROW:
                Log.i(TAG, "onBindViewHolder : lost");
                MyViewHolder_LOST holder_non_prime = (MyViewHolder_LOST) holder;
                holder_non_prime.setData(currentObj);
                break;
        }
    }
    @Override
    public int getItemViewType(int position) {
        Quiz currentObj = mData.get(position);
        switch (currentObj.getQuizStatus()){
            case WON:
                return WON_ROW;
            case NON:
                return NON_ROW;
            case LOSE:
                return LOSE_ROW;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgThumb;
        MultiRowListener mMultiRowListener;
        public MyViewHolder(@NonNull View itemView, MultiRowListener mMultiRowListener) {
            super(itemView);
            this.mMultiRowListener = mMultiRowListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mMultiRowListener.onClickRow(getAdapterPosition());
        }
    }

    class MyViewHolder_WON extends MyViewHolder  {
        TextView title;
        TextView descreption;
        ImageView imageView;

        public MyViewHolder_WON(@NonNull View itemView, MultiRowListener mMultiRowListener) {
            super(itemView, mMultiRowListener);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            descreption = (TextView) itemView.findViewById(R.id.tvDescreption);
            imageView = (ImageView) itemView.findViewById(R.id.img_row);
        }

        public void setData(Quiz current) {
            this.title.setText(current.getTitle());
            this.descreption.setText(R.string.you_won);
            LoadImage loadImage = new LoadImage(this.imageView);
            loadImage.execute(current.getImage());
            //this.imageView.setImageBitmap(Constants.getBitmap(current.getImage()));
        }
    }

    class MyViewHolder_LOST extends MyViewHolder{
        TextView title;
        TextView descreption;

        public MyViewHolder_LOST(@NonNull View itemView, MultiRowListener mMultiRowListener) {
            super(itemView, mMultiRowListener);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            descreption = (TextView) itemView.findViewById(R.id.tvDescreption);
        }

        public void setData(Quiz current) {
            this.title.setText(current.getTitle());
            this.descreption.setText(R.string.you_lost);
        }
    }

    class MyViewHolder_NON extends MyViewHolder{
        TextView title;
        TextView descreption;

        public MyViewHolder_NON(@NonNull View itemView, MultiRowListener mMultiRowListener) {
            super(itemView, mMultiRowListener);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            descreption = (TextView) itemView.findViewById(R.id.tvDescreption);
        }

        public void setData(Quiz current) {
            this.title.setText(current.getTitle());
            this.descreption.setText(R.string.new_quiz);
        }
    }

    public interface MultiRowListener {
        void onClickRow(int position);
    }

}

package com.github.sneepin.itisworkwithimage.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.sneepin.itisworkwithimage.R;
import com.github.sneepin.itisworkwithimage.content.Image;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ImagesRecyclerViewAdapter extends RecyclerView.Adapter<ImagesRecyclerViewAdapter.ViewHolder>  {

    private List<Image> mDataSet;
    private Context mContext;
    private OnImageClickListener mOnImageClickListener;

    public ImagesRecyclerViewAdapter(@NonNull Context context, @NonNull List<Image> imageList) {
        mContext = context;
        mDataSet = imageList;
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        mOnImageClickListener = onImageClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Image image = mDataSet.get(position);

        holder.mMainImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnImageClickListener.onClick(image.getUrl());
            }
        });

        Glide.with(mContext)
                .load(image.getUrl())
                .bitmapTransform(new CropCircleTransformation(mContext))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.mMainImageView);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mMainImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mMainImageView = (ImageView) itemView.findViewById(R.id.main_image_view);
        }
    }

    public interface OnImageClickListener {
        void onClick(String imageUrl);
    }
}

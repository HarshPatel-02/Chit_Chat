package com.example.instagram;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StoryDataAdapter extends RecyclerView.Adapter<StoryDataAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Bitmap> mImages;
    private ArrayList<String> mNames;

    public StoryDataAdapter(Context context, ArrayList<Bitmap> images, ArrayList<String> names) {
        mContext = context;
        mImages = images;
        mNames = names;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.all_user_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap image = mImages.get(position);
        String name = mNames.get(position);

        holder.userstory.setImageBitmap(image);
        holder.username.setText(name);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userstory;
        TextView username;

        public ViewHolder(View itemView) {
            super(itemView);
            userstory = itemView.findViewById(R.id.homepageuserstoryphoto);
            username = itemView.findViewById(R.id.homepagestoryusername);
        }
    }
}

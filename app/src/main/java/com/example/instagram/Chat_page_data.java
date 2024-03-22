package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Chat_page_data extends BaseAdapter {

    private Context context;
    private ArrayList<Bitmap> mImages;
    private ArrayList<String> mNames;

    public Chat_page_data(Context mContext, ArrayList<Bitmap> mImages, ArrayList<String> mNames) {
        this.context = mContext;
        this.mImages = mImages;
        this.mNames = mNames;
    }

    @Override
    public int getCount() {
        return mNames.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(R.layout.data_alluser_chat,null);


        ImageView ing=(ImageView)view.findViewById(R.id.login_logo);
        TextView t1=(TextView)view.findViewById(R.id.username_chat);
        ing.setImageBitmap(mImages.get(i));
        t1.setText(mNames.get(i));



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





            }
        });

        return view;

    }
}

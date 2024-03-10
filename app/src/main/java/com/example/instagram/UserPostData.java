package com.example.instagram;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserPostData extends BaseAdapter {

    Context context;
    ArrayList<String> username=new ArrayList<>();
    ArrayList<Bitmap> img=new ArrayList<>();

    public UserPostData(Context context, ArrayList<String> username, ArrayList<Bitmap> img) {
        this.context = context;
        this.username = username;
        this.img = img;
    }

    @Override
    public int getCount() {
        return username.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(R.layout.all_user_post,null);
        ImageView imageView=(ImageView) view.findViewById(R.id.userpostimage);
        TextView txt=(TextView) view.findViewById(R.id.alluserpage_userid);

        // Check if index i is within the bounds of the lists
        if (i < username.size() && i < img.size()) {
            imageView.setImageBitmap(img.get(i));
            txt.setText(username.get(i));
        } else {
            // Handle the case when index i is out of bounds
            // For example, set default values or handle the view accordingly
        }

        return view;


    }
}

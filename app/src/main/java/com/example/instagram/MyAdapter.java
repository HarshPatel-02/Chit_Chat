package com.example.instagram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context context;


    ArrayList<Bitmap> imgesa = new ArrayList();

    public MyAdapter(Context context, ArrayList imgesa) {
        this.context = context;
        this.imgesa = imgesa;
    }

    @Override
    public int getCount() {
        return imgesa.size();
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
        view=layoutInflater.inflate(R.layout.grid_item,null);

        ImageView img=(ImageView)view.findViewById(R.id.gridimage);
       img.setImageBitmap(imgesa.get(i));
        return view;
    }
}
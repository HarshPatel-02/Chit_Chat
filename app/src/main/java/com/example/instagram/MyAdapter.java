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

    private ArrayList<DataClass> dataList;
    private Context context;

    MyAdapter(ArrayList<DataClass> dataList, Context context){
        this.dataList=dataList;
        this.context=context;
    }

    LayoutInflater layoutInflater;
    @Override
    public int getCount() {
        return dataList.size();
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
        if (layoutInflater==null){
            layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        if(view==null){
            view=layoutInflater.inflate(R.layout.grid_item,null);
        }

        ImageView imgviewad=view.findViewById(R.id.gridimage);
        Bitmap bitmap = BitmapFactory.decodeFile(dataList.get(i).getImageURL());

        // Set bitmap to ImageView
        imgviewad.setImageBitmap(bitmap);
        return view;
    }
}

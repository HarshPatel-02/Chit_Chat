package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class my_adapter_alluser extends BaseAdapter {

    Context context;


    ArrayList<Bitmap> userimg = new ArrayList();

    ArrayList<String> fullname=new ArrayList();
    ArrayList<String> username=new ArrayList();

    public my_adapter_alluser(Context context, ArrayList<Bitmap> userimg, ArrayList<String> fullname, ArrayList<String> username) {
        this.context = context;
        this.userimg = userimg;
        this.fullname = fullname;
        this.username = username;
    }
    public void setData(ArrayList<Bitmap> userimg, ArrayList<String> fullname, ArrayList<String> username) {
        this.userimg = userimg;
        this.fullname = fullname;
        this.username = username;
        notifyDataSetChanged(); // Notify adapter of data change
    }
    @Override
    public int getCount() {
        return fullname.size();
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
            view=layoutInflater.inflate(R.layout.grid_item_alluser,null);


            ImageView ing=(ImageView)view.findViewById(R.id.as_user_img);
            TextView t1=(TextView)view.findViewById(R.id.as_username);
            TextView t2=(TextView)view.findViewById(R.id.as_userfullname);
            ing.setImageBitmap(userimg.get(i));
            t1.setText(username.get(i));
            t2.setText(fullname.get(i));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("MyAdapter", "Grid item clicked at position: " + fullname.get(i));
                    Intent intent=new Intent(context,Other_User_profile.class);
                    intent.putExtra("username", username.get(i));
                    context.startActivity(intent);



                }
            });

            return view;
        }
}

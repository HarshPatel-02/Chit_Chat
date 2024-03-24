package com.example.instagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;




    private MediaPlayer mediaPlayer1;
    private Context context;
    private List<String> mchat;

    private List<String> sender;

    private SharedPreferences sharedPreferences;
    private String ownusername;

    Bitmap bitmap;

    public MessageAdapter(Context context, List<String> muser,List<String> sender,Bitmap bitmap1) {
        this.context = context;
        this.mchat = muser;
        this.sender=sender;

        mediaPlayer1 = MediaPlayer.create(context, R.raw.receive_sound);
        bitmap=bitmap1;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String message = mchat.get(position);
        holder.msg.setText(message);

        // Check if the ImageView is not null before setting the bitmap
        if (holder.img != null && bitmap != null) {
            holder.img.setImageBitmap(bitmap);
        }

      //  mediaPlayer1.start();
    }


    @Override
    public int getItemCount() {
        return mchat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView msg;
        private ImageView img;
        //private LinearLayout main;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            msg=itemView.findViewById(R.id.show_message);
            img=itemView.findViewById(R.id.profile_userimg);

            //main=itemView.findViewById(R.id.mainMessageLayoyt);
        }
    }
    @Override
    public int getItemViewType(int position){

        sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        ownusername = sharedPreferences.getString("usernm", "n");
        //if (mchat.get(position).getSender())
        if (sender.get(position).equals(ownusername)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}

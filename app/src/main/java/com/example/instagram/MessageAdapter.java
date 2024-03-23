package com.example.instagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;



    private Context context;
    private List<String> mchat;

    private String sender;

    private SharedPreferences sharedPreferences;
    private String ownusername;


    public MessageAdapter(Context context, List<String> muser,String sender) {
        this.context = context;
        this.mchat = muser;
        this.sender=sender;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (sender.equals(ownusername)) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String message=mchat.get(position);
        holder.msg.setText(message);



    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView msg;
        //private LinearLayout main;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            msg=itemView.findViewById(R.id.show_message);
            //main=itemView.findViewById(R.id.mainMessageLayoyt);
        }
    }
    @Override
    public int getItemViewType(int position){

        sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        ownusername = sharedPreferences.getString("usernm", "n");
        //if (mchat.get(position).getSender())

        return position ;
    }
}

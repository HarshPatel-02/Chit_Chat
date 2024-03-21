package com.example.instagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserPostData extends RecyclerView.Adapter<UserPostData.ViewHolder> {

    boolean userExists=false;
    private String usernametolike;


    String userposttolike;
    private Context context;
    private ArrayList<String> username = new ArrayList<>();
    private ArrayList<Bitmap> img = new ArrayList<>();;
    private ArrayList<Boolean> isliked = new ArrayList<>();
    private ArrayList<String> postname = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String ownusername;

    public UserPostData(Context context, ArrayList<String> username, ArrayList<Bitmap> img,ArrayList<Boolean> isliked,ArrayList<String> postname) {
        this.context = context;
        this.username = username;
        this.img = img;
        this.isliked=isliked;
        this.postname=postname;
        sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        ownusername = sharedPreferences.getString("usernm", "n");

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_user_post, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to ViewHolder
        Bitmap image = img.get(position);
        String name = username.get(position);
        boolean isLiked = isliked.get(position); // Get liked state for this item

        userposttolike=postname.get(position);
        holder.userpost.setImageBitmap(image);
        holder.username.setText(name);

        // Set appropriate like state
        if (isLiked) {
            holder.borderheart.setImageResource(R.drawable.red_heart);
            // holder.centerhraet.setVisibility(View.VISIBLE);
        } else {
            holder.borderheart.setImageResource(R.drawable.heart_border);
            // holder.centerhraet.setVisibility(View.INVISIBLE);
        }

        GestureDetector gestureDetector = new GestureDetector(holder.itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            Animation zoomInAnim = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.like_animation_zomin);
            Animation zoomOutAnim = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.like_animation_zomout);

            @Override
            public boolean onDoubleTap(MotionEvent event) {
                // Get the adapter position of the ViewHolder
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Toggle like state
                    isliked.set(adapterPosition, true);
                    notifyItemChanged(adapterPosition);

                    holder.borderheart.startAnimation(zoomInAnim);
                    holder.borderheart.setImageResource(R.drawable.red_heart);

                    holder.centerhraet.startAnimation(zoomOutAnim);
                    usernametolike= name;
                    //userposttolike=
                    updatetodatabase();
                }
                return true;
            }
        });

        holder.userpost.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
        holder.borderheart.setOnClickListener(new View.OnClickListener() {
            int pos=holder.getAdapterPosition();
            @Override
            public void onClick(View view) {
                holder.borderheart.setImageResource(R.drawable.heart_border);
                isliked.set(pos, false);

            }
        });
    }


    public int getItemCount() {
        return img.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userpost, n;
        TextView username;

        ImageView borderheart,centerhraet;


        public ViewHolder(View itemView) {
            super(itemView);
            userpost = itemView.findViewById(R.id.userpostimage);
            username = itemView.findViewById(R.id.alluserpage_userid);
            borderheart = itemView.findViewById(R.id.likebtn);
            centerhraet=itemView.findViewById(R.id.white_heart);
            // n = itemView.findViewById(R.id.tempoooooo);
        }
    }

    private void updatetodatabase() {
        Toast.makeText(context.getApplicationContext(), userposttolike.toString(),Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        Query followQuery = databaseReference.orderByChild("img").equalTo(userposttolike);
        followQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final boolean[] userExists = {false};
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    DatabaseReference followRef = databaseReference.child(key).child("userlike");
                    Query usernameQuery = followRef.orderByChild("username").equalTo(ownusername);
                    usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                            } else {
                                gotolike();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled if needed
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // This toast will not show the correct username, move it inside onDataChange if needed.
        Toast.makeText(context.getApplicationContext(), usernametolike, Toast.LENGTH_SHORT).show();
    }

    private void gotolike() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        Query followQuery = databaseReference.orderByChild("img").equalTo(userposttolike);
        followQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String key=dataSnapshot.getKey();
                    DatabaseReference followRef = databaseReference.child(key).child("userlike");

                    DatabaseReference likeope = followRef.push();
                    Map<String, Object> data = new HashMap<>();
                    data.put("username", ownusername);
                    likeope.setValue(data);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
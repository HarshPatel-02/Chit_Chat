package com.example.instagram;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DataEmployee {
    private DatabaseReference databaseReference;

    public DataEmployee() {
            databaseReference = FirebaseDatabase.getInstance().getReference("Posts");; // Adjust this to match your database structure
    }

    public Query get(String key,String username) {
        // Construct and return a query to retrieve data based on username

        if (key==null){
            return databaseReference.orderByChild("userId").equalTo(username).limitToFirst(8);

        }
        return  databaseReference.orderByChild("userId").equalTo(username).startAfter(key).limitToFirst(8);



    }

    // Implement other methods such as add, update, and remove as needed
}


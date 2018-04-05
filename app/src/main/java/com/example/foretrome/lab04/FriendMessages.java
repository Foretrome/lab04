package com.example.foretrome.lab04;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FriendMessages extends AppCompatActivity {

    TextView title;
    ListView listView;

    //Firebase Database reference
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_messages);

        title = findViewById(R.id.friendName);
        listView = findViewById(R.id.listView2);

        //Firebase Database
        myRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        
    }
}

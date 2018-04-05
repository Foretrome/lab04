package com.example.foretrome.lab04;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class Friends extends android.support.v4.app.Fragment {

    //ListView that holds Friends
    ListView friendsList;

    //Firebase Database reference
    private DatabaseReference myRef;

    //Array that holds Friends. Used in friendsList.
    private ArrayList<String> friendsArray;

    //List adapter
    private ArrayAdapter<String> arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends_layout, container, false);

        //Sets up the lists and their adapter
        friendsList = rootView.findViewById(R.id.friendsList);
        friendsArray = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.chat_item, friendsArray);
        friendsList.setAdapter(arrayAdapter);

        //Firebase Database
        myRef = FirebaseDatabase.getInstance().getReference();

        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                String temp = (String) adapterView.getItemAtPosition(i);
                intent.putExtra("friend", temp);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    MessageToDb friends = ds.getValue(MessageToDb.class);
                    assert friends != null;
                    //Add only unique usernames to the list
                    if(!friendsArray.contains(friends.getUser())){
                        friendsArray.add(friends.getUser());
                        Collections.sort(friendsArray);
                        arrayAdapter.notifyDataSetChanged();
                    }else {
                        Log.d("TEST", "Friend: " + friends.getUser() + " is already in the list and will therefore not be added");
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //TO BE IMPLEMENTED LATER
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //TO BE IMPLEMENTED LATER
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //TO BE IMPLEMENTED LATER
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TO BE IMPLEMENTED LATER
            }
        });
    }
}

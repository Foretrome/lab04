package com.example.foretrome.lab04;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Objects;

public class Chat extends android.support.v4.app.Fragment {

    //The user's username
    private String username;

    //Chat submit button
    Button chatSubmit;
    //Chat input-field
    TextView chatInn;
    //Chat listView
    ListView listView;

    //Firebase Database reference
    private DatabaseReference myRef;

    //Array that holds received messages
    private ArrayList<String> messageItems;
    private ArrayAdapter<String> arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.chat_layout, container, false);

        //Chat input-field
        chatInn = rootView.findViewById(R.id.chatInput);
        //Chat listView
        listView = rootView.findViewById(R.id.listView);

        messageItems = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.chat_item, messageItems);
        listView.setAdapter(arrayAdapter);

        //Firebase Database
        myRef = FirebaseDatabase.getInstance().getReference();

        //Gets the username from the MainActivity
        Bundle bundle = getArguments();
        username = bundle.getString("username");
        Log.d("TEST", "Got this username in the fragment: " + username);

        //Chat submit button
        chatSubmit = rootView.findViewById(R.id.chatSubmitBtn);
        chatSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
               if(!submitMessage()){
                   Toast.makeText(getActivity(), "ERROR - faild to submit message", Toast.LENGTH_SHORT).show();
               }else {
                   chatInn.setText(null);   //Clears the input-field
                   Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();
               }
               /* CODE FOR HIDING KEYBOARD
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);

                assert inputManager != null;
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                        */
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
                    MessageToDb message = ds.getValue(MessageToDb.class);
                    //Log.d("TEST", "message = " + message.getMessage());
                    assert message != null;
                    messageItems.add(message.getUser() + ": " + message.getMessage());
                    arrayAdapter.notifyDataSetChanged();
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

    /**
     * submits a message to the db
     */
    private boolean submitMessage(){
        //Checks if the user have entered a message
        String msg = chatInn.getText().toString();
        if(Objects.equals(msg, "")){
            Log.d("TEST", "sub failed - empty message");
            return false;
        }else {
            Log.d("TEST", "message was not empty");
            MessageToDb messages = new MessageToDb(msg, username);
            myRef.push().child("messages").setValue(messages);

            return true;
        }
    }

    private void notification(String title, String text, int priority){
        String channelName = "Chat";
        String channelDescription = "description";

        String channelId = "chatChannel";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), channelId)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(priority);

        //Set up notification manager
       // NotificationManager mNotific = (NotificationManager) Bitmap.Config.context.getSystemService(getContext().NOTIFICATION_SERVICE);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

        //*** Notification Channel ***
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            mChannel.setDescription(channelDescription);
            mChannel.canShowBadge();
            mChannel.setShowBadge(true);
            //notificationManager.createNotificationChannel(mChannel);
        }


    }

}

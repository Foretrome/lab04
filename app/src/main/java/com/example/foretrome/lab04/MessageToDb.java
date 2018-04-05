package com.example.foretrome.lab04;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageToDb {
    //THESE HAVE TO BE PUBLIC FOR FireDatabase TO WORK
    public String message;
    public String date;
    public String user;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Constructor that takes a message and an username
     * @param messageToDb - the message
     * @param username - the username
     */
    MessageToDb(String messageToDb, String username){
        this.message = messageToDb;
        this.user = username;
        Date date = new Date();
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.UK);
        this.date = sfd.format(date);
        Log.d("TEST", "Creating a new message: " + username + " " + messageToDb + " " + this.date);
    }

    MessageToDb(){}

}
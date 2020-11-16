package com.themusicians.musiclms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 *
 * <p>Contributors: Shifan He Created by Shifan He on 2020-11-12
 *
 *
 */

public class Chat extends AppCompatActivity {
    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // User is already signed in. Therefore, display
        // a welcome Toast
        Toast.makeText(this,
                "Welcome " + FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getDisplayName(),
                Toast.LENGTH_LONG)
                .show();

        // Load chat room contents
        displayChatMessages();


        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of com.themusicians.musiclms.com.themusicians.musiclms.ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );

                // Clear the input
                input.setText("");
            }
        });

    }
    private void displayChatMessages() {}
    //Suppose you want to retrieve "chats" in your Firebase DB:
    Query query = FirebaseDatabase.getInstance().getReference().child("chats");
    //The error said the constructor expected FirebaseListOptions - here you create them:
    FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatmMessage>()
            .setQuery(query, ChatMessage.class)
            .setLayout(android.R.layout.message)
            .build();
//Finally you pass them to the constructor here:
    adapter = new FirebaseListAdapter<ChatMessage>(options){
        @Override
        protected void populateView(View v, ChatMessage model, int position) {
// Get references to the views of message.xml
            TextView messageText = (TextView)v.findViewById(R.id.message_text);
            TextView messageTime = (TextView)v.findViewById(R.id.message_time);

// Set their text
            messageText.setText(model.getMessageBody());
// Format the date before showing it
            messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
        };

}
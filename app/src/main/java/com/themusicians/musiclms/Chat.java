package com.themusicians.musiclms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.themusicians.musiclms.entity.Attachment.ChatMessage;

public class Chat extends AppCompatActivity {
    Button Return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toast.makeText(this,
            "Welcome " + FirebaseAuth.getInstance()
                .getCurrentUser()
                .getDisplayName(),
            Toast.LENGTH_LONG)
            .show();

        // Load chat room contents
        displayChatMessages();

        // Allow user to post chat message
        FloatingActionButton fab =
            (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText messageInput = findViewById(R.id.input);

                // Create new message to be stored
                ChatMessage newMessage = new ChatMessage();
                newMessage.setMessageText(messageInput.getText().toString());
                newMessage.setMessageUser(FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getUid());

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                    .getReference()
                    .push()
                    .setValue(newMessage);

                // Clear the input
              messageInput.setText("");
            }
        });

    }

   public void ReturnMain(View view) {
      Intent MainPage = new Intent(this, MainActivity.class);
      startActivity(MainPage);
   }

    private void displayChatMessages() {

    }
}
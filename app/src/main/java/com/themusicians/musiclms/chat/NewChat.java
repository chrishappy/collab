package com.themusicians.musiclms.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;

import java.util.HashMap;

public class NewChat extends AppCompatActivity {

  EditText textMessage;
  Button sendButton;

  FirebaseUser currentUser;
  DatabaseReference reference, toRef;

  String toMessage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_chat_main);

    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    reference = FirebaseDatabase.getInstance().getReference().child("node__user");
    toRef = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("recentText");

    textMessage = findViewById(R.id.messageBox);
    sendButton = findViewById(R.id.sendMessage);

    toRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        toMessage = snapshot.getValue(String.class);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
    sendButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String msg = textMessage.getText().toString();
        if(!msg.equals("")){
          sendMessage(currentUser.getUid(), toMessage ,msg);
        }
      }
    });

  }

  private void sendMessage(String sender, String receiver, String message){
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("sender", sender);
    hashMap.put("receiver", receiver);
    hashMap.put("message", message);

    reference.child("Chats").push().setValue(hashMap);
  }
}
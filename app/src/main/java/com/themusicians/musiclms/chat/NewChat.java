package com.themusicians.musiclms.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewChat extends AppCompatActivity {

  EditText textMessage;
  Button sendButton;

  FirebaseUser currentUser;
  DatabaseReference reference, toRef;

  ChatAdapter chatAdapter;
  List<ChatClass> chatList;

  RecyclerView recyclerView;

  String toMessageID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_chat_main);

    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    reference = FirebaseDatabase.getInstance().getReference().child("node__user");
    toRef = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("recentText");

    textMessage = findViewById(R.id.messageBox);
    sendButton = findViewById(R.id.sendMessage);

    recyclerView = findViewById(R.id.chatRecycler);
    recyclerView.setHasFixedSize(true);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
    linearLayoutManager.setStackFromEnd(true);
    recyclerView.setLayoutManager(linearLayoutManager);

    toRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        toMessageID = snapshot.getValue(String.class);
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
          sendMessage(currentUser.getUid(), toMessageID, msg);
        }
        textMessage.setText("");
      }
    });

    reference = FirebaseDatabase.getInstance().getReference().child("node__user");
    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {

        readMessages(currentUser.getUid(), toMessageID);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

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

  private void readMessages(String myId, String userId){
    chatList = new ArrayList<>();

    reference = FirebaseDatabase.getInstance().getReference().child("Chats");
    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        chatList.clear();
        for(DataSnapshot ds : snapshot.getChildren()){
          ChatClass chat = ds.getValue(ChatClass.class);
          if(chat.getReceiver().equals(myId) && chat.getSender().equals(userId) || chat.getReceiver().equals(userId) && chat.getSender().equals(myId)){
            chatList.add(chat);
          }

          chatAdapter = new ChatAdapter(chatList, NewChat.this);
          recyclerView.setAdapter(chatAdapter);

        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }
}
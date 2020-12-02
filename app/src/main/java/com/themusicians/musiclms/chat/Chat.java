package com.themusicians.musiclms.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.Notifications.Client;
import com.themusicians.musiclms.Notifications.Data;
import com.themusicians.musiclms.Notifications.MyResponse;
import com.themusicians.musiclms.Notifications.Sender;
import com.themusicians.musiclms.Notifications.Token;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat extends AppCompatActivity {

  EditText textMessage;
  Button sendButton;

  FirebaseUser currentUser;
  DatabaseReference reference, toRef;

  ChatAdapter chatAdapter;
  List<ChatClass> chatList;

  RecyclerView recyclerView;

  String toMessageID, toMessageName;
  String userId;
  APIService apiService;
  boolean notify = true;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_chat_main);
    apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
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
        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("name");
        r.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            toMessageName = snapshot.getValue(String.class);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
        });
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

    sendButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        notify = true;
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

    final String msg = textMessage.getText().toString();
    reference = FirebaseDatabase.getInstance().getReference("node__user").child(currentUser.getUid());
    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        if(notify){
          sendNotification(toMessageID,user.getUid(),msg);
        }
        notify = false;
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

  }
  private void sendNotification(String receiver, String username, String message){
    DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
    Query query = tokens.orderByKey().equalTo(receiver);
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for(DataSnapshot snapshot :dataSnapshot.getChildren()){
          Token token = snapshot.getValue(Token.class);
          Data data = new Data(currentUser.getUid(),R.mipmap.ic_launcher,username+":"+message,"new message",userId);

          Sender sender = new Sender(data, token.getToken());

          apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
              Log.d("random","randomness");
              if(response.code() == 200){

                if(response.body().success !=1){
                  Toast.makeText(Chat.this,"failed",Toast.LENGTH_SHORT).show();
                }
              }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
              Log.d("ran","rand");
            }
          });


        }
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

          chatAdapter = new ChatAdapter(chatList, Chat.this);
          recyclerView.setAdapter(chatAdapter);

        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }
}
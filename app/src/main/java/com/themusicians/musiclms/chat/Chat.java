package com.themusicians.musiclms.chat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Build;
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

/**
 * Displays the user chat
 * <not implemented> notifications
 *
 * @contributors Harveer Khangura
 * @author Jerome Lau
 * @since Nov 24, 2020
 */

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
  User currUser;
  List<String> exp = new ArrayList<>();
  int count = 0;

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
    currUser = new User(currentUser.getUid());

    /** gets the user id of who the message is being sent to */
    toRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        toMessageID = snapshot.getValue(String.class);
        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("name");
        r.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            toMessageName = snapshot.getValue(String.class);
            //setTitle(String.format(getString(R.string.activity__dynamic_chat_with_title), toMessageName));
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

    /** Sends message to Firebase */
    sendButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        notify = true;
        String msg = textMessage.getText().toString();
        if(!msg.equals("")){
          sendMessage(currentUser.getUid(), toMessageID, msg);
//          sendNotification(toMessageID,currentUser.getUid(),msg);

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
        assert user != null;

        if(notify){
          sendNotification(toMessageID,currentUser.getUid(),msg);
          Log.d("test23","test23");
        }

        notify = false;
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

    /** Adds tech experience from firebase to list */
    DatabaseReference tech = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("techExperience");
    tech.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {


        for(DataSnapshot ds : snapshot.getChildren()){
          exp.add(ds.getValue(String.class));
          count++;
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

    /** Checks if user needs help with chatting */
    tech.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        boolean popup = true;
        for (DataSnapshot ds : snapshot.getChildren()){
          if(ds.getValue(String.class).equals(getString(R.string.make_text))){
            popup = false;
          }
        }
        if(popup){
          final String [] listItems = {getString(R.string.chat_tutorial)};
          AlertDialog.Builder mBuilder = new AlertDialog.Builder(Chat.this);
          mBuilder.setTitle(R.string.texting_tutorial);
          mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              if(which == 0){
                tech.child(Integer.toString(count)).setValue(getString(R.string.make_text));
              }

              dialog.dismiss();
            }
          });
          AlertDialog mDialog = mBuilder.create();
          mDialog.show();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

//  private void createNotificationChannel() {
//    // Create the NotificationChannel, but only on API 26+ because
//    // the NotificationChannel class is new and not in the support library
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//      CharSequence name = getString(R.string.channel_name);
//      String description = getString(R.string.channel_description);
//      int importance = NotificationManager.IMPORTANCE_DEFAULT;
//      NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//      channel.setDescription(description);
//      // Register the channel with the system; you can't change the importance
//      // or other notification behaviors after this
//      NotificationManager notificationManager = getSystemService(NotificationManager.class);
//      notificationManager.createNotificationChannel(channel);
//    }
//  }


  /**
   * Stub for sending a notification
   * @param receiver the uid of the person receiving the message
   * @param username the current user's uid
   * @param message the message to send
   */
  private void sendNotification(String receiver, String username, String message){

//    DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
//    Query query = tokens.orderByKey().equalTo(receiver);
//    Log.d("test27", String.valueOf(query));
//    query.addValueEventListener(new ValueEventListener() {
//      @Override
//      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        for(DataSnapshot snapshot :dataSnapshot.getChildren()){
//          Token token = snapshot.getValue(Token.class);
//          Data data = new Data(currentUser.getUid(),R.mipmap.ic_launcher,username+":"+message,"new message",toMessageID);
//
//          Sender sender = new Sender(data, token.getToken());
//          Log.d("test26","test26");
//          apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
//            @Override
//            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//              Log.d("random","randomness");
//              if(response.code() == 200){
//
//                if(response.body().success !=1){
//                  Toast.makeText(Chat.this,"failed",Toast.LENGTH_SHORT).show();
//                }
//              }
//            }
//
//            @Override
//            public void onFailure(Call<MyResponse> call, Throwable t) {
//              Log.d("ran","rand");
//            }
//          });
//
//
//        }
//      }
//
//      @Override
//      public void onCancelled(@NonNull DatabaseError error) {
//
//      }
//    });
  }


  /** Sends chat information to Firebase */
  private void sendMessage(String sender, String receiver, String message){
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("sender", sender);
    hashMap.put("receiver", receiver);
    hashMap.put("message", message);

    reference.child("Chats").push().setValue(hashMap);
  }


  /** Pulls chat information from Firebase and displays it */
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
  private void updateToken(String token){
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
    Token token1 = new Token(token);
    reference.child(currentUser.getUid()).setValue(token1);


  }

}
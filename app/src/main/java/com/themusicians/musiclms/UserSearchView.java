package com.themusicians.musiclms;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserSearchView extends AppCompatActivity {

  protected TextView otherName, otherEmail;
  protected FirebaseUser currentUser;
  protected String viewedUser;

  ArrayList<String> myArrayList = new ArrayList<>();
  protected ListView instrumentList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_search_profile);

    otherName = findViewById(R.id.user_name_search);
    otherEmail = findViewById(R.id.user_email_search);
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    instrumentList = findViewById(R.id.InstrumentListSearch);
    final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(UserSearchView.this, android.R.layout.simple_list_item_1, myArrayList);

    DatabaseReference getOther = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("viewUser");
    getOther.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        viewedUser = snapshot.getValue(String.class);
        DatabaseReference other = FirebaseDatabase.getInstance().getReference().child("node__user").child(viewedUser);
        other.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            Object name = snapshot.child("name").getValue();
            /*
             * Checks if names exists
             */
            if (name != null) {
              otherName.setText(name.toString());
            }

            Object email = snapshot.child("email").getValue();
            /*
             * Checks if email exists
             */
            if (email != null) {
              otherEmail.setText(email.toString());
            }

            DatabaseReference instrumentRef = FirebaseDatabase.getInstance().getReference().child("node__user").child(viewedUser).child("instruments");
            instrumentList.setAdapter(myArrayAdapter);
            instrumentRef.addChildEventListener(new ChildEventListener() {
              @Override
              public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(String.class);
                myArrayList.add(value);
                myArrayAdapter.notifyDataSetChanged();
              }

              @Override
              public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

              }

              @Override
              public void onChildRemoved(@NonNull DataSnapshot snapshot) {

              }

              @Override
              public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }
}
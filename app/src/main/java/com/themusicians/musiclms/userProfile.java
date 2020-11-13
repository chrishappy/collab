package com.themusicians.musiclms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 *
 * ....
 *
 * Contributors: Jerome Lau
 * Created by Jerome Lau on 2020-11-06
 *
 * --------------------------------
 *
 * @todo View user info
 */

public class userProfile extends AppCompatActivity {

  protected TextView myName;
  protected FirebaseUser currentUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_profile);

    myName = findViewById(R.id.myName);

    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid());
    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        String name = snapshot.child("name").getValue().toString();

        myName.setText(name);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }
}
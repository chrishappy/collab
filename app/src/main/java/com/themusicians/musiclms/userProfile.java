package com.themusicians.musiclms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * ....
 *
 * Contributors: Jerome Lau, Harveer Khangura
 * Created by Jerome Lau on 2020-11-06
 *
 * --------------------------------
 *
 * @todo View user info
 */

public class userProfile extends AppCompatActivity {

  protected TextView myName;
  protected TextView myEmail;
  protected FirebaseUser currentUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_profile);

    myName = findViewById(R.id.user_name);
    myEmail = findViewById(R.id.user_email);

    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid());
    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        String name = snapshot.child("name").getValue().toString();
        myName.setText(name);

        String email = snapshot.child("email").getValue().toString();

        myEmail.setText(email);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }
}
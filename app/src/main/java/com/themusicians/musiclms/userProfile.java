package com.themusicians.musiclms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
import com.themusicians.musiclms.entity.Node.User;

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

  protected TextView newName, newEmail, newPassword;

  protected User currUser;

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

  public void toEditData(View view){
    setContentView(R.layout.edit_data);
  }

  public void changeName(View view){
    newName = findViewById(R.id.changeName);
    currUser = new User(currentUser.getUid());
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String name = newName.getText().toString().trim();

    FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("name").setValue(name);
  }

  public void changeEmail(View view){
    newEmail = findViewById(R.id.changeEmail);

  }

  public void changePassword(View view){
    newPassword = findViewById(R.id.changePassword);

  }

  public void editDataBack(View view){
    setContentView(R.layout.activity_user_profile);
  }
}
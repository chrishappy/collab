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

  ListView lsview;
  ArrayAdapter<String> adapter;
  FirebaseUser user;

  List<String> itemList;

  String uid;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_profile);
    lsview =(ListView)findViewById(R.id.list_view);
    user = FirebaseAuth.getInstance().getCurrentUser();
    uid = user.getUid();
    itemList = new ArrayList<>();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("node__user").child(user.getUid());
    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot datasnapshot) {
        itemList.clear();

        String name = datasnapshot.child(uid).child("name").getValue(String.class);
        String email = datasnapshot.child(uid).child("email").getValue(String.class);

        itemList.add(name);
        itemList.add(email);
        //adapter = new ArrayAdapter<>(userProfile.this,android.R.layout.simple_list_item_1,lsview.setAdapter(adapter)
        //);

      }
      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
  });



  /*
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
*/
}
}
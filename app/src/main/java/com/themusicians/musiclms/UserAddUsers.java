package com.themusicians.musiclms;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.UserProfile;
import com.themusicians.musiclms.UserSearch;
import com.themusicians.musiclms.entity.Node.User;

import java.util.ArrayList;
import java.util.List;

public class UserAddUsers<button> extends AppCompatActivity {

  protected User currUser;
  protected TextView myName;
  protected FirebaseUser currentUser;
  protected ListView addedUsers;
  ArrayList<String> addedUserList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.user_add_main);
    myName = findViewById(R.id.name);
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    addedUsers = findViewById(R.id.addedUsers);
    final ArrayAdapter<String> adapter = new ArrayAdapter<>(UserAddUsers.this, android.R.layout.simple_list_item_1, addedUserList);

    // display name
    currUser = new User(currentUser.getUid());
    DatabaseReference reference =
        FirebaseDatabase.getInstance()
            .getReference()
            .child("node__user")
            .child(currentUser.getUid());
    reference.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            Object name = snapshot.child("name").getValue();
            /** Checks if names exists */
            if (name != null) {
              myName.setText(name.toString());
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });


    DatabaseReference addedUsersRef = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("addedUsers");
    addedUsers.setAdapter(adapter);
    addedUsersRef.addChildEventListener(
      new ChildEventListener() {
        @Override
        public void onChildAdded(
          @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
          String value = snapshot.getValue(String.class);
          DatabaseReference nameRef = FirebaseDatabase.getInstance().getReference().child("node__user").child(value).child("name");
          nameRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                addedUserList.add(value);
                adapter.notifyDataSetChanged();
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {}
            });
        }

        @Override
        public void onChildChanged(
          @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

        @Override
        public void onChildMoved(
          @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
      });
  }
  /** Redirects to search for Users */
  public void toSearchUsers(View view) {
    Intent toSearch = new Intent(this, UserSearch.class);
    startActivity(toSearch);
  }
}

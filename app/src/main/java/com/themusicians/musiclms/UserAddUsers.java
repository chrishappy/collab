package com.themusicians.musiclms;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.entity.Node.User;
import com.themusicians.musiclms.nodeViews.AssignmentOverviewActivity;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class UserAddUsers<button> extends AppCompatActivity {

  protected User currUser;
  protected TextView myName;
  protected FirebaseUser currentUser;
  DatabaseReference addedRef, reference;
  ArrayList<User> addedList;
  RecyclerView addedRecycler;
  SearchView searchView;
  BottomNavigationView bottomNavigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.user_add_main);
    myName = findViewById(R.id.name);
    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    addedRef = FirebaseDatabase.getInstance().getReference().child("node__user");
    reference = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("addedUsers");
    addedRecycler = findViewById(R.id.addedRecycler);
    searchView = findViewById(R.id.addedSearch);


    bottomNavigationView = findViewById(R.id.bottom_navigation);
    bottomNavigationView.setSelectedItemId(R.id.page_2);
    bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

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
  }

  /** On page start, display recycler view of searched users */
  @Override
  protected void onStart() {
    super.onStart();
    if (addedRef != null) {
      addedRef.addValueEventListener(
        new ValueEventListener() {
          @Override
          /**
           * Fetch users from Firebase and add them to the search list
           *
           * @param snapshot data snapshot for gathering data from Firebase
           */
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
              addedList = new ArrayList<>();
              UserAddedAdapter addedAdapter =
                new UserAddedAdapter(addedList, UserAddUsers.this);
              addedRecycler.setAdapter(addedAdapter);

              for (DataSnapshot ds : snapshot.getChildren()) {

                reference.addChildEventListener(new ChildEventListener() {
                  public void onChildAdded(@NonNull DataSnapshot Snapshot, @Nullable String previousChildName) {
                    String value = Snapshot.getValue(String.class);
                    if (ds.getValue(User.class).getId().equals(value)) {
                      addedList.add(ds.getValue(User.class));
                      addedAdapter.notifyDataSetChanged();
                    }
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
            }
          }

          /**
           * Display message for error on fetching data from Firebase
           *
           * @param error
           */
          @Override
          public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(UserAddUsers.this, error.getMessage(), LENGTH_SHORT).show();
          }
        });
    }
    /** Check if search bar is empty */
    if (searchView != null) {
      searchView.setOnQueryTextListener(
        new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
            return false;
          }

          /**
           * On search bar text changes, call search method
           *
           * @param newText Passes user input, newText, to search function
           * @return false
           */
          @Override
          public boolean onQueryTextChange(String newText) {
            search(newText);
            return false;
          }
        });
    }
  }

  /**
   * Search bar Takes user input and adds new searched users into the search list
   *
   * @param str String of user input passed from onQueryTextChange
   */
  private void search(String str) {
    ArrayList<User> myAddedList = new ArrayList<>();
    for (User object : addedList) {
      if (object.getName().toLowerCase().contains(str.toLowerCase())) {
        myAddedList.add(object);
      }
    }
    UserAddedAdapter userAddedAdapter = new UserAddedAdapter(myAddedList, UserAddUsers.this);
    addedRecycler.setAdapter(userAddedAdapter);
  }

  private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()){
        case R.id.page_1:
          Intent toAssignmentOverview = new Intent(UserAddUsers.this, AssignmentOverviewActivity.class);
          startActivity(toAssignmentOverview);
          return true;
        case R.id.page_2:
          Intent toUserProfile = new Intent(UserAddUsers.this, UserProfile.class);
          startActivity(toUserProfile);
          return true;
      }

      return true;
    }
  };

  /** Redirects to search for Users */
  public void toSearchUsers(View view) {
    Intent toSearch = new Intent(this, UserSearch.class);
    startActivity(toSearch);
  }
}

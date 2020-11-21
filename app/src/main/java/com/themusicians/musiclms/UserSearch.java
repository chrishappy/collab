package com.themusicians.musiclms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
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

public class UserSearch extends AppCompatActivity {

  DatabaseReference searchRef;
  ArrayList<User> searchList;
  RecyclerView searchRecycler;
  SearchView searchView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_profile_search);

    searchRef = FirebaseDatabase.getInstance().getReference().child("node__user");
    searchRecycler = findViewById(R.id.searchRecycler);
    searchView = findViewById(R.id.searchView);

  }

  /**
   * Display search pool
   */
  @Override
  protected void onStart(){
    super.onStart();
    if(searchRef != null){
      searchRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          if(snapshot.exists()){
            searchList = new ArrayList<>();
            for(DataSnapshot ds : snapshot.getChildren()){
              searchList.add(ds.getValue(User.class));
            }
            UserSearchAdapter searchAdapter = new UserSearchAdapter(searchList);
            searchRecycler.setAdapter(searchAdapter);
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
          Toast.makeText(UserSearch.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });
    }
    if(searchView != null){
      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
          return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
          search(newText);
          return false;
        }
      });
    }
  }

  /**
   * Search bar
   */
  private void search(String str){
    ArrayList<User> mySearchList = new ArrayList<>();
    for(User object : searchList){
      if(object.getName().toLowerCase().contains(str.toLowerCase())){
        mySearchList.add(object);
      }
    }
    UserSearchAdapter userSearchAdapter = new UserSearchAdapter(mySearchList);
    searchRecycler.setAdapter(userSearchAdapter);
  }

  /**
   * Redirect to User Profile
   */
  public void returnUserProfile(View view){
    Intent toUserProfile = new Intent(this, UserProfile.class);
    startActivity(toUserProfile);
  }
}
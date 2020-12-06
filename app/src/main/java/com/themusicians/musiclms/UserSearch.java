package com.themusicians.musiclms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.entity.Node.User;

import java.util.ArrayList;

/**
 * Allow users to search for teachers/students so - teachers can assign homework to students -
 * students can receive homework from students
 *
 * @author Jerome Lau
 * @since Nov 19, 2020
 */
public class UserSearch extends AppCompatActivity {

  DatabaseReference searchRef;
  ArrayList<User> searchList;
  RecyclerView searchRecycler;
  SearchView searchView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_add_search);

    searchRef = FirebaseDatabase.getInstance().getReference().child("node__user");
    searchRecycler = findViewById(R.id.searchRecycler);
    searchView = findViewById(R.id.searchView);
  }

  /** On page start, display recycler view of searched users */
  @Override
  protected void onStart() {
    super.onStart();
    if (searchRef != null) {
      searchRef.addValueEventListener(
          new ValueEventListener() {
            @Override
            /**
             * Fetch users from Firebase and add them to the search list
             *
             * @param snapshot data snapshot for gathering data from Firebase
             */
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              if (snapshot.exists()) {
                searchList = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                  searchList.add(ds.getValue(User.class));
                }
                UserSearchAdapter searchAdapter =
                    new UserSearchAdapter(searchList, UserSearch.this);
                searchRecycler.setAdapter(searchAdapter);
              }
            }

            /**
             * Display message for error on fetching data from Firebase
             *
             * @param error
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
              Toast.makeText(UserSearch.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
    ArrayList<User> mySearchList = new ArrayList<>();
    for (User object : searchList) {
      if (object.getName().toLowerCase().contains(str.toLowerCase())) {
        mySearchList.add(object);
      }
    }
    UserSearchAdapter userSearchAdapter = new UserSearchAdapter(mySearchList, UserSearch.this);
    searchRecycler.setAdapter(userSearchAdapter);
  }
}

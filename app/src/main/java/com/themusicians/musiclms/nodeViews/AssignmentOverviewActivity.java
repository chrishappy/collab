package com.themusicians.musiclms.nodeViews;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.nodeForms.AssignmentCreateFormActivity;

/**
 * Displays the assignments
 *
 * Based on https://www.geeksforgeeks.org/how-to-populate-recyclerview-with-firebase-data-using-firebaseui-in-android-studio/
 *
 * @contributor
 * @author Nathan Tsai
 * @since Nov 7, 2020
 */

public class AssignmentOverviewActivity extends AppCompatActivity {
  FirebaseAuth fAuth;

  private RecyclerView recyclerView;
  personAdapter
      adapter; // Create Object of the Adapter class
  DatabaseReference mbase; // Create object of the
  // Firebase Realtime Database

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_assignment_overview);

    // Create a instance of the database and get
    // its reference
    mbase
        = FirebaseDatabase.getInstance().getReference("person");

    recyclerView = findViewById(R.id.recycler1);

    // To display the Recycler view linearly
    recyclerView.setLayoutManager(
        new LinearLayoutManager(this));

    // It is a class provide by the FirebaseUI to make a
    // query in the database to fetch appropriate data
    FirebaseRecyclerOptions<person> options
        = new FirebaseRecyclerOptions.Builder<person>()
        .setQuery(mbase, person.class)
        .build();
    // Connecting object of required Adapter class to
    // the Adapter class itself
    adapter = new personAdapter(options);
    // Connecting Adapter class with the Recycler view*/
    recyclerView.setAdapter(adapter);

    /**
     * Set the action button to add a new assignment
     */
    FloatingActionButton fab = findViewById(R.id.createAssignment);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {


//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show();

        Intent redirectToAssignmentCreate = new Intent(AssignmentOverviewActivity.this, AssignmentCreateFormActivity.class);
        startActivity(redirectToAssignmentCreate);
      }
    });
  }

  // Function to tell the app to start getting
  // data from database on starting of the activity
  @Override protected void onStart()
  {
    super.onStart();
    adapter.startListening();
  }

  // Function to tell the app to stop getting
  // data from database on stoping of the activity
  @Override protected void onStop()
  {
    super.onStop();
    adapter.stopListening();
  }
}
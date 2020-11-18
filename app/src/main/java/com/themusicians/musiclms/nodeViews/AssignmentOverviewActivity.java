package com.themusicians.musiclms.nodeViews;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Assignment;
import com.themusicians.musiclms.myLogin;
import com.themusicians.musiclms.nodeForms.AssignmentCreateFormActivity;
import com.themusicians.musiclms.UserProfile;
import org.jetbrains.annotations.NotNull;

import static com.themusicians.musiclms.nodeForms.AssignmentCreateFormActivity.ACCEPT_ENTITY_ID;

/**
 * Displays the assignments
 *
 * <p>Based on
 * https://www.geeksforgeeks.org/how-to-populate-recyclerview-with-firebase-data-using-firebaseui-in-android-studio/
 *
 * @contributor
 * @author Nathan Tsai
 * @since Nov 7, 2020
 */
public class AssignmentOverviewActivity extends AppCompatActivity implements AssignmentOverviewAdapter.ItemClickListener {
  FirebaseAuth fAuth;

  private RecyclerView recyclerView;
  AssignmentOverviewAdapter assignmentOverviewAdapter; // Create Object of the Adapter class
  DatabaseReference mbase; // Create object of the Firebase Realtime Database

  /**
   * To Group elements, see this tutorial: https://stackoverflow.com/questions/34848401/divide-elements-on-groups-in-recyclerview
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_assignment_overview);

    // Create a instance of the database and get
    // its reference
    Assignment tempAssignment = new Assignment();
    mbase = FirebaseDatabase.getInstance().getReference(tempAssignment.getBaseTable());
    recyclerView = findViewById(R.id.assignmentOverviewRecycler);

    // To display the Recycler view using grid layout for slide functionality
    recyclerView.setLayoutManager(new GridLayoutManager( AssignmentOverviewActivity.this, 1));

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
      @Override
      public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
      }

      /**
       * To Delete on swipe: https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e
       * @param viewHolder cast to AssignmentOverviewAdapter.AssignmentsViewholder
       * @param swipeDir ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
       */
      @Override
      public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int swipeDir) {

//        AssignmentOverviewAdapter.AssignmentsViewholder swipedAssignment = (AssignmentOverviewAdapter.AssignmentsViewholder) viewHolder;

        switch(swipeDir) {
          case ItemTouchHelper.LEFT:
            Snackbar.make(recyclerView, "Assignment swiped left", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();

            int position = viewHolder.getAdapterPosition();
            assignmentOverviewAdapter.deleteAssignment(position);
            break;

          case ItemTouchHelper.RIGHT:
            Snackbar.make(recyclerView, "Assignment swiped right", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
            break;
        }

        // Remove item from backing list here
        assignmentOverviewAdapter.notifyDataSetChanged();
      }
    });
    itemTouchHelper.attachToRecyclerView(recyclerView);

    // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate data
    FirebaseRecyclerOptions<Assignment> options =
        new FirebaseRecyclerOptions.Builder<Assignment>().setQuery(mbase, Assignment.class).build();

    // Create new Adapter
    assignmentOverviewAdapter = new AssignmentOverviewAdapter(options);
    assignmentOverviewAdapter.addItemClickListener(this);
    recyclerView.setAdapter(assignmentOverviewAdapter);


    /** Set the action button to add a new assignment */
    FloatingActionButton fab = findViewById(R.id.createAssignment);
    fab.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {

            //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //            .setAction("Action", null).show();

            Intent redirectToAssignmentCreate =
                new Intent(AssignmentOverviewActivity.this, AssignmentCreateFormActivity.class);
            startActivity(redirectToAssignmentCreate);
          }
        });
  }

  // Function to tell the app to start getting
  // data from database on starting of the activity
  @Override
  protected void onStart() {
    super.onStart();
    assignmentOverviewAdapter.startListening();
  }

  // Function to tell the app to stop getting
  // data from database on stoping of the activity
  @Override
  protected void onStop() {
    super.onStop();
    assignmentOverviewAdapter.stopListening();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    fAuth = FirebaseAuth.getInstance();
    switch (item.getItemId()) {
      case R.id.logout:
        fAuth.signOut();
        Intent logout = new Intent(AssignmentOverviewActivity.this, myLogin.class);
        startActivity(logout);
        return true;
      case R.id.userprofile:
        Intent toUserProfile = new Intent(AssignmentOverviewActivity.this, UserProfile.class);
        startActivity(toUserProfile);
        return true;
      case R.id.createassignment:
        Intent toCreateAssignment = new Intent(AssignmentOverviewActivity.this, AssignmentCreateFormActivity.class);
        startActivity(toCreateAssignment);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Run this function when clicking the edit button
   * @param entityId
   */
  @Override
  public void onEditButtonClick(String type, String entityId) {
    switch(type) {
      case "editAssignment":
        Intent toCreateAssignment = new Intent(AssignmentOverviewActivity.this, AssignmentCreateFormActivity.class);
        toCreateAssignment.putExtra(ACCEPT_ENTITY_ID, entityId);
        startActivity(toCreateAssignment);
        break;
    }
  }
}

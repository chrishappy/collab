package com.themusicians.musiclms.nodeViews;

import static com.themusicians.musiclms.nodeForms.AssignmentCreateFormActivity.ACCEPT_ENTITY_ID;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.UserLogin;
import com.themusicians.musiclms.UserProfile;
import com.themusicians.musiclms.entity.Node.Assignment;
import com.themusicians.musiclms.entity.Node.User;
import com.themusicians.musiclms.nodeForms.AssignmentCreateFormActivity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import su.j2e.rvjoiner.JoinableAdapter;
import su.j2e.rvjoiner.JoinableLayout;
import su.j2e.rvjoiner.RvJoiner;

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
public class AssignmentOverviewActivity extends AppCompatActivity
    implements AssignmentOverviewAdapter.ItemClickListener {
  FirebaseAuth fAuth;

  private RecyclerView recyclerView;
  AssignmentOverviewAdapter assignmentOverviewAdapterWeek1;
  AssignmentOverviewAdapter assignmentOverviewAdapterWeek2;
  AssignmentOverviewAdapter assignmentOverviewAdapterWeek3;// Create Object of the Adapter class
  DatabaseReference mbase; // Create object of the Firebase Realtime Database

  /**
   * To Group elements, see this tutorial:
   * https://stackoverflow.com/questions/34848401/divide-elements-on-groups-in-recyclerview
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
    recyclerView.setLayoutManager(new GridLayoutManager(AssignmentOverviewActivity.this, 1));

    ItemTouchHelper itemTouchHelper1 =
        new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT /*| ItemTouchHelper.RIGHT */) {
              @Override
              public boolean onMove(
                  @NonNull RecyclerView recyclerView,
                  @NonNull RecyclerView.ViewHolder viewHolder,
                  @NonNull RecyclerView.ViewHolder target) {
                return false;
              }

              /**
               * To Delete on swipe:
               * https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e
               *
               * @param viewHolder cast to AssignmentOverviewAdapter.AssignmentsViewholder
               * @param swipeDir ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
               */
              @Override
              public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                DataSnapshot snapshot =
                    assignmentOverviewAdapterWeek1.getSnapshots().getSnapshot(position);

                if (swipeDir == ItemTouchHelper.LEFT) {
                  Snackbar.make(recyclerView, "Assignment deleted.", Snackbar.LENGTH_LONG)
                      .setAction("Action", null)
                      .show();

                    tempAssignment.getEntityDatabase().child(snapshot.getKey()).removeValue();
                    assignmentOverviewAdapterWeek1.notifyItemRemoved(position);
//                    break;

//                  case ItemTouchHelper.RIGHT:
//                    Snackbar.make(recyclerView, "Assignment swiped right", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null)
//                        .show();
//                    break;
                }

                // Remove item from backing list here
                assignmentOverviewAdapterWeek1.notifyDataSetChanged();
              }
            });
    itemTouchHelper1.attachToRecyclerView(recyclerView);

    ItemTouchHelper itemTouchHelper2 =
            new ItemTouchHelper(
                    new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                      @Override
                      public boolean onMove(
                              @NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
                        return false;
                      }

                      /**
                       * To Delete on swipe:
                       * https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e
                       *
                       * @param viewHolder cast to AssignmentOverviewAdapter.AssignmentsViewholder
                       * @param swipeDir ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                       */
                      @Override
                      public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        int position = viewHolder.getAdapterPosition();
                        DataSnapshot snapshot =
                                assignmentOverviewAdapterWeek2.getSnapshots().getSnapshot(position);

                        switch (swipeDir) {
                          case ItemTouchHelper.LEFT:
                            Snackbar.make(recyclerView, "Assignment swiped left", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null)
                                    .show();

                            tempAssignment.getEntityDatabase().child(snapshot.getKey()).removeValue();
                            assignmentOverviewAdapterWeek2.notifyItemRemoved(position);
                            break;

                          case ItemTouchHelper.RIGHT:
                            Snackbar.make(recyclerView, "Assignment swiped right", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null)
                                    .show();
                            break;
                        }

                        // Remove item from backing list here
                        assignmentOverviewAdapterWeek2.notifyDataSetChanged();
                      }
                    });
    itemTouchHelper2.attachToRecyclerView(recyclerView);

    // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate
    // data


   long time= System.currentTimeMillis()/1000;
    //long time = 1607035900;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    Query query1 = rootRef.child("node__assignment").orderByChild("dueDate").startAt(time).endAt(time+605000);
    Query query2 = rootRef.child("node__assignment").orderByChild("dueDate").startAt(time+605000).endAt(time+1210000);
    Query query3 = rootRef.child("node__assignment").orderByChild("dueDate").startAt(time+605000).endAt(time+1815000);

    FirebaseRecyclerOptions<Assignment> options1 =
            new FirebaseRecyclerOptions.Builder<Assignment>().setQuery(query1, Assignment.class).build();
    FirebaseRecyclerOptions<Assignment> options2 =
            new FirebaseRecyclerOptions.Builder<Assignment>().setQuery(query2, Assignment.class).build();
    FirebaseRecyclerOptions<Assignment> options3 =
            new FirebaseRecyclerOptions.Builder<Assignment>().setQuery(query3, Assignment.class).build();

    // Create new Adapter
    assignmentOverviewAdapterWeek1 = new AssignmentOverviewAdapter(options1);
    assignmentOverviewAdapterWeek1.addItemClickListener(this);

    assignmentOverviewAdapterWeek2 = new AssignmentOverviewAdapter(options2);
    assignmentOverviewAdapterWeek2.addItemClickListener(this);

    assignmentOverviewAdapterWeek3 = new AssignmentOverviewAdapter(options2);
    assignmentOverviewAdapterWeek3.addItemClickListener(this);

    RvJoiner rvJoiner = new RvJoiner();

    rvJoiner.add(new JoinableLayout(R.layout.due_in_week1));
    rvJoiner.add(new JoinableAdapter(assignmentOverviewAdapterWeek1));
    rvJoiner.add(new JoinableLayout(R.layout.due_in_week2));
    rvJoiner.add(new JoinableAdapter(assignmentOverviewAdapterWeek2));
    rvJoiner.add(new JoinableLayout(R.layout.due_in_week3));
    rvJoiner.add(new JoinableAdapter(assignmentOverviewAdapterWeek3));

    //set join adapter to your RecyclerView
    recyclerView.setAdapter(rvJoiner.getAdapter());

    // Set the action button to add a new assignment
    FloatingActionButton fab = findViewById(R.id.createAssignment);

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    String userid = currentUser.getUid();

    DatabaseReference userEntityDatabase;

    User tempUser = new User();
    userEntityDatabase = tempUser.getEntityDatabase();
    userEntityDatabase
            .child(userid)
            .child("role")
            .addValueEventListener(
                    new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Object role = snapshot.getValue();
                        if (role != null && role.toString().matches("teacher")){
                          fab.setVisibility(View.VISIBLE);
                        }
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {}
                    });

    fab.setOnClickListener(
        view -> {
          Intent redirectToAssignmentCreate =
              new Intent(AssignmentOverviewActivity.this, AssignmentCreateFormActivity.class);
          startActivity(redirectToAssignmentCreate);
        });
  }

  // Function to tell the app to start getting
  // data from database on starting of the activity
  @Override
  protected void onStart() {
    super.onStart();
    assignmentOverviewAdapterWeek1.startListening();
    assignmentOverviewAdapterWeek2.startListening();
    assignmentOverviewAdapterWeek3.startListening();


  }

  // Function to tell the app to stop getting
  // data from database on stoping of the activity
  @Override
  protected void onStop() {
    super.onStop();
    assignmentOverviewAdapterWeek1.stopListening();
    assignmentOverviewAdapterWeek2.startListening();
    assignmentOverviewAdapterWeek3.startListening();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    fAuth = FirebaseAuth.getInstance();
    switch (item.getItemId()) {
      case R.id.logout:
        fAuth.signOut();
        Intent logout = new Intent(AssignmentOverviewActivity.this, UserLogin.class);
        startActivity(logout);
        return true;
      case R.id.userprofile:
        Intent toUserProfile = new Intent(AssignmentOverviewActivity.this, UserProfile.class);
        startActivity(toUserProfile);
        return true;
//      case R.id.createassignment:
//        Intent toCreateAssignment =
//            new Intent(AssignmentOverviewActivity.this, AssignmentCreateFormActivity.class);
//        startActivity(toCreateAssignment);
//        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Run this function when clicking the edit button
   *
   * @param entityId the entity to edit
   */
  @Override
  public void onButtonClick(String type, String entityId) {
    switch (type) {
      case "editAssignment":
        Intent toCreateAssignment =
            new Intent(AssignmentOverviewActivity.this, AssignmentCreateFormActivity.class);
        toCreateAssignment.putExtra(ACCEPT_ENTITY_ID, entityId);
        startActivity(toCreateAssignment);
        break;

      case "viewAssignment":
        Intent toViewAssignment =
            new Intent(AssignmentOverviewActivity.this, AssignmentViewActivity.class);
        toViewAssignment.putExtra(ACCEPT_ENTITY_ID, entityId);
        startActivity(toViewAssignment);
        break;

      default:
        throw new IllegalStateException("Unexpected value: " + type);
    }
  }
}

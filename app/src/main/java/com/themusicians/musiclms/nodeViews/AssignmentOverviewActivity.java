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
import androidx.constraintlayout.solver.SolverVariableValues;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import com.themusicians.musiclms.UserAddUsers;
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

  BottomNavigationView bottomNavigationView;
  /**
   * For loading and deleting assignments
   */
  Assignment tempAssignment;
  User tempUser;

  /**
   * To Group elements, see this tutorial:
   * https://stackoverflow.com/questions/34848401/divide-elements-on-groups-in-recyclerview
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_assignment_overview);

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    assert currentUser != null;

    // Create a instance of the database and get
    // its reference
    tempAssignment = new Assignment();
    tempUser = new User(currentUser.getUid());

    recyclerView = findViewById(R.id.assignmentOverviewRecycler);

    // To display the Recycler view using grid layout for slide functionality
    recyclerView.setLayoutManager(new GridLayoutManager(AssignmentOverviewActivity.this, 1));


    DatabaseReference tempUserRelatedAssignments = tempUser.getRelatedAssignmentDbReference();

    // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate
    // data

    long time= System.currentTimeMillis();
    long week = 605000000;
    Query query1 = tempAssignment.getEntityDatabase().orderByChild("dueDate").startAt(0).endAt(time+week);
    FirebaseRecyclerOptions<Assignment> options1 =
        new FirebaseRecyclerOptions.Builder<Assignment>()
            .setIndexedQuery(tempUserRelatedAssignments, tempAssignment.getEntityDatabase(), Assignment.class)
            .setLifecycleOwner(this)
            .build();

//    long time= System.currentTimeMillis();
//    long week = 605000000;
//    Query query1 = tempAssignment.getEntityDatabase().orderByChild("dueDate").startAt(0).endAt(time+week);
//    FirebaseRecyclerOptions<Assignment> options1 =
//            new FirebaseRecyclerOptions.Builder<Assignment>()
//                .setQuery(query1, Assignment.class)
//                .build();
//
//    Query query2 = tempAssignment.getEntityDatabase().orderByChild("dueDate").startAt(time+week+1).endAt(time + week*2);
//    FirebaseRecyclerOptions<Assignment> options2 =
//            new FirebaseRecyclerOptions.Builder<Assignment>().setQuery(query2, Assignment.class).build();
//
//    Query query3 = tempAssignment.getEntityDatabase().orderByChild("dueDate").startAt(time + week*2 + 1);//.endAt(time+1815000);
//    FirebaseRecyclerOptions<Assignment> options3 =
//            new FirebaseRecyclerOptions.Builder<Assignment>().setQuery(query3, Assignment.class).build();
//
//    // Create new Adapter
    assignmentOverviewAdapterWeek1 = new AssignmentOverviewAdapter(options1);
    assignmentOverviewAdapterWeek1.addItemClickListener(this);
//
//    assignmentOverviewAdapterWeek2 = new AssignmentOverviewAdapter(options2);
//    assignmentOverviewAdapterWeek2.addItemClickListener(this);
//
//    assignmentOverviewAdapterWeek3 = new AssignmentOverviewAdapter(options2);
//    assignmentOverviewAdapterWeek3.addItemClickListener(this);
//
//    RvJoiner rvJoiner = new RvJoiner();
//
//    rvJoiner.add(new JoinableLayout(R.layout.due_in_week1));
//    rvJoiner.add(new JoinableAdapter(assignmentOverviewAdapterWeek1));
//    rvJoiner.add(new JoinableLayout(R.layout.due_in_week2));
//    rvJoiner.add(new JoinableAdapter(assignmentOverviewAdapterWeek2));
//    rvJoiner.add(new JoinableLayout(R.layout.due_in_week3));
//    rvJoiner.add(new JoinableAdapter(assignmentOverviewAdapterWeek3));
//
//    //set join adapter to your RecyclerView
//    recyclerView.setAdapter(rvJoiner.getAdapter());
    getItemTouchHelper(assignmentOverviewAdapterWeek1).attachToRecyclerView(recyclerView);
    recyclerView.setAdapter(assignmentOverviewAdapterWeek1);

    // Set the action button to add a new assignment
    FloatingActionButton fab = findViewById(R.id.createAssignment);
    tempUser.getEntityDatabase()
            .child(currentUser.getUid())
            .child("role")
            .addValueEventListener(
                    new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Object role = snapshot.getValue();
                        if (role != null && role.toString().matches("teacher")){
                          fab.setVisibility(View.VISIBLE);
                        }
                        else {
                          fab.setVisibility(View.GONE);
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

    bottomNavigationView = findViewById(R.id.bottom_navigation);
    bottomNavigationView.setSelectedItemId(R.id.page_1);
    bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
  }

  private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()){
        case R.id.page_2:
          Intent toUserProfile = new Intent(AssignmentOverviewActivity.this, UserProfile.class);
          startActivity(toUserProfile);
          overridePendingTransition(0, 0);
          return true;
        case R.id.page_3:
          Intent toChat = new Intent(AssignmentOverviewActivity.this, UserAddUsers.class);
          startActivity(toChat);
          return true;
      }

      return true;
    }
  };

  // Function to tell the app to start getting
  // data from database on starting of the activity
  @Override
  protected void onStart() {
    super.onStart();
    assignmentOverviewAdapterWeek1.startListening();
//    assignmentOverviewAdapterWeek2.startListening();
//    assignmentOverviewAdapterWeek3.startListening();
  }

  // Function to tell the app to stop getting
  // data from database on stoping of the activity
  @Override
  protected void onStop() {
    super.onStop();
    assignmentOverviewAdapterWeek1.stopListening();
//    assignmentOverviewAdapterWeek2.startListening();
//    assignmentOverviewAdapterWeek3.startListening();
  }

  private ItemTouchHelper getItemTouchHelper(AssignmentOverviewAdapter adapter) {
    return
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
                int position = viewHolder.getAdapterPosition() - 1;
                tempAssignment =
                    adapter.getSnapshots().getSnapshot(position).getValue(Assignment.class);

                if (swipeDir == ItemTouchHelper.LEFT) {
                  Snackbar.make(recyclerView, "Assignment deleted.", Snackbar.LENGTH_LONG)
                      .setAction("Action", null)
                      .show();

                  tempAssignment.delete();
                  adapter.notifyItemRemoved(position);
                  adapter.notifyDataSetChanged();
                }
              }
            });
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

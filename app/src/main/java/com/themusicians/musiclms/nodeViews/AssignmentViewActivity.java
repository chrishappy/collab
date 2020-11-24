package com.themusicians.musiclms.nodeViews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Assignment;
import com.themusicians.musiclms.entity.Node.ToDoItem;
import com.themusicians.musiclms.entity.Node.User;
import com.themusicians.musiclms.nodeForms.ToDoAssignmentFormAdapter;
import com.themusicians.musiclms.nodeForms.ToDoTaskCreateFormActivity;
import org.jetbrains.annotations.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Used to create and update assignments node entities
 *
 * @author Nathan Tsai
 * @since Nov 24, 2020
 */
public class AssignmentViewActivity extends NodeViewActivity
    implements ToDoAssignmentFormAdapter.ItemClickListener {
  /** The entity to be saved */
  protected Assignment assignment;

  /** Create recycler view for to do items */
  private RecyclerView toDoItemsRecyclerView;

  /** Fields */
  private TextView AssignmentName;
  private TextView StudentOrClass;
  private TextView dueDate;

  /** Create adapter for to do items */
  ToDoAssignmentFormAdapter toDoItemsAdapter; // Create Object of the Adapter class

  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    // If we are editing an assignment
    assignment
        .getEntityDatabase()
        .child(viewEntityId)
        .addListenerForSingleValueEvent(
            new ValueEventListener() {
              @Override
              public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                assignment = dataSnapshot.getValue(Assignment.class);

                assert assignment != null;

                if (assignment.getName() != null) {
                  AssignmentName.setText(assignment.getName());
                }

                if (assignment.getClassId() != null) {
                  StudentOrClass.setText(assignment.getClassId());
                }

                if (assignment.getDueDate() != 0) {
                  Date date = new Date(assignment.getDueDate() * 1000);
                  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
                  dueDate.setText(dateFormat.format(date));
                }

                Log.w(LOAD_ENTITY_DATABASE_TAG, "loadAssignment:onDataChange");
              }

              @Override
              public void onCancelled(@NotNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(
                    LOAD_ENTITY_DATABASE_TAG,
                    "loadAssignment:onCancelled",
                    databaseError.toException());
                // ...
              }
            });

    // For after creating the first to do item
    if (toDoItemsAdapter == null) {
      initToDoItemsList();
    }

    // If the assignment has not be saved (no id()), the toDoItemsAdapter will not be initialized
    if (toDoItemsAdapter != null) {
      toDoItemsAdapter.startListening();
    }
  }

  /** Function to tell the app to stop getting data from database on stoping of the activity */
  @Override
  protected void onStop() {
    super.onStop();

    // If the assignment has not be saved (no id()), the toDoItemsAdapter will not be initialized
    if (toDoItemsAdapter != null) {
      toDoItemsAdapter.stopListening();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Temp Entities
    User tempUser = new User();

    // Initiate the entity
    assignment = new Assignment(viewEntityId);
    setContentView(R.layout.activity_assignment_view);

    // Get fields
    AssignmentName = findViewById(R.id.assignment_name);
    StudentOrClass = findViewById(R.id.students_or_class);
    dueDate = findViewById(R.id.dueDate);

    // Load the to do tasks
    initToDoItemsList();
  }

  /** Create the to do items list */
  private void initToDoItemsList() {
    // If the assignment has not been saved yet (and has no id), don't let initiate
    if (assignment.getId() == null) {
      return;
    }

    toDoItemsRecyclerView = findViewById(R.id.todoItemsRecyclerView);
    toDoItemsRecyclerView.setLayoutManager(
        new GridLayoutManager(AssignmentViewActivity.this, 1));
    ItemTouchHelper itemTouchHelper =
        new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
              @Override
              public boolean onMove(
                  @NonNull RecyclerView recyclerView,
                  @NonNull RecyclerView.ViewHolder viewHolder,
                  @NonNull RecyclerView.ViewHolder target) {
                return false;
              }

              @Override
              public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int swipeDir) {

                //        ToDoAssignmentFormAdapter.ToDoAssignmentFormViewholder swipedAssignment =
                // (ToDoAssignmentFormAdapter.ToDoAssignmentFormViewholder) viewHolder;

                switch (swipeDir) {
                  case ItemTouchHelper.LEFT:
                    Snackbar.make(toDoItemsRecyclerView, "ToDo swiped left", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
                    break;

                  case ItemTouchHelper.RIGHT:
                    Snackbar.make(toDoItemsRecyclerView, "ToDo swiped right", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
                    break;
                }

                // Remove item from backing list here
                toDoItemsAdapter.notifyDataSetChanged();
              }
            });
    itemTouchHelper.attachToRecyclerView(toDoItemsRecyclerView);

    // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate
    // data
    ToDoItem tempToDoItem = new ToDoItem();
    FirebaseRecyclerOptions<ToDoItem> toDoOptionsQuery =
        new FirebaseRecyclerOptions.Builder<ToDoItem>()
            .setIndexedQuery(
                assignment.getToDoItemsKeyQuery(), tempToDoItem.getEntityDatabase(), ToDoItem.class)
            .build();
    //            .setQuery(tempToDoItem.getEntityDatabase(), ToDoItem.class).build();

    // Create new Adapter
    toDoItemsAdapter = new ToDoAssignmentFormAdapter(toDoOptionsQuery);
    toDoItemsAdapter.addItemClickListener(this);
    toDoItemsRecyclerView.setAdapter(toDoItemsAdapter);
  }

  /**
   * When creating a new task, we want to save the assignment to ensure the data (name, due date,
   * etc) are saved with the to do items
   *
   * @param savedInstanceState the bundle to be used to restore session
   */
  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    assignment.save();

    // Save the user's current game state
    savedInstanceState.putString(SAVED_ENTITY_ID, assignment.getId());

    // Always call the superclass so it can save the view hierarchy state
    super.onSaveInstanceState(savedInstanceState);
  }

  /** To handle saving a To Do item */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

//    switch (requestCode) {
//      case REQUEST_TODO_ENTITY:
//        if (resultCode == Activity.RESULT_OK) {
//          String toDoId = data.getStringExtra(RETURN_INTENT_TODO_ID);
//
//          if (assignment.getToDoIds().get(toDoId) == null) {
//            assignment.addToDoId(toDoId);
//            assignment.save();
//
//            // Display notification
//            Snackbar.make(
//                    findViewById(R.id.createAssignmentLayout),
//                    "To Do Item Saved",
//                    Snackbar.LENGTH_LONG)
//                .setAction("Edit", null)
//                .show();
//          } else {
//            // Display notification
//            Snackbar.make(
//                    findViewById(R.id.createAssignmentLayout),
//                    "To Do Item Updated",
//                    Snackbar.LENGTH_LONG)
//                .setAction("Edit", null)
//                .show();
//          }
//        }
        //        if (resultCode == Activity.RESULT_CANCELED) {
        // Write your code if there's no result
        //        }
//        break;
//    }
  }

  /**
   * Implement onButtonClick()
   *
   * @param entityId the entity we are editing
   */
  @Override
  public void onButtonClick(String type, String entityId) {
    if ("editToDoAssignmentForm".equals(type)) {
      Intent toViewToDoItem =
          new Intent(AssignmentViewActivity.this, ToDoViewActivity.class);
      toViewToDoItem.putExtra(ACCEPT_ENTITY_ID, entityId);
      startActivity(toViewToDoItem);
    } else {
      Toast.makeText(
          AssignmentViewActivity.this,
          "No actions for onEditButton: " + type,
          Toast.LENGTH_SHORT)
          .show();
    }
  }
}

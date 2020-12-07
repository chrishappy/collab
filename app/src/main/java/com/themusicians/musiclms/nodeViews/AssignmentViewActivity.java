package com.themusicians.musiclms.nodeViews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Assignment;
import com.themusicians.musiclms.entity.Node.Node;
import com.themusicians.musiclms.entity.Node.ToDoItem;
import com.themusicians.musiclms.entity.Node.User;
import com.themusicians.musiclms.nodeForms.AssignmentCreateFormActivity;
import com.themusicians.musiclms.nodeForms.ToDoAssignmentFormAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

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
  private TextInputEditText AssignmentName;

  private EditText StudentOrClass;
  private TextInputEditText dueDate;
  private FloatingActionButton editButton;

  /** Checkbox fields */
  private LinearLayout assignmentCompleteWrapper;

  private CheckBox assignmentComplete;

  private LinearLayout assignmentMarkedWrapper;
  private CheckBox assignmentMarked;

  /** Create adapter for to do items */
  ToDoAssignmentFormAdapter toDoItemsAdapter; // Create Object of the Adapter class

  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    // If we are editing an assignment
    if (viewEntityId != null) {
      assignment
          .getEntityDatabase()
          .child(viewEntityId)
          .addListenerForSingleValueEvent(
              new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                  assignment = dataSnapshot.getValue(Assignment.class);
                  assert assignment != null;

                  // Register options menu
                  invalidateOptionsMenu();

                  if (assignment.getName() != null) {
                    AssignmentName.setText(assignment.getName());
                  }

                  if (assignment.getClassId() != null) {
                    StudentOrClass.setText(assignment.getClassId());
                  }

                  if (assignment.getDueDate() != 0) {
                    Date date = new Date(assignment.getDueDate());
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
                    dueDate.setText(dateFormat.format(date));
                  }

                  // Set checkboxes
                  assignmentComplete.setChecked(assignment.getAssignmentComplete());
                  assignmentMarked.setChecked(assignment.getAssignmentMarked());

                  // If the author/teacher, show edit + marked checkbox
                  if (Objects.equals(assignment.getUid(), currentUser.getUid())) {
                    editButton.setVisibility(View.VISIBLE);

                    if (assignment.getAssignmentComplete()) {
                      assignmentMarkedWrapper.setVisibility(View.VISIBLE);
                      assignmentCompleteWrapper.setVisibility(View.GONE);
                    } else {
                      assignmentMarkedWrapper.setVisibility(View.GONE);
                      assignmentCompleteWrapper.setVisibility(View.GONE);
                    }
                  } else { // must be student
                    editButton.setVisibility(View.GONE);
                    assignmentMarkedWrapper.setVisibility(View.GONE);
                    assignmentCompleteWrapper.setVisibility(View.VISIBLE);

                    // Hide edit and delete functions
                    // https://stackoverflow.com/a/32072318
                    //                    final MenuItem editMenuItem =
                    // findItem(R.id.action_assignment_edit);
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
    } else {
      Toast.makeText(
              AssignmentViewActivity.this,
              "There was a problem loading this Assignment",
              Toast.LENGTH_LONG)
          .show();
      ;
    }

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

    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    // Initiate the entity
    assignment = new Assignment(viewEntityId);
    setContentView(R.layout.activity_assignment_view);

    // Get fields
    AssignmentName = findViewById(R.id.assignment_name);
    StudentOrClass = findViewById(R.id.students_or_class);
    dueDate = findViewById(R.id.dueDate);
    editButton = findViewById(R.id.auth_edit_button);

    // Checkbox wrappers for visibility
    assignmentCompleteWrapper = findViewById(R.id.assignment__student_done);
    assignmentMarkedWrapper = findViewById(R.id.assignment__teacher_marked);

    // Checkboxes: assignment complete or marked
    assignmentComplete = findViewById(R.id.assignment_completedCB);
    assignmentComplete.setOnClickListener(
        v -> {
          assignment.setAssignmentComplete(assignmentComplete.isChecked());
          assignment.save();
        });

    assignmentMarked = findViewById(R.id.assignment_marked_CB);
    assignmentMarked.setOnClickListener(
        v -> {
          assignment.setAssignmentMarked(assignmentMarked.isChecked());
          assignment.save();
        });

    // Floating edit button
    editButton.setOnClickListener(
        v -> {
          Intent toEditAssignment =
              new Intent(AssignmentViewActivity.this, AssignmentCreateFormActivity.class);
          toEditAssignment.putExtra(ACCEPT_ENTITY_ID, assignment.getId());
          startActivity(toEditAssignment);
        });

    // Load the to do tasks
    toDoItemsRecyclerView = findViewById(R.id.todoItemsRecyclerView2);
    initToDoItemsList();

    // Initialize Attachments
    initShowAttachments(R.id.showAttachments__assignments, "");
  }

  /** Return the node to add attachments to */
  @Override
  public Node getNodeForAttachments() {
    return assignment;
  }

  /** Create the to do items list */
  private void initToDoItemsList() {
    // If the assignment has not been saved yet (and has no id), don't let initiate
    if (assignment.getId() == null) {
      return;
    }

    toDoItemsRecyclerView.setLayoutManager(new GridLayoutManager(AssignmentViewActivity.this, 1));
    //    ItemTouchHelper itemTouchHelper =
    //        new ItemTouchHelper(
    //            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |
    // ItemTouchHelper.RIGHT) {
    //              @Override
    //              public boolean onMove(
    //                  @NonNull RecyclerView recyclerView,
    //                  @NonNull RecyclerView.ViewHolder viewHolder,
    //                  @NonNull RecyclerView.ViewHolder target) {
    //                return false;
    //              }
    //
    //              @Override
    //              public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int swipeDir)
    // {
    //
    //                //        ToDoAssignmentFormAdapter.ToDoRecordingFeedbackViewHolder
    // swipedAssignment =
    //                // (ToDoAssignmentFormAdapter.ToDoRecordingFeedbackViewHolder) viewHolder;
    //
    //                switch (swipeDir) {
    //                  case ItemTouchHelper.LEFT:
    //                    Snackbar.make(toDoItemsRecyclerView, "ToDo swiped left",
    // Snackbar.LENGTH_LONG)
    //                        .setAction("Action", null)
    //                        .show();
    //                    break;
    //
    //                  case ItemTouchHelper.RIGHT:
    //                    Snackbar.make(toDoItemsRecyclerView, "ToDo swiped right",
    // Snackbar.LENGTH_LONG)
    //                        .setAction("Action", null)
    //                        .show();
    //                    break;
    //                }
    //
    //                // Remove item from backing list here
    //                toDoItemsAdapter.notifyDataSetChanged();
    //              }
    //            });
    //    itemTouchHelper.attachToRecyclerView(toDoItemsRecyclerView);

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

    LinearLayoutManager llm = new LinearLayoutManager(this);
    llm.setOrientation(LinearLayoutManager.VERTICAL);
    toDoItemsRecyclerView.setLayoutManager(llm);
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
      Intent toViewToDoItem = new Intent(AssignmentViewActivity.this, ToDoViewActivity.class);
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

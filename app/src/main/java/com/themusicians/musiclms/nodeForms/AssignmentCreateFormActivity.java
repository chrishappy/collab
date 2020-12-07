package com.themusicians.musiclms.nodeForms;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Assignment;
import com.themusicians.musiclms.entity.Node.Node;
import com.themusicians.musiclms.entity.Node.ToDoItem;
import com.themusicians.musiclms.entity.Node.User;
import org.jetbrains.annotations.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import static com.themusicians.musiclms.nodeForms.ToDoTaskCreateFormActivity.ACCEPT_ATTACHED_ASSIGNMENT_ID;
import static com.themusicians.musiclms.nodeForms.ToDoTaskCreateFormActivity.REQUEST_TODO_ENTITY;
import static com.themusicians.musiclms.nodeForms.ToDoTaskCreateFormActivity.RETURN_INTENT_TODO_ID;

/**
 * Used to create and update assignments node entities
 *
 * @contributor Mingyang Wei
 * @author Nathan Tsai
 * @since Nov 5, 2020
 */
public class AssignmentCreateFormActivity extends NodeCreateFormActivity
    implements ToDoAssignmentFormAdapter.ItemClickListener {
  /** The entity to be saved */
  protected Assignment assignment;

  /** Create recycler view for to do items */
  private RecyclerView toDoItemsRecyclerView;

  private final static DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.CANADA);

  /** Fields to edit */
  private EditText AssignmentName;
  private MultiAutoCompleteTextView assigneesAutoComplete;
  private EditText dueDate;

  /** Autocomplete adapter for students */
  private ArrayAdapter<String> assigneesAutoCompleteAdapter;

  /** Used to quickly get user id when saving */
  HashMap<String, String> assigneeNameAndIdMap;

  /** Create adapter for to do items */
  private ToDoAssignmentFormAdapter toDoItemsAdapter; // Create Object of the Adapter class

  /**
   * @return the node we are editing
   */
  @Override
  public Node getNodeForAttachments() {
    return assignment;
  }

  @Override
  public void onStart() {
    super.onStart();

    // Set current user
    assignment.setUid(currentUser.getUid());

    if (inEditMode) {
      assignment
          .getEntityDatabase()
          .child(editEntityId)
          .addListenerForSingleValueEvent(
              new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                  assignment = dataSnapshot.getValue(Assignment.class);
                  assert assignment != null;

                  // Add delete menu action
                  invalidateOptionsMenu();

                  if (assignment.getName() != null) {
                    AssignmentName.setText(assignment.getName());
                  }

                  if (assignment.getClassId() != null) {
                    assigneesAutoComplete.setText(assignment.getClassId());
                  }

                  if (assignment.getDueDate() != 0) {
                    final Date date = new Date(assignment.getDueDate());
                    dueDate.setText(dateFormat.format(date));
                  }

                  // Add autocomplete suggestions
                  final User tempUser = new User(assignment.getUid());

                  //Child the root before all the push() keys are found and add a ValueEventListener()
                  tempUser.getEntityDatabase().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                      final User authorUser = dataSnapshot.child(currentUser.getUid()).getValue(User.class);

                      //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
                      for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                        String userId = suggestionSnapshot.child("id").getValue(String.class);

                        if (authorUser.getAddedUsers().contains(userId)) { // only add students associated with teacher
                          String userName = suggestionSnapshot.child("name").getValue(String.class);
                          assigneesAutoCompleteAdapter.add(userName);

                          // For when saving
                          assigneeNameAndIdMap.put(userName,userId);
                        }
                      }
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) {
                    }
                  });

                  Log.w(LOAD_ENTITY_DATABASE_TAG, "loadAssignment:onDataChange");
                }

                @Override
                public void onCancelled(@NotNull DatabaseError databaseError) {
                  // Getting Post failed, log a message
                  Log.w(
                      LOAD_ENTITY_DATABASE_TAG,
                      "loadAssignment:onCancelled",
                      databaseError.toException());
                }
              });
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

    // Initiate the entity
    if (inEditMode) {
      assignment = new Assignment(editEntityId);
    } else {
      assignment = new Assignment();
    }

    setContentView(R.layout.activity_assignment_create_form);

    // Get fields
    AssignmentName = findViewById(R.id.assignment_name);
    assigneesAutoComplete = findViewById(R.id.students_or_class);
    dueDate = findViewById(R.id.dueDate);

    // Make fields required
    addToRequired(AssignmentName);
    addToRequired(assigneesAutoComplete);
    addToRequired(dueDate);

    // Show user auto complete
    // Create a new ArrayAdapter with your context and the simple layout for the dropdown menu
    // provided by Android
    assigneesAutoCompleteAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line);
    assigneesAutoComplete.setAdapter(assigneesAutoCompleteAdapter);
    assigneesAutoComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

    // Prep for saving
    assigneeNameAndIdMap = new HashMap<>();

    // Due Date Popup
    dueDate.setInputType(InputType.TYPE_NULL);
    Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("PDT"));
    final Calendar cldr = cal.getInstance();
    dueDate.setOnClickListener(
        v -> {
          int day = cldr.get(Calendar.DAY_OF_MONTH);
          int month = cldr.get(Calendar.MONTH);
          int year = cldr.get(Calendar.YEAR);
          // date picker dialog
          DatePickerDialog picker =
              new DatePickerDialog(
                  AssignmentCreateFormActivity.this,
                  (view, year1, monthOfYear, dayOfMonth) -> {
                    cldr.set(year1, monthOfYear, dayOfMonth);
                    dueDate.setText(dateFormat.format(cldr.getTimeInMillis()));
                  },
                  year,
                  month,
                  day);
          picker.show();
        });

    // Load the to do tasks
    initToDoItemsList();

    // Show attachments
    initShowAttachments(R.id.showAttachments__assignments, "");

    // Add a task
    // From: https://stackoverflow.com/questions/10407159
    final Button addTask = findViewById(R.id.todoAddItem);
    addTask.setOnClickListener(
        view -> {
          // To generate a id if necessary
          if (assignment.getId() == null) {
            assignment.save();
          }

          Intent toAddToDoIntent =
              new Intent(AssignmentCreateFormActivity.this, ToDoTaskCreateFormActivity.class);
          toAddToDoIntent.putExtra(ACCEPT_ATTACHED_ASSIGNMENT_ID, assignment.getId());
          startActivityForResult(toAddToDoIntent, REQUEST_TODO_ENTITY);
        });

    // Cancel the Assignment
    final Button assignmentCancel = findViewById(R.id.cancelAction1);
    assignmentCancel.setOnClickListener(
        view -> {
          Snackbar.make(view, "Assignment about to be cancelled", Snackbar.LENGTH_LONG)
              .setAction("Action", null)
              .show();
          finish();
        });

    // Save the Assignment
    final Button assignmentSave = findViewById(R.id.saveAction1);
    assignmentSave.setOnClickListener(
        view -> {

          // TODO change save code to save() and put validate in there
          if (!validateForm()) {
            return;
          }

          // Due Date timestamp
          long dueDateTimestamp = cldr.getTimeInMillis();

          // Add Assignees
          String temp = assigneesAutoComplete.getText().toString();
          String[] assigneeNames = temp.split(", ?");

          // Reset the assignees and prep for removal afterwards
          assignment.resetAssigneesAndPrepareToRemoveOldAssignees();
          for (String studentId: assigneeNames){
              String id = assigneeNameAndIdMap.get(studentId);
              assignment.addAssignees(id);
          }

          assignment.setName(AssignmentName.getText().toString());
          assignment.setClassId(assigneesAutoComplete.getText().toString());
          assignment.setDueDate(dueDateTimestamp);
          assignment.setStatus(true);
          assignment.save();

          finish();

          // Display notification
          String saveMessage = (editEntityId != null) ? "Assignment updated" : "Assignment Saved";
          Snackbar.make(view, saveMessage, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        });
  }

  /** Create the to do items list */
  private void initToDoItemsList() {
    // If the assignment has not been saved yet (and has no id), don't let initiate
    if (assignment.getId() == null) {
      return;
    }

    toDoItemsRecyclerView = findViewById(R.id.todoItemsRecyclerView);
//    toDoItemsRecyclerView.setLayoutManager(
//        new GridLayoutManager(AssignmentCreateFormActivity.this, 1));
//    ItemTouchHelper itemTouchHelper =
//        new ItemTouchHelper(
//            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//              @Override
//              public boolean onMove(
//                  @NonNull RecyclerView recyclerView,
//                  @NonNull RecyclerView.ViewHolder viewHolder,
//                  @NonNull RecyclerView.ViewHolder target) {
//                return false;
//              }
//
//              @Override
//              public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
//
//                //        ToDoAssignmentFormAdapter.ToDoAssignmentFormViewholder swipedAssignment =
//                // (ToDoAssignmentFormAdapter.ToDoAssignmentFormViewholder) viewHolder;
//
//                switch (swipeDir) {
//                  case ItemTouchHelper.LEFT:
//                    Snackbar.make(toDoItemsRecyclerView, "ToDo swiped left", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null)
//                        .show();
//                    break;
//
//                  case ItemTouchHelper.RIGHT:
//                    Snackbar.make(toDoItemsRecyclerView, "ToDo swiped right", Snackbar.LENGTH_LONG)
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

    if (requestCode == REQUEST_TODO_ENTITY) {
      if (resultCode == Activity.RESULT_OK) {
        String toDoId = data.getStringExtra(RETURN_INTENT_TODO_ID);

        if (assignment.getToDoIds().get(toDoId) == null) {
          assignment
              .getToDoItemsKeyQuery()
              .child(toDoId)
              .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                  boolean isComplete = (Boolean) dataSnapshot.getValue();

                  assignment.addToDoId(toDoId, isComplete);
                  assignment.save();

                  Log.w(LOAD_ENTITY_DATABASE_TAG, "update to do item successful");
                }

                @Override
                public void onCancelled(@NotNull DatabaseError databaseError) {
                  // Getting Post failed, log a message
                  Log.w(
                      LOAD_ENTITY_DATABASE_TAG,
                      "update to do item failed",
                      databaseError.toException());

                }
              });

          // Display notification
          Snackbar.make(
              findViewById(R.id.createAssignmentLayout),
              "To Do Item Saved",
              Snackbar.LENGTH_LONG)
              .setAction("Edit", null)
              .show();
        } else {
          // Display notification
          Snackbar.make(
              findViewById(R.id.createAssignmentLayout),
              "To Do Item Updated",
              Snackbar.LENGTH_LONG)
              .setAction("Edit", null)
              .show();
        }
      }
      //        if (resultCode == Activity.RESULT_CANCELED) {
      // Write your code if there's no result
      //        }
    } else {
      Log.w("AssignmentCreateActivity", "No case to handle activity result.");
    }
  }

  /**
   * Implement onEditButtonClick()
   *
   * @param entityId the entity we are editing
   */
  @Override
  public void onButtonClick(String type, String entityId) {
    if ("editToDoAssignmentForm".equals(type)) {
      Intent toEditToDoItem =
          new Intent(AssignmentCreateFormActivity.this, ToDoTaskCreateFormActivity.class);
      toEditToDoItem.putExtra(ACCEPT_ENTITY_ID, entityId);
      toEditToDoItem.putExtra(ACCEPT_ATTACHED_ASSIGNMENT_ID, assignment.getId());
      startActivityForResult(toEditToDoItem, REQUEST_TODO_ENTITY);
    } else {
      Toast.makeText(
          AssignmentCreateFormActivity.this,
          "No actions for onEditButton: " + type,
          Toast.LENGTH_SHORT)
          .show();
    }
  }
}

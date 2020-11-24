package com.themusicians.musiclms.nodeForms;

import static com.themusicians.musiclms.nodeForms.ToDoTaskCreateFormActivity.REQUEST_TODO_ENTITY;
import static com.themusicians.musiclms.nodeForms.ToDoTaskCreateFormActivity.RETURN_INTENT_TODO_ID;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Assignment;
import com.themusicians.musiclms.entity.Node.ToDoItem;
import com.themusicians.musiclms.entity.Node.User;
import com.themusicians.musiclms.nodeViews.AssignmentOverviewActivity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

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

  /** Code for file */
  Button selectFile, upload;

  TextView notification;
  Uri pdfUri;

  FirebaseStorage storage; // used for upload files
  FirebaseDatabase database; // used to store URLs of uploaded files
  ProgressDialog progressDialog;

  /** Create recycler view for to do items */
  private RecyclerView toDoItemsRecyclerView;

  /** Create adapter for to do items */
  ToDoAssignmentFormAdapter toDoItemsAdapter; // Create Object of the Adapter class

  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    // If we are editing an assignment
    final EditText AssignmentName = findViewById(R.id.assignment_name);
    final EditText StudentOrClass = findViewById(R.id.students_or_class);
    final EditText dueDate = findViewById(R.id.dueDate);
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

    // Initiate the entity
    if (inEditMode) {
      assignment = new Assignment(editEntityId);
    } else {
      assignment = new Assignment();
    }
    setContentView(R.layout.activity_assignment_create_form);

    // Get fields
    final EditText AssignmentName = findViewById(R.id.assignment_name);
    final MultiAutoCompleteTextView StudentOrClass = findViewById(R.id.students_or_class);
    final EditText dueDate = findViewById(R.id.dueDate);

    // Show user auto complete
    // Create a new ArrayAdapter with your context and the simple layout for the dropdown menu
    // provided by Android
    final ArrayAdapter<String> autoComplete =
        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
    // Child the root before all the push() keys are found and add a ValueEventListener()
    tempUser
        .getEntityDatabase()
        .addValueEventListener(
            new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                // Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's
                // inside the method.
                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                  // Get the suggestion by childing the key of the string you want to get.
                  String suggestion = suggestionSnapshot.child("name").getValue(String.class);
                  // Add the retrieved string to the list
                  autoComplete.add(suggestion);
                }
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {}
            });
    StudentOrClass.setAdapter(autoComplete);

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
                    dueDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                    cldr.set(year1, monthOfYear, dayOfMonth);
                  },
                  year,
                  month,
                  day);
          picker.show();
        });

    // Load the to do tasks
    initToDoItemsList();

    // Add a task
    // From: https://stackoverflow.com/questions/10407159
    final Button addTask = findViewById(R.id.todoAddItem);
    addTask.setOnClickListener(
        view -> {
          Intent intent =
              new Intent(AssignmentCreateFormActivity.this, ToDoTaskCreateFormActivity.class);
          startActivityForResult(intent, REQUEST_TODO_ENTITY);
        });

    // Cancel the Assignment
    final Button assignmentCancel = findViewById(R.id.cancelAction);
    assignmentCancel.setOnClickListener(
        view -> {
          Snackbar.make(view, "Assignment about to be cancelled", Snackbar.LENGTH_LONG)
              .setAction("Action", null)
              .show();
          finish();
        });

    // Save the Assignment
    final Button assignmentSave = findViewById(R.id.saveAction);
    assignmentSave.setOnClickListener(
        view -> {
          // Display notification
          Snackbar.make(view, "Assignment about to be Saved", Snackbar.LENGTH_LONG)
              .setAction("Action", null)
              .show();
          // Due Date timestamp
          long dueDateTimestamp = TimeUnit.MILLISECONDS.toSeconds(cldr.getTimeInMillis());

          assignment.setName(AssignmentName.getText().toString());
          assignment.setClassId(StudentOrClass.getText().toString());
          assignment.setDueDate(dueDateTimestamp);
          assignment.setStatus(true);
          assignment.setUid(currentUser.getUid());
          //            assignment.setAttachmentIds( null );
          assignment.save();

          Intent toAssignmentOverview =
              new Intent(AssignmentCreateFormActivity.this, AssignmentOverviewActivity.class);
          startActivity(toAssignmentOverview);

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
    toDoItemsRecyclerView.setLayoutManager(
        new GridLayoutManager(AssignmentCreateFormActivity.this, 1));
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

    switch (requestCode) {
      case REQUEST_TODO_ENTITY:
        if (resultCode == Activity.RESULT_OK) {
          String toDoId = data.getStringExtra(RETURN_INTENT_TODO_ID);

          if (assignment.getToDoIds().get(toDoId) == null) {
            assignment.addToDoId(toDoId);
            assignment.save();

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
        break;

      case 86:
        if (resultCode == RESULT_OK && data != null) {
          pdfUri = data.getData();
          String notificationText = "A file is selected : " + data.getData().getLastPathSegment();
          notification.setText(notificationText);
        } else {
          Toast.makeText(
                  AssignmentCreateFormActivity.this, "Please select a file", Toast.LENGTH_SHORT)
              .show();
        }
        break;

      default:
        Log.w("AssignmentCreateActivity", "No case to handle activity result.");
        break;
    }
  }

  /**
   * Implement onEditButtonClick()
   *
   * @param entityId the entity we are editing
   */
  @Override
  public void onButtonClick(String type, String entityId) {
    switch (type) {
      case "editToDoAssignmentForm":
        Intent toEditToDoItem =
            new Intent(AssignmentCreateFormActivity.this, ToDoTaskCreateFormActivity.class);
        toEditToDoItem.putExtra(ACCEPT_ENTITY_ID, entityId);
        startActivityForResult(toEditToDoItem, REQUEST_TODO_ENTITY);
        break;

      default:
        Toast.makeText(
                AssignmentCreateFormActivity.this,
                "No actions for onEditButton: " + type,
                Toast.LENGTH_SHORT)
            .show();
        break;
    }
  }
}

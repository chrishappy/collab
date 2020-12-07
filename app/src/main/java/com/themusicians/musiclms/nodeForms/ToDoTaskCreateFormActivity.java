package com.themusicians.musiclms.nodeForms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.attachmentDialogs.AddAttachmentDialogFragment;
import com.themusicians.musiclms.entity.Attachment.Comment;
import com.themusicians.musiclms.entity.Node.Node;
import com.themusicians.musiclms.entity.Node.ToDoItem;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Used to create and update To Do items Referenced from AssignmentCreateForm.java
 *
 * @author Nathan Tsai
 * @since Nov 13, 2020
 */
public class ToDoTaskCreateFormActivity extends NodeCreateFormActivity {

  /** The Firebase Auth Instance */
  private FirebaseUser currentUser;

  /** The request code for retrieving to do items */
  static final int REQUEST_TODO_ENTITY = 1;

  /** The request code for retrieving to do items */
  static final String RETURN_INTENT_TODO_ID = "TODO_ID_KEY";

  /** The assignment id to attach the to do item to */
  public static final String ACCEPT_ATTACHED_ASSIGNMENT_ID = "ACCEPT_ATTACHED_ASSIGNMENT_ID";

  private String attachedAssignmentId;
  private EditText toDoItemName;
  private CheckBox requireRecording;

  /** The To Do Item object */
  protected ToDoItem toDoItem;

  /**
   * @return the node we are editing
   */
  @Override
  public Node getNodeForAttachments() {
    if (toDoItem == null) Log.w(LOAD_ENTITY_DATABASE_TAG, "todo item is null");

    return toDoItem;
  }

  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    if (inEditMode) {
      // If we are editing an to do item
      toDoItem
          .getEntityDatabase()
          .child(editEntityId)
          .addListenerForSingleValueEvent(
              new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                  toDoItem = dataSnapshot.getValue(ToDoItem.class);
                  assert toDoItem != null;

                  // Add delete menu action
                  invalidateOptionsMenu();

                  toDoItemName.setText(toDoItem.getName());
                  requireRecording.setChecked(toDoItem.getRequireRecording());
                  attachedAssignmentId = toDoItem.getAttachedAssignment();

                  Log.w(LOAD_ENTITY_DATABASE_TAG, "loadToDoItem:onDataChange");
                }

                @Override
                public void onCancelled(@NotNull DatabaseError databaseError) {
                  // Getting Post failed, log a message
                  Log.w(
                      LOAD_ENTITY_DATABASE_TAG,
                      "loadToDoItem:onCancelled",
                      databaseError.toException());
                  // ...
                }
              });
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_to_do_item_create_form);

    // Load the attached assignment id
    Intent intent = getIntent();
    attachedAssignmentId = intent.getStringExtra(ACCEPT_ATTACHED_ASSIGNMENT_ID);
    assert attachedAssignmentId != null;

    if (inEditMode) {
      toDoItem = new ToDoItem(editEntityId);
    } else {
      toDoItem = new ToDoItem();
    }

    // Get fields
    toDoItemName = findViewById(R.id.to_do_item_name);
    requireRecording = findViewById(R.id.require_recording);

    // Initialize attachments
//    initShowAttachments(R.id.showAttachments__to_do__create, "todo__create");

    // Cancel the Assignment
    final Button assignmentCancel = findViewById(R.id.cancelAction);
    assignmentCancel.setOnClickListener(
        view -> {
          Intent returnIntent = new Intent();
          setResult(Activity.RESULT_CANCELED, returnIntent);
          finish();
        });

    // Save the Assignment
    final Button assignmentSave = findViewById(R.id.saveAction);
    assignmentSave.setOnClickListener(
        view -> {
          // Due Date timestamp
          toDoItem.setAttachedAssignment(attachedAssignmentId);
          toDoItem.setName(toDoItemName.getText().toString());
          toDoItem.setStatus(true);
          toDoItem.setUid(currentUser.getUid());
          toDoItem.setRequireRecording(requireRecording.isChecked());
          toDoItem.save();

          // Return To Do Item
          Intent returnIntent = new Intent();
          returnIntent.putExtra(RETURN_INTENT_TODO_ID, toDoItem.getId());
          setResult(Activity.RESULT_OK, returnIntent);
          finish();
        });
  }
}

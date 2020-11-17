package com.themusicians.musiclms.nodeForms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.attachmentDialogs.AddAttachmentDialogFragment;
import com.themusicians.musiclms.attachmentDialogs.AddCommentDialogFragment;
import com.themusicians.musiclms.attachmentDialogs.AddFileDialogFragment;
import com.themusicians.musiclms.entity.Attachment.Comment;
import com.themusicians.musiclms.entity.Node.ToDoItem;

public class ToDoTaskCreateFormActivity extends AppCompatActivity
                                          implements AddAttachmentDialogFragment.AddAttachmentDialogListener {

  /** The Firebase Auth Instance */
  private FirebaseUser currentUser;

  /** The request code for retrieving to do items  */
  static final int REQUEST_TODO_ENTITY = 1;
  static final String RETURN_INTENT_TODO_ID = "TODO_ID_KEY";

  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
  }

  /** @param savedInstanceState */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_to_do_item_create_form);

    // Get fields
    final EditText ToDoItemName   = findViewById(R.id.to_do_item_name);
    final CheckBox RequireRecording   = findViewById(R.id.require_recording);

    // Cancel the Assignment
    final Button assignmentCancel = findViewById(R.id.assignmentCancelAction);
    assignmentCancel.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Snackbar.make(view, "To Do Item cancelled", Snackbar.LENGTH_LONG)
                .show();

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
          }
        });


    // Save the Assignment
    final Button assignmentSave = findViewById(R.id.assignmentSaveAction1);
    assignmentSave.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            // Due Date timestamp
            ToDoItem toDoItem = new ToDoItem();
            toDoItem.setStatus( true );
            toDoItem.setUid( currentUser.getUid() );
            toDoItem.setRequireRecording( RequireRecording.isChecked() ? 1 : 0 );
            toDoItem.save();

            // Return To Do Item
            Intent returnIntent = new Intent();
            returnIntent.putExtra(RETURN_INTENT_TODO_ID, toDoItem.getId());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
          }
        });

    /*
     * This section will be added to all Nodes. Please use variables to allow us
     * to quickly move these functions into a separate class
     *
     * @todo Save Comment into database
     * @todo Create "Add File Button" -> use the same functions
     */


    // Add a Comment
    final Button addCommentButton = findViewById(R.id.addCommentButton);
    addCommentButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String dialogTag = "addComment";
            DialogFragment newAddCommentDialog = new AddCommentDialogFragment();
            newAddCommentDialog.show(getSupportFragmentManager(), dialogTag);
          }
        });

      // Add a File
      final Button addFileButton = findViewById(R.id.selectFile);
      addFileButton.setOnClickListener(
              new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      String dialogTag = "addFile";
                      DialogFragment newAddFileDialog = new AddFileDialogFragment();
                      newAddFileDialog.show(getSupportFragmentManager(), dialogTag);
                  }
              });
  }


  @Override
  public void onDialogPositiveClick(DialogFragment dialog) {
    // Get field from dialog
    final EditText AssignmentName = (EditText) findViewById(R.id.assignment_name);

    Comment newComment = new Comment();
    newComment.setComment( AssignmentName.getText().toString() );
    newComment.save();

  }

  @Override
  public void onDialogNegativeClick(DialogFragment dialog) {
    Snackbar.make(findViewById(R.id.createAssignmentLayout), "Comment Negative clicked", Snackbar.LENGTH_LONG)
        .setAction("Action", null)
        .show();

  }
}

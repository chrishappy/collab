package com.themusicians.musiclms.nodeForms;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.attachmentDialogs.AddAttachmentDialogFragment;
import com.themusicians.musiclms.attachmentDialogs.AddCommentDialogFragment;
//import com.themusicians.musiclms.attachmentDialogs.AddFileDialogFragment;
import com.themusicians.musiclms.attachmentDialogs.AddFileDialogFragment;
import com.themusicians.musiclms.entity.Attachment.Comment;
import com.themusicians.musiclms.entity.Node.Assignment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AssignmentCreateFormActivity extends AppCompatActivity
                                          implements AddAttachmentDialogFragment.AddAttachmentDialogListener {

  /** The Firebase Auth Instance */
  private FirebaseUser currentUser;

  /** The request code for retrieving todo items  */
  static final int REQUEST_TODO_ENTITY = 1;

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

    setContentView(R.layout.activity_assignment_create_form);

    // Get fields
    final EditText AssignmentName   = findViewById(R.id.assignment_name);
    final EditText StudentOrClass   = findViewById(R.id.students_or_class);
    final EditText dueDate          = findViewById(R.id.dueDate);

    // Due Date Popup
    dueDate.setInputType(InputType.TYPE_NULL);
    Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("PDT"));
    final Calendar cldr = cal.getInstance();
    dueDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(AssignmentCreateFormActivity.this,
            new DatePickerDialog.OnDateSetListener() {
              @Override
              public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dueDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                cldr.set(year, monthOfYear, dayOfMonth);
              }
            }, year, month, day);
        picker.show();
      }
    });

    // Add a task
    final Button addTask = findViewById(R.id.todoAddItem);
    addTask.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent();
            startActivityForResult(intent, REQUEST_TODO_ENTITY);
          }
        });

    // Cancel the Assignment
    final Button assignmentCancel = findViewById(R.id.assignmentCancelAction);
    assignmentCancel.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Snackbar.make(view, "Assignment about to be cancelled", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
            finish();
          }
        });


    // Save the Assignment
    final Button assignmentSave = findViewById(R.id.assignmentSaveAction1);
    assignmentSave.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            // Display notification
            Snackbar.make(view, "Assignment about to be Saved", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();

            List<String> dummyList = new LinkedList<>();
            dummyList.add("This is an element");
            dummyList.add("This is another element");

            // Due Date timestamp
            long dueDateTimestamp = TimeUnit.MILLISECONDS.toSeconds( cldr.getTimeInMillis() );

            Assignment assignment = new Assignment();
            assignment.setName( AssignmentName.getText().toString() );
            assignment.setClassId( StudentOrClass.getText().toString() );
            assignment.setDueDate( dueDateTimestamp );
            assignment.setStatus( true );
            assignment.setUid( currentUser.getUid() );
//            assignment.setAttachmentIds( null );
            assignment.save();

           //Display notification
            Snackbar.make(view, "Assignment Saved", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
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
      final Button addFileButton = findViewById(R.id.addFileButton);
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

  // End section to be generalized

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_TODO_ENTITY && resultCode == RESULT_OK) {

    }
  }
}

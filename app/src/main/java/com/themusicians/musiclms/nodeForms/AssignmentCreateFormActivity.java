package com.themusicians.musiclms.nodeForms;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.themusicians.musiclms.MainActivity;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.attachmentDialogs.AddAttachmentDialogFragment;
import com.themusicians.musiclms.attachmentDialogs.AddCommentDialogFragment;
//import com.themusicians.musiclms.attachmentDialogs.AddFileDialogFragment;
import com.themusicians.musiclms.attachmentDialogs.AddFileDialogFragment;
import com.themusicians.musiclms.entity.Attachment.Comment;
import com.themusicians.musiclms.entity.Node.Assignment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.themusicians.musiclms.nodeForms.ToDoTaskCreateFormActivity.RETURN_INTENT_TODO_ID;

public class AssignmentCreateFormActivity extends AppCompatActivity
                                          implements AddAttachmentDialogFragment.AddAttachmentDialogListener {

  /** The Firebase Auth Instance */
  private FirebaseUser currentUser;

  /** The request code for retrieving to do items  */
  public static final int REQUEST_TODO_ENTITY = 1;

  /** The request code for retrieving to do items  */
  public static final String ACCEPT_ENTITY_ID = "ENTITY_ID_FOR_EDIT";

  /**
   * Used to restore entity id after instance is saved
   * See: https://stackoverflow.com/q/26359130
   */
  static final String SAVED_ENTITY_ID = "SAVED_ENTITY_ID";

  /** The entity id to edit */
  protected String editEntityId;

  /** Log tag for editing */
  public static final String LOAD_ASSIGNMENT_TAG = "Load Assignment To Edit";

  protected boolean inEditMode = false;

  protected Assignment assignment;

  Button selectFile,upload;
  TextView notification;
  Uri pdfUri;

  FirebaseStorage storage; // used for upload files
  FirebaseDatabase database; // used to store URLs of uploaded files
  ProgressDialog progressDialog;

  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    // If we are editing an assignment
    final EditText AssignmentName   = findViewById(R.id.assignment_name);
    final EditText StudentOrClass   = findViewById(R.id.students_or_class);
    if (inEditMode) {
      assignment.getEntityDatabase().child( editEntityId )
          .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              assignment = dataSnapshot.getValue(Assignment.class);
              AssignmentName.setText( assignment.getName() );
              StudentOrClass.setText( assignment.getClassId() );

              Log.w(LOAD_ASSIGNMENT_TAG, "loadAssignment:onDataChange");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
              // Getting Post failed, log a message
              Log.w(LOAD_ASSIGNMENT_TAG, "loadAssignment:onCancelled", databaseError.toException());
              // ...
            }
          });
    }
  }

  /** @param savedInstanceState */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Get id to edit
    Intent intent = getIntent();
    editEntityId = intent.getStringExtra(ACCEPT_ENTITY_ID);

    assignment = new Assignment();

    if (editEntityId != null) {
      inEditMode = true;
    }

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
    // From: https://stackoverflow.com/questions/10407159
    final Button addTask = findViewById(R.id.todoAddItem);
    addTask.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(AssignmentCreateFormActivity.this, ToDoTaskCreateFormActivity.class);
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





    storage=FirebaseStorage.getInstance();
    database=FirebaseDatabase.getInstance();

    selectFile=findViewById(R.id.selectFile);
    upload=findViewById(R.id.upload);
    notification=findViewById(R.id.notification);

    selectFile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(ContextCompat.checkSelfPermission(AssignmentCreateFormActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            {
                selectPdf();
            }
            else
                ActivityCompat.requestPermissions(AssignmentCreateFormActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
        }
    });

    upload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(pdfUri!=null)
                uploadFile(pdfUri);
            else
                Toast.makeText(AssignmentCreateFormActivity.this,"Select a file",Toast.LENGTH_SHORT).show();

        }
    });

  }

  private void uploadFile(Uri pdfUri){

      progressDialog=new ProgressDialog(this);
      progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      progressDialog.setTitle("Uploading file...");
      progressDialog.setProgress(0);
      progressDialog.show();

      final String fileName=System.currentTimeMillis()+"";
      StorageReference storageReference=storage.getReference();

      storageReference.child("Uploads").child(fileName).putFile(pdfUri)
              .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                      Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                      task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                          @Override
                          public void onSuccess(Uri uri) {
                              String url = uri.toString();

                              DatabaseReference reference=database.getReference();

                              reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      if(task.isSuccessful())
                                          Toast.makeText(AssignmentCreateFormActivity.this,"File successfully uploaded",Toast.LENGTH_SHORT).show();
                                      else
                                          Toast.makeText(AssignmentCreateFormActivity.this,"File not successfully uploaded",Toast.LENGTH_SHORT).show();
                                  }
                              });
                          }
                      });

                  }
              }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {

              Toast.makeText(AssignmentCreateFormActivity.this,"File not successfully uploaded",Toast.LENGTH_SHORT).show();

          }
      }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
          @Override
          public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

              int currentProgress=(int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
              progressDialog.setProgress(currentProgress);

          }
      });
  }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            selectPdf();
        else
            Toast.makeText(AssignmentCreateFormActivity.this,"please provide permission..",Toast.LENGTH_SHORT).show();
    }

    private void selectPdf(){

      Intent intent=new Intent();
      intent.setType("application/pdf");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(intent,86);
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

  // ---- End section to be generalized-----

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    assignment.save();

    // Save the user's current game state
    savedInstanceState.putString(SAVED_ENTITY_ID, assignment.getId());

    // Always call the superclass so it can save the view hierarchy state
    super.onSaveInstanceState(savedInstanceState);
  }

  /*
  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState) {
    // Always call the superclass so it can restore the view hierarchy
    super.onRestoreInstanceState(savedInstanceState);

    // Restore state members from saved instance
    editEntityId = savedInstanceState.getString(SAVED_ENTITY_ID);
  }
  /**/

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

      if (requestCode == REQUEST_TODO_ENTITY) {
        if(resultCode == Activity.RESULT_OK){
          String toDoId = data.getStringExtra(RETURN_INTENT_TODO_ID);

          assignment.addToDoId(toDoId);
          assignment.save();

          //Display notification
          Snackbar.make(findViewById(R.id.createAssignmentLayout), "To Do Item Saved", Snackbar.LENGTH_LONG)
              .setAction("Edit", null)
              .show();
        }
        if (resultCode == Activity.RESULT_CANCELED) {
          //Write your code if there's no result
        }
      }
      else {
          if(requestCode==86 && resultCode==RESULT_OK && data!=null){
              pdfUri=data.getData();
              notification.setText("A file is selected : "+data.getData().getLastPathSegment());
          }else{
              Toast.makeText(AssignmentCreateFormActivity.this,"Please select a file",Toast.LENGTH_SHORT).show();
          }
      }

  }
}

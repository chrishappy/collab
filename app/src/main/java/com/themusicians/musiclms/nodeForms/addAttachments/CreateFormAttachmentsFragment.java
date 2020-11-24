package com.themusicians.musiclms.nodeForms.addAttachments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Attachment.AllAttachment;
import com.themusicians.musiclms.entity.Attachment.Comment;
import com.themusicians.musiclms.nodeForms.AssignmentCreateFormActivity;

/**
 * Create the attachments to be added to all node create/update forms
 *
 * @contributor Mingyang Wei
 * @author Nathan Tsai
 * @since Nov 23, 2020
 */
public class CreateFormAttachmentsFragment extends CreateFormFragment {

//  private AddAttachmentsViewModel homeViewModel;

  /** The attachment to be edited or saved */
  protected AllAttachment attachment;


  /** Receive the entity id of the attachment to edit */
  public static CreateFormAttachmentsFragment newInstance(String editEntityId) {
    CreateFormAttachmentsFragment fragment = new CreateFormAttachmentsFragment();
    Bundle args = new Bundle();
    args.putString(ACCEPT_ENTITY_ID, editEntityId);
    fragment.setArguments(args);
    return fragment;
  }

  /** Set the user object + fill in the edit information */
  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    attachment = new AllAttachment();

//    homeViewModel = new ViewModelProvider(this).get(addAttachmentsViewModel.class);

    View root = inflater.inflate(R.layout.fragment_add_attachments, container, false);

    // The comment fields
    final EditText editComment = root.findViewById(R.id.editComment);


    // The file fields

    //        final TextView textView = root.findViewById(R.id.text_home);
    //        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
    //            @Override
    //            public void onChanged(@Nullable String s) {
    //                textView.setText(s);
    //            }
    //        });

    // Save the data
    final Button saveAction = root.findViewById(R.id.saveAction);
    saveAction.setOnClickListener(
        view -> {

          attachment.setComment( editComment.getText().toString() );
          attachment.setStatus( true );
          attachment.setUid( currentUser.getUid() );
          attachment.save();

          //Display notification
          String saveMessage = (editEntityId != null) ? "Attachment updated" : "Attachment Saved";
          Snackbar.make(view, saveMessage, Snackbar.LENGTH_LONG)
              .setAction("Action", null)
              .show();
        });

    final Button cancelAction = root.findViewById(R.id.cancelAction);
    cancelAction.setOnClickListener(
        view -> {
          //Display notification
          String saveMessage = (editEntityId != null) ? "Attachment update cancelled" : "Attachment save cancelled";
          Snackbar.make(view, saveMessage, Snackbar.LENGTH_LONG)
              .setAction("Action", null)
              .show();
        });

    return root;
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    attachment.save();

    // Save the user's current game state
    savedInstanceState.putString(SAVED_ENTITY_ID, attachment.getId());

    // Always call the superclass so it can save the view hierarchy state
    super.onSaveInstanceState(savedInstanceState);
  }/*
   * Not working function to upload a pdf
   *
   * @param pdfUri the file to upload
   */
  /*
  private void uploadFile(Uri pdfUri) {

    progressDialog = new ProgressDialog(this);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.setTitle("Uploading file...");
    progressDialog.setProgress(0);
    progressDialog.show();

    final String fileName = System.currentTimeMillis() + "";
    StorageReference storageReference = storage.getReference();

    File tempFile = new File();
    storageReference
        .child(tempFile.getBaseTable())
        .child(fileName)
        .putFile(pdfUri)
        .addOnSuccessListener(
            taskSnapshot -> {

              Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
              task.addOnSuccessListener(
                  new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                      String url = uri.toString();

                      DatabaseReference reference = database.getReference();

                      reference
                          .child(fileName)
                          .setValue(url)
                          .addOnCompleteListener(
                              task1 -> {
                                if (task1.isSuccessful())
                                  Toast.makeText(
                                          AssignmentCreateFormActivity.this,
                                          "File successfully uploaded",
                                          Toast.LENGTH_SHORT)
                                      .show();
                                else
                                  Toast.makeText(
                                          AssignmentCreateFormActivity.this,
                                          "File not successfully uploaded",
                                          Toast.LENGTH_SHORT)
                                      .show();
                              });
                    }
                  });
            })
        .addOnFailureListener(
            e -> Toast.makeText(
                    AssignmentCreateFormActivity.this,
                    "File not successfully uploaded",
                    Toast.LENGTH_SHORT)
                .show())
        .addOnProgressListener(
            taskSnapshot -> {

              int currentProgress =
                  (int)
                      (100
                          * taskSnapshot.getBytesTransferred()
                          / taskSnapshot.getTotalByteCount());
              progressDialog.setProgress(currentProgress);
            });
  }
   */

  /**
   * Ask user permission to get PDF
   */
  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      selectPdf();
    }
    else {
      Toast.makeText(getActivity(), "Please provide permission..", Toast.LENGTH_SHORT)
          .show();
    }
  }

  private void selectPdf() {

    Intent intent = new Intent();
    intent.setType("application/pdf");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(intent, 86);
  }

//  @Override
//  public void onDialogPositiveClick(DialogFragment dialog) {
//    // Get field from dialog
//    final EditText AssignmentName = findViewById(R.id.assignment_name);
//
//    Comment newComment = new Comment();
//    newComment.setComment(AssignmentName.getText().toString());
//    newComment.save();
//  }
//
//  @Override
//  public void onDialogNegativeClick(DialogFragment dialog) {
//    Snackbar.make(
//        findViewById(R.id.createAssignmentLayout),
//        "Comment Negative clicked",
//        Snackbar.LENGTH_LONG)
//        .setAction("Action", null)
//        .show();
//  }

}

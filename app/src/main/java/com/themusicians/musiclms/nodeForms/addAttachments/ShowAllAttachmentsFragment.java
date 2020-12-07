package com.themusicians.musiclms.nodeForms.addAttachments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Attachment.AllAttachment;
import com.themusicians.musiclms.entity.Node.Node;
import com.themusicians.musiclms.nodeForms.NodeActivity;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;
import static com.themusicians.musiclms.nodeForms.addAttachments.ShowAllAttachmentsAdapter.OPEN_ZOOM;
import static com.themusicians.musiclms.nodeForms.addAttachments.ShowAllAttachmentsAdapter.SHOW_PDF;
import static com.themusicians.musiclms.nodeForms.addAttachments.ShowAllAttachmentsAdapter.editAllAttachments;

/**
 * Displays the AllAttachment
 *
 * <p>Based on
 * https://www.geeksforgeeks.org/how-to-populate-recyclerview-with-firebase-data-using-firebaseui-in-android-studio/
 *
 * @contributor
 * @author Nathan Tsai
 * @since Nov 19, 2020
 */
public class ShowAllAttachmentsFragment extends CreateFormFragment
    implements ShowAllAttachmentsAdapter.ItemClickListener {

  private static final String PARENT_NODE_ID = "ACCEPT_ATTACHMENT_KEY_QUERY";

  private RecyclerView recyclerView;
  ShowAllAttachmentsAdapter showAllAttachmentsAdapter; // Create Object of the Adapter class

  /** The attachment to be edited or saved */
  protected AllAttachment attachment;

  // Where to find the attachments
  private Node nodeToBeEdited;

  /** For Uploading a File */
  FirebaseStorage storage; // used for upload files
  StorageReference storageReference;
  ProgressDialog progressDialog;
  Uri pdfUri;

  TextView fileNotification;
  Button selectFile, uploadFile, removeFile;

  /** For Zoom Meetings */
  TextView zoomMeeting, zoomPasscode;
  Button zoomTutorialLink;

  /** The Save Attachment Button */
  private Button addAttachment;

  /** Receive the entity id of the attachment to edit */
  public static ShowAllAttachmentsFragment newInstance(@NonNull Node nodeToBeEdited){
    ShowAllAttachmentsFragment fragment = new ShowAllAttachmentsFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);

    fragment.nodeToBeEdited = nodeToBeEdited;
    Log.w("debugMissingNode", "id3 is: " + nodeToBeEdited.getId());
    return fragment;
  }

  /**
   * Set the arguments
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

//    if (getArguments() != null) {
//      parentNodeId = getArguments().getString(PARENT_NODE_ID);
//    }

    // The attachment for create attachment
    attachment = new AllAttachment();

    // The node to add attachments
    if (nodeToBeEdited != null) {
      Log.w("debugMissingNode", "id is: " + nodeToBeEdited.getId());
    }
    else {
      nodeToBeEdited = ((NodeActivity) getActivity()).getNodeForAttachments();
      if (nodeToBeEdited != null) {
        Log.w("debugMissingNode", "id2 is: " + nodeToBeEdited.getId());
      }
    }
  }

  // Function to tell the app to start getting
  // data from database on starting of the activity
  @Override
  public void onStart() {
    super.onStart();
    if (nodeToBeEdited != null && nodeToBeEdited.getId() != null) {
      initShowAttachmentRecyclerView();
      addAttachment.setVisibility(View.VISIBLE);
      showAllAttachmentsAdapter.startListening();
    }
    else {
      addAttachment.setVisibility(View.GONE);
    }
  }

  // Function to tell the app to stop getting
  // data from database on stoping of the activity
  @Override
  public void onStop() {
    super.onStop();
    if (showAllAttachmentsAdapter != null) {
      showAllAttachmentsAdapter.stopListening();
    }
  }

  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_show_attachments, container, false);

    // Add attachment
    addAttachment = root.findViewById(R.id.add_attachment_button);
    addAttachment.setOnClickListener(view -> {
      showCreateAttachmentPopup(addAttachment);
    });

    // Set recycler, but initiate in onStart()
    recyclerView = root.findViewById(R.id.attachmentsOverviewRecycler);

    return root;
  }

  private void initShowAttachmentRecyclerView() {
    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
    FirebaseRecyclerOptions<AllAttachment> options =
        new FirebaseRecyclerOptions.Builder<AllAttachment>()
            .setIndexedQuery(nodeToBeEdited.getAttachmentsKeyReference(), attachment.getEntityDatabase(), AllAttachment.class)
            .build();

    // Create new Adapter
    showAllAttachmentsAdapter = new ShowAllAttachmentsAdapter(options, currentUser);
    showAllAttachmentsAdapter.addItemClickListener(this);
    recyclerView.setAdapter(showAllAttachmentsAdapter);
  }

  /**
   * Run this function when clicking the edit button in the adapter
   *
   * @param passedString this could be an entity, a pdf url, or zoom meeting info
   */
  @Override
  public void onButtonClick(String type, String passedString, View clickedItem) {
    switch (type) {
      case editAllAttachments:

        editEntityId = passedString;
        inEditMode = true;
        attachment.setId( editEntityId );

        showCreateAttachmentPopup(clickedItem);
      break;

      case SHOW_PDF:
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(passedString), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent newIntent = Intent.createChooser(intent, "Open File");
        try {
          startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
          Toast.makeText(getActivity(), "Unable to show pdf. Do you have a PDF reader installed?", Toast.LENGTH_SHORT).show();
        }
        break;

      case OPEN_ZOOM:
        String[] zoomParts = passedString.split(" ", 2);

        if (zoomParts.length == 2) {
          if (!launchZoomUrl(zoomParts[0], zoomParts[1])) {
            Toast.makeText(getActivity(), "Unable to open Zoom. Do you have the app installed?", Toast.LENGTH_SHORT).show();
          }
        }
        else {
          Toast.makeText(getActivity(), "Unable to open Zoom. Please try editing attachment and including details again.", Toast.LENGTH_SHORT).show();
        }
    }
  }

  /**
   * Show the popup to create or edit an attachment
   * @param anchorView The view to display the popup under
   */
  public void showCreateAttachmentPopup(View anchorView) {

    final View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_attachments, null);
//    final RelativeLayout back_dim_layout = requireActivity().findViewById(R.id.share_bac_dim_layout);

    // Calculate size
    int width = Resources.getSystem().getDisplayMetrics().widthPixels;

    PopupWindow popupWindow = new PopupWindow(popupView, width - 100, ViewGroup.LayoutParams.WRAP_CONTENT);

    // Set up all the fields
    initCreateAttachment(popupView, popupWindow);

    // If allow popup to be clicked
    popupWindow.setFocusable(true);
    popupWindow.setOutsideTouchable(false);
    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.rgb(225, 225, 225)));

    // Show Popup in the middle of the screen
    popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0); //.showAsDropDown(anchorView, 0, 10);
//    back_dim_layout.setVisibility(View.VISIBLE);
//
//    popupWindow.setOnDismissListener(() -> {
//      back_dim_layout.setVisibility(View.GONE);
//    });
  }

  /**
   * Initiate the create attachment form
   *
   * @param root Where to find all the widgets
   * @param popup the popup we're editing
   */
  private void initCreateAttachment(View root, PopupWindow popup) {
    final EditText editComment = root.findViewById(R.id.editComment);

    // Initiate the upload files
    initUploadFile(root);

    // Initialize the Zoom fields
    initZoomMeeting(root);

    // Save the data
    final Button saveAction = root.findViewById(R.id.saveAction);
    saveAction.setOnClickListener(
        view -> {
          // Save the attachment
          // attachment.setFileUploadUri(pdfUri); // Already saved immediately upon upload
          attachment.setComment(editComment.getText().toString());
          attachment.setZoomId(zoomMeeting.getText().toString());
          attachment.setZoomPasscode(zoomPasscode.getText().toString());
          attachment.setStatus(true);
          attachment.setUid(currentUser.getUid());
          attachment.save();

          // Add the attachment to the node
          nodeToBeEdited.addAttachmentIdDirectlyToDatabase(attachment.getId());

          // Display notification
          String saveMessage = (editEntityId != null) ? "Attachment updated" : "Attachment Saved";
          Snackbar.make(view, saveMessage, Snackbar.LENGTH_LONG).setAction("Action", null).show();

          closePopup(popup);
        });

    final Button cancelAction = root.findViewById(R.id.cancelAction);
    cancelAction.setOnClickListener(
        view -> {
          // Display notification
          String saveMessage =
              (editEntityId != null) ? "Attachment update cancelled" : "Attachment save cancelled";
          Snackbar.make(view, saveMessage, Snackbar.LENGTH_LONG).setAction("Action", null).show();

          closePopup(popup);
        });

    // Initialize field if editing
    if (inEditMode) {
      attachment
          .getEntityDatabase()
          .child(editEntityId)
          .addListenerForSingleValueEvent(
              new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                  attachment = dataSnapshot.getValue(AllAttachment.class);
                  assert attachment != null;

                  if (attachment.getComment() != null) {
                    editComment.setText(attachment.getComment());
                  }
                  
                  if (attachment.getFileUri() != null) {
                    pdfUri = Uri.parse(attachment.getFileUri());
                    setPdfFileUploaded(attachment.getFileUri());
                  }

                  if (attachment.getZoomId() != null) {
                    zoomMeeting.setText(attachment.getZoomId());
                  }

                  if (attachment.getZoomPassword() != null) {
                    zoomMeeting.setText(attachment.getZoomPassword());
                  }

                  Log.w(LOAD_ENTITY_DATABASE_TAG, "loadAttachment:onDataChange");
                }

                @Override
                public void onCancelled(@NotNull DatabaseError databaseError) {
                  // Getting Post failed, log a message
                  Log.w(
                      LOAD_ENTITY_DATABASE_TAG,
                      "loadAttachment:onCancelled",
                      databaseError.toException());
                }
              });
    }
  }

  /**
   * After saving or canceling the popup, run this function
   * @param popupToClose the popup currently opened
   */
  private void closePopup(PopupWindow popupToClose) {
    editEntityId = null;
    inEditMode = false;
    attachment.setId(null);
    popupToClose.dismiss();
  }

  /**
   * Add a zoom meeting
   */
  private void initZoomMeeting(View root) {
    zoomMeeting = root.findViewById(R.id.zoomMeeting);
    zoomPasscode = root.findViewById(R.id.zoomPasscode);
    zoomTutorialLink.findViewById(R.id.zoomTutorialLink);

    zoomTutorialLink.setOnClickListener(view -> {
      final String zoomTutorial = "https://support.zoom.us/hc/en-us/articles/201362413-How-Do-I-Schedule-Meetings-";
      Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(zoomTutorial));
      startActivity(browserIntent);
    });
  }

  /**
   * Upload a file
   */
  private void initUploadFile(View root) {

    // Upload a file
    storage = FirebaseStorage.getInstance();
    storageReference = storage.getReference(attachment.getBaseTable());

    // Buttons
    selectFile = root.findViewById(R.id.selectFile);
    uploadFile = root.findViewById(R.id.uploadFile);
    removeFile = root.findViewById(R.id.removeFile);

    fileNotification = root.findViewById(R.id.notification);

    selectFile.setOnClickListener(
        view -> {
          if (ContextCompat.checkSelfPermission(
              requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
              == PackageManager.PERMISSION_GRANTED) {
            selectPdf();
          } else
            ActivityCompat.requestPermissions(
                requireActivity(),
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                9);
           selectPdf();
        });

    uploadFile.setOnClickListener(
        view -> {
          if(pdfUri != null) {
            uploadFile(pdfUri);
          }
          else {
            Toast.makeText(getActivity(), "Select a file", Toast.LENGTH_SHORT).show();
          }
        });
    
    removeFile.setOnClickListener(
        view -> {
          removeFile();
        }
    );
  }

  /** Ask user permission to get PDF */
  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      selectPdf();
    } else {
      Toast.makeText(getActivity(), "Please provide permission..", Toast.LENGTH_SHORT).show();
    }
  }

  private void selectPdf() {
    Intent intent = new Intent();
    intent.setType("application/pdf");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(intent, 86);
  }

  /**
   * Remove file from attachment and delete from database
   */
  private void removeFile() {
    String fileUrl = pdfUri.toString();

    storage.getReferenceFromUrl(fileUrl)
        .delete()
        .addOnSuccessListener(
            taskSnapshot -> {
              Toast.makeText(
                  getActivity(),
                  getContext().getString(R.string.toast__attachment__file_delete_successful),
                  Toast.LENGTH_SHORT)
                  .show();

        })
        .addOnFailureListener(
            e -> {
              Toast.makeText(
                  getActivity(),
                  getContext().getString(R.string.toast__attachment__file_delete_failed),
                  Toast.LENGTH_SHORT)
                  .show();
            });
  }

  /*
   * Not working function to upload a pdf
   *
   * @param pdfUri the file to upload
   */
  private void uploadFile(@NonNull Uri pdfUri) {

    progressDialog = new ProgressDialog(getActivity());
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.setTitle("Uploading file...");
    progressDialog.setProgress(0);
    progressDialog.show();

    final String fileName = System.currentTimeMillis() + ".pdf";

    storageReference
        .child(currentUser.getUid())
        .child(fileName)
        .putFile(pdfUri)
        .addOnSuccessListener(
            taskSnapshot -> {

              progressDialog.dismiss();

              Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
              task.addOnSuccessListener(
                  downloadUrl -> {
                      attachment.setFileUri(pdfUri.toString());
                      attachment.setDownloadFileUri(downloadUrl.toString());
                      attachment.save();

                      Toast.makeText(
                          getActivity(),
                          getContext().getString(R.string.toast__attachment__file_successful),
                          Toast.LENGTH_SHORT)
                          .show();

                  });
            })
        .addOnFailureListener(
            e -> Toast.makeText(
                getActivity(),
                getContext().getString(R.string.toast__attachment__file_failed),
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

  /**
   * Open Zoom Meeting
   *
   * Source: https://stackoverflow.com/q/63717072
   * Author: Mithun Sarker Shuvro (https://stackoverflow.com/users/3887432)
   */
  private boolean launchZoomUrl(String meetingId, String meetingPasscode) {
    Uri zoomUri = Uri.parse(String.format("zoomus://zoom.us/join?confno=%s&pwd=%s", meetingId, meetingPasscode));
    Intent intent = new Intent(Intent.ACTION_VIEW, zoomUri);
    if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
      startActivity(intent);
      return true;
    }
    return false;
  }
  
  /**
   * Process selected pdf for upload
   */
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 86 && resultCode == RESULT_OK && data!= null ){
      pdfUri = data.getData();
      String pathUrl = pdfUri.getLastPathSegment();
      if (pathUrl != null) {
        setPdfFileSelected(pathUrl);
      }
    } else{
      Toast.makeText(getActivity(),"Please select a file",Toast.LENGTH_SHORT).show();
    }
  }
  
  private void setPdfFileSelected(@NonNull String fileName) {
    fileNotification.setText(String.format(getString(R.string.attachment__pdf_ppload_preview), fileName));

    selectFile.setVisibility(View.GONE);
    uploadFile.setVisibility(View.VISIBLE);
    removeFile.setVisibility(View.GONE);
  }
  
  private void setPdfFileUploaded(@NonNull String fileName) {
    fileNotification.setText(String.format(getString(R.string.attachment__pdf_ppload_preview), fileName));

    selectFile.setVisibility(View.GONE);
    uploadFile.setVisibility(View.GONE);
    removeFile.setVisibility(View.VISIBLE);
  }
}

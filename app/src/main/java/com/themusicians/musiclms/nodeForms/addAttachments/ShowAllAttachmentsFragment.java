package com.themusicians.musiclms.nodeForms.addAttachments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Attachment.AllAttachment;
import com.themusicians.musiclms.entity.Node.Node;
import com.themusicians.musiclms.nodeForms.NodeCreateFormActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;
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
  FirebaseAuth fAuth;

  private RecyclerView recyclerView;
  ShowAllAttachmentsAdapter showAllAttachmentsAdapter; // Create Object of the Adapter class

  /** The attachment to be edited or saved */
  protected AllAttachment attachment;

  // Where to find the attachments
//  private String parentNodeId;
  private Node nodeToBeEdited;

  /** For Uploading a File */
  Button selectFile, upload;

  TextView notification;
  Uri pdfUri;

  FirebaseStorage storage; // used for upload files
  ProgressDialog progressDialog;

  /** Receive the entity id of the attachment to edit */
  public static ShowAllAttachmentsFragment newInstance(String parentNodeId) {
    ShowAllAttachmentsFragment fragment = new ShowAllAttachmentsFragment();
    Bundle args = new Bundle();
//    args.putString(PARENT_NODE_ID, parentNodeId);
    fragment.setArguments(args);
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
    nodeToBeEdited = ((NodeCreateFormActivity) getActivity()).getNode();
  }

  // Function to tell the app to start getting
  // data from database on starting of the activity
  @Override
  public void onStart() {
    super.onStart();
    if (nodeToBeEdited.getId() != null) {
      initShowAttachmentRecyclerView();
      showAllAttachmentsAdapter.startListening();
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
    final Button addAttachment = root.findViewById(R.id.add_attachment_button);
//    final NestedScrollView scrollContainer = root.findViewById(R.id.attachmentContainer);
    addAttachment.setOnClickListener(view -> {
      showCreateAttachmentPopup(addAttachment);
    });

    // Set recycler, but initiate in onStart()
    recyclerView = root.findViewById(R.id.attachmentsOverviewRecycler);

    return root;
  }

  private void initShowAttachmentRecyclerView() {
    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
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

              /**
               * To Delete on swipe:
               * https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e
               *
               * @param viewHolder cast to AssignmentOverviewAdapter.AssignmentsViewholder
               * @param swipeDir ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
               */
              @Override
              public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int swipeDir) {

                //        AssignmentOverviewAdapter.AssignmentsViewholder swipedAssignment =
                // (AssignmentOverviewAdapter.AssignmentsViewholder) viewHolder;

                switch (swipeDir) {
                  case ItemTouchHelper.LEFT:
                    Snackbar.make(recyclerView, "Attachment swiped left", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();

                    int position = viewHolder.getAdapterPosition();
                    // assignmentOverviewAdapter.deleteAssignment(position);
                    break;

                  case ItemTouchHelper.RIGHT:
                    Snackbar.make(recyclerView, "Attachment swiped right", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
                    break;
                }

                // Remove item from backing list here
                showAllAttachmentsAdapter.notifyDataSetChanged();
              }
            });
    itemTouchHelper.attachToRecyclerView(recyclerView);

    AllAttachment tempAllAttachment = new AllAttachment();
    FirebaseRecyclerOptions<AllAttachment> options =
        new FirebaseRecyclerOptions.Builder<AllAttachment>()
            .setQuery(tempAllAttachment.getEntityDatabase(), AllAttachment.class)
//            .setIndexedQuery(nodeToBeEdited.getAttachmentsReference(), tempAllAttachment.getEntityDatabase(), AllAttachment.class)
            .build();

    // Create new Adapter
    showAllAttachmentsAdapter = new ShowAllAttachmentsAdapter(options);
    showAllAttachmentsAdapter.addItemClickListener(this);
    recyclerView.setAdapter(showAllAttachmentsAdapter);
  }

  /**
   * Run this function when clicking the edit button in the adapter
   *
   * @param entityId
   */
  @Override
  public void onEditButtonClick(String type, String entityId, View clickedItem) {
    switch (type) {
      case editAllAttachments:

        editEntityId = entityId;
        inEditMode = true;
        attachment.setId( editEntityId );

        showCreateAttachmentPopup(clickedItem);
      break;

      case SHOW_PDF:
        String url = entityId;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent newIntent = Intent.createChooser(intent, "Open File");
        try {
          startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
          Toast.makeText(getActivity(), "Unable to show pdf. Do you have a PDF reader installed?", Toast.LENGTH_SHORT).show();
        }
        break;
    }
  }

  /**
   * Show the popup to create or edit an attachment
   * @param anchorView The view to display the popup under
   */
  public void showCreateAttachmentPopup(View anchorView) {

    View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_attachments, null);

    PopupWindow popupWindow = new PopupWindow(popupView,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    // Set up all the fields
    initCreateAttachment(popupView, popupWindow);

    // If allow popup to be clicked
    popupWindow.setFocusable(true);
    popupWindow.setOutsideTouchable(false);
    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.rgb(225, 225, 225)));

    // Show Popup in the middle of the screen
    popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0); //.showAsDropDown(anchorView, 0, 10);
  }

  /**
   * Initiate the create attachment form
   *
   * @param root Where to find all the widgets
   * @param popup the popup we're editing
   */
  private void initCreateAttachment(View root, PopupWindow popup) {
    final EditText editComment = root.findViewById(R.id.editComment);

    initUploadFile(root);

    // Save the data
    final Button saveAction = root.findViewById(R.id.saveAction);
    saveAction.setOnClickListener(
        view -> {
          // Save the attachment
          // attachment.setFileUploadUri(pdfUri); // Already saved immediately upon upload
          attachment.setComment(editComment.getText().toString());
          attachment.setStatus(true);
          attachment.setUid(currentUser.getUid());
          attachment.save();

          // Add the attachment to the node
          NodeCreateFormActivity nodeCreateFormActivity = (NodeCreateFormActivity) getActivity();
          assert nodeCreateFormActivity != null;
          nodeCreateFormActivity
              .getNode()
              .addAttachmentId(attachment.getId())
              .save();

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
    popupToClose.dismiss();
  }

  /**
   * Upload a file
   */
  private void initUploadFile(View root) {

    // Upload a file
    storage = FirebaseStorage.getInstance();

    selectFile = root.findViewById(R.id.selectFile);
    upload = root.findViewById(R.id.upload);
    notification = root.findViewById(R.id.notification);

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

    upload.setOnClickListener(
        view -> {
          if(pdfUri != null) {
            Log.w("uploadFile()", "begin upload");
            uploadFile(pdfUri);
            Log.w("uploadFile()", "done upload");

          }
          else {
            Toast.makeText(getActivity(), "Select a file", Toast.LENGTH_SHORT).show();
          }
        });
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

  /*
   * Not working function to upload a pdf
   *
   * @param pdfUri the file to upload
   */
  private void uploadFile(Uri pdfUri) {

//    progressDialog = new ProgressDialog(getActivity());
//    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//    progressDialog.setTitle("Uploading file...");
//    progressDialog.setProgress(0);
//    progressDialog.show();

    final String fileName = System.currentTimeMillis() + "";
    StorageReference storageReference = storage.getReference();

    storageReference
        .child(attachment.getBaseTable())
        .child(fileName)
        .putFile(pdfUri)
        .addOnSuccessListener(
            taskSnapshot -> {

              Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
              task.addOnSuccessListener(
                  uri -> {
                      String url = uri.toString();

                      attachment.setFileUploadUri(url);

                      if (attachment.save()) {
                        Toast.makeText(
                            getActivity(),
                            "File successfully uploaded",
                            Toast.LENGTH_SHORT)
                            .show();
                      }
                      else {
                        Toast.makeText(
                            getActivity(),
                            "File not successfully uploaded",
                            Toast.LENGTH_SHORT)
                            .show();
                      }

                  });
            })
        .addOnFailureListener(
            e -> Toast.makeText(
                getActivity(),
                "File not successfully uploaded",
                Toast.LENGTH_SHORT)
                .show());
//        .addOnProgressListener(
//            taskSnapshot -> {
//
//              int currentProgress =
//                  (int)
//                      (100
//                          * taskSnapshot.getBytesTransferred()
//                          / taskSnapshot.getTotalByteCount());
//              progressDialog.setProgress(currentProgress);
//            });
  }

  /**
   * Process selected pdf for upload
   */
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 86 && resultCode == RESULT_OK && data!= null ){
      pdfUri = data.getData();
      String pathUrl = data.getData().getLastPathSegment();
      notification.setText(String.format(getString(R.string.attachment__pdf_ppload_preview), pathUrl));
    } else{
      Toast.makeText(getActivity(),"Please select a file",Toast.LENGTH_SHORT).show();
    }
  }
}

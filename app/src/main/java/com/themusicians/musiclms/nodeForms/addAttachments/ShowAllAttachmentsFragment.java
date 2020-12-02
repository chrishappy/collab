package com.themusicians.musiclms.nodeForms.addAttachments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Attachment.AllAttachment;
import com.themusicians.musiclms.entity.Node.Node;
import com.themusicians.musiclms.nodeForms.NodeCreateFormActivity;

import org.jetbrains.annotations.NotNull;

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
public class ShowAllAttachmentsFragment extends CreateFormAttachmentsFragment
    implements ShowAllAttachmentsAdapter.ItemClickListener {

  private static final String PARENT_NODE_ID = "ACCEPT_ATTACHMENT_KEY_QUERY";
  FirebaseAuth fAuth;

  private RecyclerView recyclerView;
  ShowAllAttachmentsAdapter showAllAttachmentsAdapter; // Create Object of the Adapter class
  FirebaseDatabase mbase; // Create object of the Firebase Realtime Database

  // Where to find the attachments
  private String parentNodeId;
  private Node nodeToBeEdited;

  /** Receive the entity id of the attachment to edit */
  public static ShowAllAttachmentsFragment newInstance(String parentNodeId) {
    ShowAllAttachmentsFragment fragment = new ShowAllAttachmentsFragment();
    Bundle args = new Bundle();
    args.putString(PARENT_NODE_ID, parentNodeId);
    fragment.setArguments(args);
    return fragment;
  }

  /**
   * Set the arguments
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      parentNodeId = getArguments().getString(PARENT_NODE_ID);
    }

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
    if (nodeToBeEdited != null) {
      showAllAttachmentsAdapter.stopListening();
    }
  }

  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    // Generate the database
    mbase = FirebaseDatabase.getInstance();

    View root = inflater.inflate(R.layout.fragment_show_attachments, container, false);

    // Add attachment
    final Button addAttachment = root.findViewById(R.id.add_attachment_button);
//    final NestedScrollView scrollContainer = root.findViewById(R.id.attachmentContainer);
    addAttachment.setOnClickListener(view -> {
      showPopup(addAttachment);
    });

    // Create a instance of the database and get
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
   * Run this function when clicking the edit button
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

        showPopup(clickedItem);
      break;
    }
  }

  public void showPopup(View anchorView) {

    View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_attachments, null);

    PopupWindow popupWindow = new PopupWindow(popupView,
        AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT);

    // Set up all the fields
    initCreateAttachment(popupView, popupWindow);

    // If the PopupWindow should be focusable
    popupWindow.setFocusable(true);
    popupWindow.setOutsideTouchable(false);
    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));

    // Show under anchor view with 10 vertical offset
    popupWindow.showAsDropDown(anchorView, 0, 10);
  }

  private void initCreateAttachment(View root, PopupWindow popup) {
    final EditText editComment = root.findViewById(R.id.editComment);

    // Save the data
    final Button saveAction = root.findViewById(R.id.saveAction);
    saveAction.setOnClickListener(
        view -> {

          // Save the attachment
          attachment.setComment(editComment.getText().toString());
          attachment.setStatus(true);
          attachment.setUid(currentUser.getUid());
          attachment.save();

          // Add the attachment to the node
          NodeCreateFormActivity nodeCreateFormActivity = (NodeCreateFormActivity) getActivity();
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

  private void closePopup(PopupWindow popupToClose) {
    editEntityId = null;
    inEditMode = false;
    popupToClose.dismiss();
  }
}

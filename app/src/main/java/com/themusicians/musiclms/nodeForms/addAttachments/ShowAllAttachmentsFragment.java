package com.themusicians.musiclms.nodeForms.addAttachments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Attachment.AllAttachment;
import org.jetbrains.annotations.NotNull;

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
public class ShowAllAttachmentsFragment extends Fragment
    implements ShowAllAttachmentsAdapter.ItemClickListener {

  private static final String ACCEPT_ATTACHMENT_KEY_QUERY = "ACCEPT_ATTACHMENT_KEY_QUERY";
  FirebaseAuth fAuth;

  private RecyclerView recyclerView;
  ShowAllAttachmentsAdapter showAllAttachmentsAdapter; // Create Object of the Adapter class
  FirebaseDatabase mbase; // Create object of the Firebase Realtime Database

  // Where to find the attachments
  private String attachmentKeyQuery;

  /** Receive the entity id of the attachment to edit */
  public static ShowAllAttachmentsFragment newInstance(String attachmentKeyQuery) {
    ShowAllAttachmentsFragment fragment = new ShowAllAttachmentsFragment();
    Bundle args = new Bundle();
    args.putString(ACCEPT_ATTACHMENT_KEY_QUERY, attachmentKeyQuery);
    fragment.setArguments(args);
    return fragment;
  }
  /**
   * To Group elements, see this tutorial:
   * https://stackoverflow.com/questions/34848401/divide-elements-on-groups-in-recyclerview
   *
   * @param savedInstanceState
   */
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    // Generate the database
    mbase = FirebaseDatabase.getInstance();

    View root = inflater.inflate(R.layout.fragment_show_attachments, container, false);

    // Create a instance of the database and get
    recyclerView = root.findViewById(R.id.attachmentsOverviewRecycler);

    // To display the Recycler view using grid layout for slide functionality
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
//            .setIndexedQuery(mbase.getReference(attachmentKeyQuery), tempAllAttachment.getEntityDatabase(), AllAttachment.class)
            .build();

    // Create new Adapter
    showAllAttachmentsAdapter = new ShowAllAttachmentsAdapter(options);
    showAllAttachmentsAdapter.addItemClickListener(this);
    recyclerView.setAdapter(showAllAttachmentsAdapter);

    return root;
  }

  /**
   * Set the arguments
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      attachmentKeyQuery = getArguments().getString(ACCEPT_ATTACHMENT_KEY_QUERY);
    }
  }

  // Function to tell the app to start getting
  // data from database on starting of the activity
  @Override
  public void onStart() {
    super.onStart();
    showAllAttachmentsAdapter.startListening();
  }

  // Function to tell the app to stop getting
  // data from database on stoping of the activity
  @Override
  public void onStop() {
    super.onStop();
    showAllAttachmentsAdapter.stopListening();
  }

  /**
   * Run this function when clicking the edit button
   *
   * @param entityId
   */
  @Override
  public void onEditButtonClick(String type, String entityId) {
    switch (type) {
        //      case editAllAttachments:
        //        Intent toCreateAssignment = new Intent(AssignmentOverviewActivity.this,
        // AssignmentCreateFormActivity.class);
        //        toCreateAssignment.putExtra(ACCEPT_ENTITY_ID, entityId);
        //        startActivity(toCreateAssignment);
        //        break;
    }
  }
}

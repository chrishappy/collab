package com.themusicians.musiclms.nodeForms.addAttachments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.themusicians.musiclms.entity.Node.Assignment;
import com.themusicians.musiclms.nodeForms.addAttachments.AllAttachmentsAdapter;

import org.jetbrains.annotations.NotNull;

import static com.themusicians.musiclms.nodeForms.addAttachments.AllAttachmentsAdapter.editAllAttachments;

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
public class showAllAttachmentsFragment extends Fragment implements AllAttachmentsAdapter.ItemClickListener {
  FirebaseAuth fAuth;

  private RecyclerView recyclerView;
  AllAttachmentsAdapter assignmentOverviewAdapter; // Create Object of the Adapter class
  DatabaseReference mbase; // Create object of the Firebase Realtime Database

  /**
   * To Group elements, see this tutorial: https://stackoverflow.com/questions/34848401/divide-elements-on-groups-in-recyclerview
   * @param savedInstanceState
   */
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    // Generate the database
    AllAttachment tempAllAttachment = new AllAttachment();
    mbase = tempAllAttachment.getEntityDatabase();

    View root = inflater.inflate(R.layout.fragment_show_attachments, container, false);

    // Create a instance of the database and get
    recyclerView = root.findViewById(R.id.attachmentsOverviewRecycler);

    // To display the Recycler view using grid layout for slide functionality
    recyclerView.setLayoutManager(new GridLayoutManager( getActivity(), 1));

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
      @Override
      public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
      }

      /**
       * To Delete on swipe: https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e
       * @param viewHolder cast to AssignmentOverviewAdapter.AssignmentsViewholder
       * @param swipeDir ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
       */
      @Override
      public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int swipeDir) {

//        AssignmentOverviewAdapter.AssignmentsViewholder swipedAssignment = (AssignmentOverviewAdapter.AssignmentsViewholder) viewHolder;

        switch(swipeDir) {
          case ItemTouchHelper.LEFT:
            Snackbar.make(recyclerView, "Assignment swiped left", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();

            int position = viewHolder.getAdapterPosition();
            assignmentOverviewAdapter.deleteAssignment(position);
            break;

          case ItemTouchHelper.RIGHT:
            Snackbar.make(recyclerView, "Assignment swiped right", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
            break;
        }

        // Remove item from backing list here
        assignmentOverviewAdapter.notifyDataSetChanged();
      }
    });
    itemTouchHelper.attachToRecyclerView(recyclerView);

    // It is a class provide by the FirebaseUI to make a query in the database to fetch appropriate data
    FirebaseRecyclerOptions<AllAttachment> options =
        new FirebaseRecyclerOptions.Builder<AllAttachment>().setQuery(mbase, AllAttachment.class).build();

    // Create new Adapter
    assignmentOverviewAdapter = new AllAttachmentsAdapter(options);
    assignmentOverviewAdapter.addItemClickListener(this);
    recyclerView.setAdapter(assignmentOverviewAdapter);

    return root;
  }

  // Function to tell the app to start getting
  // data from database on starting of the activity
  @Override
  public void onStart() {
    super.onStart();
    assignmentOverviewAdapter.startListening();
  }

  // Function to tell the app to stop getting
  // data from database on stoping of the activity
  @Override
  public void onStop() {
    super.onStop();
    assignmentOverviewAdapter.stopListening();
  }

  /**
   * Run this function when clicking the edit button
   * @param entityId
   */
  @Override
  public void onEditButtonClick(String type, String entityId) {
    switch(type) {
//      case editAllAttachments:
//        Intent toCreateAssignment = new Intent(AssignmentOverviewActivity.this, AssignmentCreateFormActivity.class);
//        toCreateAssignment.putExtra(ACCEPT_ENTITY_ID, entityId);
//        startActivity(toCreateAssignment);
//        break;
    }
  }
}

package com.themusicians.musiclms.nodeViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Assignment;
import com.themusicians.musiclms.entity.Node.User;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * The adapter for the Assignment Form pages
 *
 * @author Nathan Tsai
 * @since Nov 16, 2020
 */

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class AssignmentOverviewAdapter
    extends FirebaseRecyclerAdapter<Assignment, AssignmentOverviewAdapter.AssignmentsViewHolder> {

  private ItemClickListener itemClickListener;

  DatabaseReference userEntityDatabase;

  public AssignmentOverviewAdapter(@NonNull FirebaseRecyclerOptions<Assignment> options) {
    super(options);
  }

  // Function to bind the view in Card view(here
  // "person.xml") iwth data in
  // assignment class(here "person.class")
  @Override
  protected void onBindViewHolder(
      @NonNull AssignmentsViewHolder holder, int position, @NonNull Assignment assignment) {

    holder.assignmentName.setText(assignment.getName());

    if (assignment.getUid() != null) {
      User tempUser = new User();
      userEntityDatabase = tempUser.getEntityDatabase();
      userEntityDatabase
          .child(assignment.getUid())
          .child("name")
          .addValueEventListener(
              new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                  Object name = snapshot.getValue();
                  if (name != null) {
                    holder.authorName.setText(name.toString());
                  }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
              });
    }

    /*if (assignment.getClassId() != null) {
      holder.userName.setText(assignment.getClassId());
    }*/

    if (assignment.getDueDate() != 0) {
      Date date = new Date(assignment.getDueDate() * 1000);
      DateFormat dateFormat = new SimpleDateFormat("MMM d", Locale.CANADA);
      holder.dueDate.setText(dateFormat.format(date));
    }

//    holder.editAssignment.setOnClickListener(
//        view -> {
//          if (itemClickListener != null) {
//            itemClickListener.onButtonClick("editAssignment", assignment.getId());
//          }
//        });

    holder.wrapper.setOnClickListener(
        view -> {
          if (itemClickListener != null) {
            itemClickListener.onButtonClick("viewAssignment", assignment.getId());
          }
        });
  }

  // Function to tell the class about the Card view (here
  // "Assignment.xml")in
  // which the data will be shown
  @NonNull
  @Override
  public AssignmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.viewholder_assignment_overview, parent, false);
    return new AssignmentsViewHolder(view);
  }

  /**
   * Archive the assignment on swipe
   *
   * @param position the recycler item index
   */
  public void deleteAssignment(int position) {
    //    mRecentlyDeletedItem = mListItems.get(position);
    //    mRecentlyDeletedItemPosition = position;
    //    items.remove(position);
  }

  // Sub Class to create references of the views in Crad
  // view (here "person.xml")
  static class AssignmentsViewHolder extends RecyclerView.ViewHolder {
    TextView assignmentName, authorName, dueDate, userName;
//    Button editAssignment, viewAssignment;
    ConstraintLayout wrapper;

    public AssignmentsViewHolder(@NonNull View itemView) {
      super(itemView);

      wrapper = itemView.findViewById(R.id.assignment_overview_wrapper);

      assignmentName = itemView.findViewById(R.id.assignmentName);
      authorName = itemView.findViewById(R.id.authorName);
      dueDate = itemView.findViewById(R.id.dueDate);
      //userName = itemView.findViewById(R.id.userName);

      // editAssignment = itemView.findViewById(R.id.edit_button);
      // viewAssignment = itemView.findViewById(R.id.view_button);
    }
  }

  /**
   * Allow users to click the edit button
   *
   * <p>From: https://stackoverflow.com/questions/39551313/
   */
  public interface ItemClickListener {
    void onButtonClick(String type, String entityId);
  }

  public void addItemClickListener(ItemClickListener listener) {
    itemClickListener = listener;
  }
}

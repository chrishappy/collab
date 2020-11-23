package com.themusicians.musiclms.nodeViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Assignment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

  DatabaseReference reff;

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
      reff= FirebaseDatabase.getInstance().getReference().child("node__user").child(assignment.getUid());
      reff.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          String name=snapshot.child("name").getValue().toString();
          holder.authorName.setText(name);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
      });

    }

    if (assignment.getClassId() != null) {
      holder.userName.setText(assignment.getClassId());
    }

    if (assignment.getDueDate() != 0) {
      Date date = new Date(assignment.getDueDate()*1000);
//      DateFormat dateFormat = new SimpleDateFormat( getText(R.string.date_format__month_day), Locale.CANADA);
      DateFormat dateFormat = new SimpleDateFormat( "MMM d", Locale.CANADA);
      holder.dueDate.setText(dateFormat.format(date));
    }

    holder.editAssignment.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (itemClickListener != null) {
              itemClickListener.onEditButtonClick( "editAssignment", assignment.getId() );
            }
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
   * @param position
   */
  public void deleteAssignment(int position) {
//    mRecentlyDeletedItem = mListItems.get(position);
//    mRecentlyDeletedItemPosition = position;
//    items.remove(position);
    notifyItemRemoved(position);
  }

  // Sub Class to create references of the views in Crad
  // view (here "person.xml")
  class AssignmentsViewHolder extends RecyclerView.ViewHolder {
    TextView assignmentName, authorName, dueDate,userName;
    Button editAssignment, deleteAssignment;

    public AssignmentsViewHolder(@NonNull View itemView) {
      super(itemView);

      assignmentName = itemView.findViewById(R.id.assignmentName);
      authorName = itemView.findViewById(R.id.authorName);
      dueDate = itemView.findViewById(R.id.dueDate);
      userName = itemView.findViewById(R.id.userName);
      editAssignment = itemView.findViewById(R.id.edit_button);
//      editAssignment = itemView.findViewById(R.id.delete_button);
    }
  }

  /**
   * Allow users to click the edit button
   *
   * From: https://stackoverflow.com/questions/39551313/
   */
  public interface ItemClickListener {
    void onEditButtonClick(String type, String entityId);
  }

  public void addItemClickListener(ItemClickListener listener) {
    itemClickListener = listener;
  }
}

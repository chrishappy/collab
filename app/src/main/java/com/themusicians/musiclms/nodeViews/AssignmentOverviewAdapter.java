package com.themusicians.musiclms.nodeViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
      final User tempUser = new User();
      userEntityDatabase = tempUser.getEntityDatabase();
      userEntityDatabase
          .child(assignment.getUid())
          .addValueEventListener(
              new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                  User tempUser = snapshot.getValue(User.class);
                  assert tempUser != null;

                  if (tempUser.getName() != null) {
                    holder.authorName.setText(tempUser.getName());
                  }

                  if (assignment.getAssignmentMarked()) {
                    holder.isMarkedCheck.setVisibility(View.VISIBLE);
                    holder.isDoneCheck.setVisibility(View.GONE);
                  }
                  else if (assignment.getAssignmentComplete()) {
                    holder.isMarkedCheck.setVisibility(View.GONE);
                    holder.isDoneCheck.setVisibility(View.VISIBLE);
                  }
                  else {
                    holder.isMarkedCheck.setVisibility(View.GONE);
                    holder.isDoneCheck.setVisibility(View.GONE);
                  }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
              });
    }


    if (assignment.getDueDate() != 0) {
      Date date = new Date(assignment.getDueDate() );
      DateFormat dateFormat = new SimpleDateFormat("MMM d", Locale.CANADA);
      holder.dueDate.setText(dateFormat.format(date));
    }

    // Set the progress
    double amountOfProgress = 0;
    if (assignment.getCountOfTotalToDos() != 0) {
      amountOfProgress = (double) assignment.getCountOfDoneToDos() / assignment.getCountOfTotalToDos();
    }
    if (amountOfProgress == 0) {  amountOfProgress = 0.05;  } // Always show some progress

    holder.progressBar.setProgress((int) (amountOfProgress * 100), true);

    // Add on click listener
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

  // Sub Class to create references of the views in Crad
  // view (here "person.xml")
  static class AssignmentsViewHolder extends RecyclerView.ViewHolder {
    TextView assignmentName, authorName, dueDate;
    ImageView isDoneCheck, isMarkedCheck;
    ProgressBar progressBar;
    ConstraintLayout wrapper;

    public AssignmentsViewHolder(@NonNull View itemView) {
      super(itemView);
      wrapper = itemView.findViewById(R.id.assignment_overview_wrapper);
      assignmentName = itemView.findViewById(R.id.assignmentName);
      authorName = itemView.findViewById(R.id.authorName);
      progressBar = itemView.findViewById(R.id.progressBar2);
      dueDate = itemView.findViewById(R.id.dueDate);
      isDoneCheck = itemView.findViewById(R.id.assignment_overview__is_done);
      isMarkedCheck = itemView.findViewById(R.id.assignment_overview__is_marked);
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

package com.themusicians.musiclms.nodeViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Assignment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class AssignmentOverviewAdapter
    extends FirebaseRecyclerAdapter<Assignment, AssignmentOverviewAdapter.AssignmentsViewholder> {

  public AssignmentOverviewAdapter(@NonNull FirebaseRecyclerOptions<Assignment> options) {
    super(options);
  }

  // Function to bind the view in Card view(here
  // "person.xml") iwth data in
  // assignment class(here "person.class")
  @Override
  protected void onBindViewHolder(
      @NonNull AssignmentsViewholder holder, int position, @NonNull Assignment assignment) {

    holder.assignmentName.setText(assignment.getName());
    holder.authorName.setText(String.format("%s...", assignment.getUid().substring(0, 20)));

    Date date = new Date(assignment.getDueDate());
    DateFormat dateFormat = new SimpleDateFormat( String.valueOf(R.string.date_format__month_day) );
    holder.dueDate.setText(dateFormat.format(date));
  }

  // Function to tell the class about the Card view (here
  // "Assignment.xml")in
  // which the data will be shown
  @NonNull
  @Override
  public AssignmentsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.assignment_overview, parent, false);
    return new AssignmentOverviewAdapter.AssignmentsViewholder(view);
  }

  // Sub Class to create references of the views in Crad
  // view (here "person.xml")
  class AssignmentsViewholder extends RecyclerView.ViewHolder {
    TextView assignmentName, authorName, dueDate;

    public AssignmentsViewholder(@NonNull View itemView) {
      super(itemView);

      assignmentName = itemView.findViewById(R.id.assignmentName);
      authorName = itemView.findViewById(R.id.authorName);
      dueDate = itemView.findViewById(R.id.dueDate);
    }
  }
}

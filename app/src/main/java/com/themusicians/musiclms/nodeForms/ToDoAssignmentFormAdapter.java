package com.themusicians.musiclms.nodeForms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Assignment;
import com.themusicians.musiclms.entity.Node.ToDoItem;

/**
 * The adapter for the Assignment Form pages
 *
 * @author Nathan Tsai
 * @since Nov 10, 2020
 */
// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class ToDoAssignmentFormAdapter
    extends FirebaseRecyclerAdapter<ToDoItem, ToDoAssignmentFormAdapter.ToDoAssignmentFormViewholder> {

  private ItemClickListener itemClickListener;

  public ToDoAssignmentFormAdapter(@NonNull FirebaseRecyclerOptions<ToDoItem> options) {
    super(options);
  }

  // Function to bind the view in Card view(here
  // "person.xml") iwth data in
  // assignment class(here "person.class")
  @Override
  protected void onBindViewHolder(
      @NonNull ToDoAssignmentFormViewholder holder, int position, @NonNull ToDoItem toDoItem) {

    holder.toDoName.setText(toDoItem.getName());

    holder.toDoWrapper.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (itemClickListener != null) {
              itemClickListener.onEditButtonClick( "editToDoAssignmentForm", toDoItem.getId() );
            }
          }
        });
  }

  // Function to tell the class about the Card view (here
  // "Assignment.xml")in
  // which the data will be shown
  @NonNull
  @Override
  public ToDoAssignmentFormViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.viewholder_to_do_assignment_form_overview, parent, false);
    return new ToDoAssignmentFormViewholder(view);
  }

  // Sub Class to create references of the views in Crad
  // view (here "person.xml")
  class ToDoAssignmentFormViewholder extends RecyclerView.ViewHolder {
    TextView toDoName;
    ConstraintLayout toDoWrapper;

    public ToDoAssignmentFormViewholder(@NonNull View itemView) {
      super(itemView);

      toDoName = itemView.findViewById(R.id.toDoName);
      toDoWrapper = itemView.findViewById(R.id.toDoAssignmentFormWrapper);
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

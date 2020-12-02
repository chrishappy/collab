package com.themusicians.musiclms.nodeForms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.ToDoItem;

/**
 * The adapter for the Assignment Form pages
 *
 * @author Nathan Tsai
 * @since Nov 10, 2020
 */
public class ToDoAssignmentFormAdapter
    extends FirebaseRecyclerAdapter<
        ToDoItem, ToDoAssignmentFormAdapter.ToDoAssignmentFormViewholder> {

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

    // Set the to do item name
    holder.toDoName.setText(toDoItem.getName());

    // Set the on click listener
    View.OnClickListener editToDoItemListener =
        view -> {
          if (itemClickListener != null) {
            itemClickListener.onButtonClick("editToDoAssignmentForm", toDoItem.getId());
          }
        };

    if (toDoItem.getcompleteToDo() == true) {
      holder.toDoCheck.setChecked(true);
    }else{
      holder.toDoCheck.setChecked(false);
    }

    holder.toDoCheck.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (holder.toDoCheck.isChecked()){
          toDoItem.setcompleteToDo(true);
          toDoItem.save();
        }else{
          toDoItem.setcompleteToDo(false);
          toDoItem.save();
        }
      }
    });

    holder.toDoName.setOnClickListener(editToDoItemListener);
    holder.editButton.setOnClickListener(editToDoItemListener);
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
  static class ToDoAssignmentFormViewholder extends RecyclerView.ViewHolder {
    TextView toDoName;
    Button editButton;
    CheckBox toDoCheck;
    ConstraintLayout toDoWrapper;

    public ToDoAssignmentFormViewholder(@NonNull View itemView) {
      super(itemView);

      toDoName = itemView.findViewById(R.id.toDoName);
      editButton = itemView.findViewById(R.id.edit_button);
      toDoWrapper = itemView.findViewById(R.id.toDoAssignmentFormWrapper);
      toDoCheck = itemView.findViewById(R.id.to_do_item_completed);
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

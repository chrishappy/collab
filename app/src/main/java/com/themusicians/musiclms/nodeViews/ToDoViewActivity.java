package com.themusicians.musiclms.nodeViews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Attachment.Comment;
import com.themusicians.musiclms.entity.Node.Assignment;
import com.themusicians.musiclms.entity.Node.ToDoItem;
import com.themusicians.musiclms.entity.Node.User;
import com.themusicians.musiclms.nodeForms.ToDoAssignmentFormAdapter;
import com.themusicians.musiclms.nodeForms.ToDoTaskCreateFormActivity;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Used to create and update assignments node entities
 *
 * @author Nathan Tsai
 * @since Nov 24, 2020
 */
public class ToDoViewActivity extends NodeViewActivity {
  /** The Firebase Auth Instance */
  private FirebaseUser currentUser;

  /** Fields */
  private TextView toDoItemName;

  /** The To Do Item object */
  ToDoItem toDoItem;

  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    // If we are editing an to do item
    toDoItem
        .getEntityDatabase()
        .child(viewEntityId)
        .addListenerForSingleValueEvent(
            new ValueEventListener() {
              @Override
              public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                toDoItem = dataSnapshot.getValue(ToDoItem.class);
                toDoItemName.setText(toDoItem.getName());

                Log.w(LOAD_ENTITY_DATABASE_TAG, "loadToDoItem:onDataChange");
              }

              @Override
              public void onCancelled(@NotNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(
                    LOAD_ENTITY_DATABASE_TAG,
                    "loadToDoItem:onCancelled",
                    databaseError.toException());
                // ...
              }
            });
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_to_do_item_create_form);
    toDoItem = new ToDoItem();

    // Get fields
    toDoItemName = findViewById(R.id.to_do_item_name);

  }
}

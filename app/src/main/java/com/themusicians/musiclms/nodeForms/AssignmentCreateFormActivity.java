package com.themusicians.musiclms.nodeForms;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AssignmentCreateFormActivity extends AppCompatActivity {

  /**
   * The Firebase Instance
   */
  private DatabaseReference mDatabase;

  /**
   *
   * @param savedInstanceState
   */

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Firebase instance
    mDatabase = FirebaseDatabase.getInstance().getReference();

    setContentView(R.layout.activity_assignment_create_form);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
    toolBarLayout.setTitle(getTitle());

    // Get fields
    final EditText AssignmentName = (EditText) findViewById(R.id.assignment_name);
    final EditText StudentOrClass = (EditText) findViewById(R.id.students_or_class);
    final EditText AssignmentDate = (EditText) findViewById(R.id.edit_due_date);
//    final EditText jobDeet = (EditText) findViewById(R.id.job_dis);
//    final EditText jobMch1 = (EditText) findViewById(R.id.first_machine);
//    final EditText jobMch2 = (EditText) findViewById(R.id.second_machine);

    Button assignmentSave = findViewById(R.id.assignmentSaveAction);
    assignmentSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        List<Integer> dummyList =  Arrays.asList(-12, -11);

        Map<String, Object> fields = new HashMap<>();
        fields.put("name", AssignmentName.toString());
        fields.put("assignees", dummyList);
        fields.put("classId", -1);
        fields.put("attachmentIds", dummyList);


        Assignment assignment = new Assignment();
        assignment.create(fields);

        // Get key if it doesn't already exist
        String assignmentId = mDatabase.child("node__assignments").push().getKey();

        mDatabase.child("node__assignments").child(assignmentId).setValue(assignment);

        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });
  }
}
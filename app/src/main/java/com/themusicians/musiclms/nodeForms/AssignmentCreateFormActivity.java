package com.themusicians.musiclms.nodeForms;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Assignment;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
//    final EditText AssignmentDate = (EditText) findViewById(R.id.edit_due_date);
//    final EditText jobDeet = (EditText) findViewById(R.id.job_dis);
//    final EditText jobMch1 = (EditText) findViewById(R.id.first_machine);
//    final EditText jobMch2 = (EditText) findViewById(R.id.second_machine);

    /**
     *
     */
    EditText eText;
    Button btnGet;
    TextView tvw;
    tvw=(TextView)findViewById(R.id.textView1);
    eText=(EditText) findViewById(R.id.editText1);
    eText.setInputType(InputType.TYPE_NULL);
    eText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(AssignmentCreateFormActivity.this,
            new DatePickerDialog.OnDateSetListener() {
              @Override
              public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                eText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
              }
            }, year, month, day);
        picker.show();
      }
    });
    btnGet=(Button)findViewById(R.id.button1);
    btnGet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        tvw.setText("Selected Date: "+ eText.getText());
      }
    });

    /**
     * Save the Assignment
     */
    Button assignmentSave = findViewById(R.id.assignmentSaveAction);
    assignmentSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        List<Integer> dummyList =  Arrays.asList(-12, -11);

        // Map the fields
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("name", AssignmentName.getText());
        fieldMap.put("assignees", dummyList);
        fieldMap.put("classId", -1);
        fieldMap.put("attachmentIds", dummyList);
        fieldMap.put("created", System.currentTimeMillis() / 1000);
        fieldMap.put("updated", System.currentTimeMillis() / 1000);

        // Create the assignment using the field map
        Assignment assignment = new Assignment(fieldMap);
        assignment.save();

        //Display notification
        Snackbar.make(view, "Assignment Saved", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });
  }
}
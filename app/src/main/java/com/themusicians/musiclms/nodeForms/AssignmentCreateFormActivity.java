package com.themusicians.musiclms.nodeForms;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Assignment;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AssignmentCreateFormActivity extends AppCompatActivity {

  /** The Firebase Instance */
  private DatabaseReference mDatabase;

  /** @param savedInstanceState */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Firebase instance
    mDatabase = FirebaseDatabase.getInstance().getReference();

    setContentView(R.layout.activity_assignment_create_form);
    //    Toolbar toolbar = findViewById(R.id.toolbar);
    //    setSupportActionBar(toolbar);
    //    CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
    //    toolBarLayout.setTitle(getTitle());

    // Get fields
    final EditText AssignmentName = (EditText) findViewById(R.id.assignment_name);
    final EditText StudentOrClass = (EditText) findViewById(R.id.students_or_class);

    /** Due Date Popup */
    EditText dueDate = findViewById(R.id.dueDate);
    //    dueDate.setInputType(InputType.TYPE_NULL);
    //
    //    Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("PDT"));
    //    final Calendar cldr = cal.getInstance();
    //    dueDate.setOnClickListener(new View.OnClickListener() {
    //      @Override
    //      public void onClick(View v) {
    //        int day = cldr.get(Calendar.DAY_OF_MONTH);
    //        int month = cldr.get(Calendar.MONTH);
    //        int year = cldr.get(Calendar.YEAR);
    //        // date picker dialog
    //        DatePickerDialog picker = new DatePickerDialog(AssignmentCreateFormActivity.this,
    //            new DatePickerDialog.OnDateSetListener() {
    //              @Override
    //              public void onDateSet(DatePicker view, int year, int monthOfYear, int
    // dayOfMonth) {
    //                dueDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
    //                cldr.set(year, monthOfYear, dayOfMonth);
    //              }
    //            }, year, month, day);
    //        picker.show();
    //      }
    //    });

    /** Save the Assignment */
    final Button assignmentCancel = findViewById(R.id.assignmentCancelAction);
    assignmentCancel.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Snackbar.make(view, "Assignment about to be cancelled", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
          }
        });

    final Button assignmentSave = findViewById(R.id.assignmentSaveAction);
    assignmentSave.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            // Display notification
            Snackbar.make(view, "Assignment about to be Saved", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();

//            List<String> dummyList = Arrays.asList("afewfae", "fesfaee");
        List<String> dummyList =  new LinkedList<>();
        dummyList.add("This is an element");
        dummyList.add("This is another element");

            // Due Date timestamp
            long dueDateTimestamp;
            dueDateTimestamp = 1234; // TimeUnit.MILLISECONDS.toSeconds( cldr.getTimeInMillis() );

            // Map the fields
            Map<String, Object> fieldMap = new HashMap<>();
            fieldMap.put("name", AssignmentName.getText());
//            fieldMap.put("assignees", dummyList);
            fieldMap.put("classId", -1);
            fieldMap.put("dueDate", dueDateTimestamp);
//            fieldMap.put("attachmentIds", dummyList);

            String assignId = mDatabase.push().getKey();
            mDatabase.child(assignId).setValue(fieldMap);
            //
            //        // Create the assignment using the field map
            Assignment assignment = new Assignment(fieldMap);
            assignment.save();
            //
            //        //Display notification
            Snackbar.make(view, "Assignment Saved", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
          }
        });
  }
}

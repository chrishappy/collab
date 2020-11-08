package com.themusicians.musiclms;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.entity.Node.Assignment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AssignmentInstrumentedTest {
  protected Assignment assignment;
  protected Assignment loadAssignment;
  protected Map<String, Object> fieldMap;

  protected String assignmentName = "Assignment Name";
  private String otherAssignmentName = "Other Assignment Name";

  @Before
  public void before() throws Exception {
    // Map the fields
    fieldMap = new HashMap<>();
    fieldMap.put("name", assignmentName);
    fieldMap.put("classId", -1);
    fieldMap.put("dueDate", 123);

    // Set Lists Fields
    List<String> dummyList =  new LinkedList<>();
    dummyList.add("fewsfj32");
    dummyList.add("wfef");
    fieldMap.put("assignees", dummyList);
    dummyList.add("3-09ger");
    fieldMap.put("attachmentIds", dummyList);
  }

  @After
  public void after() throws Exception {
  }

  /**
   *
   * Constructor: Assignment(String id)
   *
   */
  @Test
  public void testAssignmentConstructor_loadById() throws Exception {
//    Assignment loadAssignment = new Assignment(fieldMap);
  }

  /**
   *
   * Method: save()
   *
   */
  @Test
  public void testSave() throws Exception {
    // Save the data
    assignment = new Assignment( fieldMap );
    assignment.setField("name", otherAssignmentName);
    assertTrue(assignment.save());

    DatabaseReference nodeDatabase = FirebaseDatabase.getInstance().getReference(assignment.BASE_TABLE);

    ValueEventListener nodeListener = new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        // Get node object and use the values to update the UI
        loadAssignment = dataSnapshot.getValue(Assignment.class);
        assertEquals(loadAssignment.getLabel(), otherAssignmentName);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        assertTrue("Loading failed", false);
      }
    };
    nodeDatabase.child( assignment.id() ).addListenerForSingleValueEvent(nodeListener);
  }
}
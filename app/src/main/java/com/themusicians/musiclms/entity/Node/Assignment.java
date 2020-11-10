package com.themusicians.musiclms.entity.Node;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.themusicians.musiclms.entity.Attachment.Attachment;

import java.util.List;
import java.util.Map;

/**
 * @file Assignment.java
 *
 * ....
 *
 * Contributors: Nathan Tsai
 * Created by Nathan Tsai on 2020-11-02
 *
 * --------------------------------
 *
 * @todo Create Edit Form
 * @todo Create UI
 */

public class Assignment extends Node {

  /**
   * Base table for saving the data
   */
  public final static String BASE_TABLE = "node__assignments";

  /**
   * Firebase's Realtime Database
   */
  private DatabaseReference nodeDatabase;

  public String type = "assignment";

  /**
   * The fields for the Assignment
   *
   * Public properties will be automatically saved by Firebase
   * Private will not
   */
  public List<String> assignees;

  public int classId;

  public long dueDate;

  public List<String> attachmentIds;

  public String[] allowedAttachments;

  private Map<Map, Attachment> attachments;

  /**
   * The default constructor for Firebase + loadMultiple
   */
  public Assignment() {
    super();
  }

  /**
   * @param valueMap The fields values for the Node
   */
  public Assignment(Map<String, Object> valueMap) {
    super(valueMap);

    setFields(valueMap);
  }

  /*** End fields ***/

  /**
   *
   * @return The label of the entity: Assignment 1 or 2, etc
   */
  @Override
  public String getLabel() {
    return this.name;
  }

  /**
   * Save the Node to the Database
   *
   * @return whether it was successful or not
   */
  public boolean save() {

    writeAssignment();

    return true;
  }

  /**
   *
   * @return Boolean
   */
  private boolean writeAssignment() {
    nodeDatabase = FirebaseDatabase.getInstance().getReference(BASE_TABLE);

    final boolean[] result = {false};

    // Set default created time
    if ( isNew && getField("created") == null) {
      setField("created", ServerValue.TIMESTAMP);
    }

    // Set default updated time
    if ( getField("updated") == null) {
      setField("updated", ServerValue.TIMESTAMP);
    }

    // If we're creating an Assignment
    if (id() == null) {
      Log.println(Log.INFO, "assignment", "Create new Assignment" );

      String assignmentID = nodeDatabase.push().getKey();
      nodeDatabase.child(assignmentID).setValue(this, new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
          if (databaseError != null) {
            result[0] = false;
            System.out.println("Data could not be saved " + databaseError.getMessage());
          } else {
            result[0] = true;
            System.out.println("Data saved successfully.");
          }
        }
      });
      setField("id", assignmentID);
    }
    else {
      Log.println(Log.INFO, "assignment", "Update Assignment" );

      nodeDatabase.child( id() ).setValue(this, new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
          if (databaseError != null) {
            result[0] = false;
            System.out.println("Data could not be saved " + databaseError.getMessage());
          } else {
            result[0] = true;
            System.out.println("Data saved successfully.");
          }
        }
      });
    }

    return result[0];
  }

  /**
   * Settings and Getters
   */
  /**/

  public List<String> getAssignees() {
    return assignees;
  }

  public void setAssignees(List<String> assignees) {
    this.assignees = assignees;
  }

  public int getClassId() {
    return classId;
  }

  public void setClassId(int classId) {
    this.classId = classId;
  }

  public long getDueDate() {
    return dueDate;
  }

  public void setDueDate(long dueDate) {
    this.dueDate = dueDate;
  }

  public String[] getAllowedAttachments() {
    return allowedAttachments;
  }

  public void setAllowedAttachments(String[] allowedAttachments) {
    this.allowedAttachments = allowedAttachments;
  }
  /**/
}

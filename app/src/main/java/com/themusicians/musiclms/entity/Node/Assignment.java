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
 *     <p>....
 *     <p>Contributors: Nathan Tsai Created by Nathan Tsai on 2020-11-02
 *     <p>--------------------------------
 * @todo Create Edit Form
 * @todo Create UI
 */
public class Assignment extends Node {

  /** Firebase's Realtime Database */
  private DatabaseReference entityDatabase;

  public String type = "assignment";

  /**
   * The fields for the Assignment
   *
   * <p>Public properties will be automatically saved by Firebase Private will not
   */
  public List<String> assignees;

  public String classId;

  public long dueDate;

  public List<String> attachmentIds;

  public List<String> allowedAttachments;

  private Map<Map, Attachment> attachments;

  /** The default constructor for Firebase + loadMultiple */
  public Assignment() {
    super();
  }

  /**
   * Save the Node to the Database
   *
   * @return whether it was successful or not
   */
  public boolean save() {

    writeEntity();

    return true;
  }

  /** @return Boolean */
  private boolean writeEntity() {
    entityDatabase = FirebaseDatabase.getInstance().getReference( getBaseTable() );


    // Set default created time
    if (isNew && getCreated() == null) {
      setCreated(ServerValue.TIMESTAMP);
    }

    // Set default updated time
    if (getUpdated() == null) {
      setUpdated(ServerValue.TIMESTAMP);
    }

    // If we're creating an Assignment
    if (getId() == null) {
      Log.println(Log.INFO, getEntityType() + "__" + getType(), "Create new entity");

      setId( entityDatabase.push().getKey() );
    } else {
      Log.println(Log.INFO, getEntityType() + "__" + getType(), "Update Entity: " + getId());
    }

    final boolean[] result = {false};
    entityDatabase
        .child( getId() )
        .setValue(
            this,
            new DatabaseReference.CompletionListener() {
              @Override
              public void onComplete(
                  DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                  result[0] = false;
                  System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                  result[0] = true;
                  System.out.println("Data saved successfully.");
                }
              }
            });


    return result[0];
  }

  /** Settings and Getters */

  public List<String> getAssignees() {
    return assignees;
  }

  public void setAssignees(List<String> assignees) {
    this.assignees = assignees;
  }

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public long getDueDate() {
    return dueDate;
  }

  public void setDueDate(long dueDate) {
    this.dueDate = dueDate;
  }

  public List<String> getAllowedAttachments() {
    return allowedAttachments;
  }
  /**/
}

package com.themusicians.musiclms.entity.Node;

import android.util.Log;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;
import com.themusicians.musiclms.entity.Attachment.Attachment;
import java.util.List;
import java.util.Map;

/**
 * @file Assignment.java
 *
 * @contributor
 * @author Nathan Tsai
 * @since Nov 2, 2020
 */
@IgnoreExtraProperties
public class Assignment extends Node {

  protected String type = "assignment";

  /**
   * The fields for the Assignment
   *
   * <p>Public properties will be automatically saved by Firebase Private will not
   */
  protected List<String> assignees;

  protected String classId;

  protected long dueDate;

  protected List<String> attachmentIds;

  protected List<String> allowedAttachments;

  protected Map<Map, Attachment> attachments;

  /** The default constructor for Firebase + loadMultiple */
  public Assignment() {
    super();
  }

  public Assignment(String id) {
    super(id);
  }
  
  /**
   * Implement getBaseTable()
   * @return the database table to store the entity
   */
  @Override
  public String getBaseTable() {
    return "node__assignment";
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

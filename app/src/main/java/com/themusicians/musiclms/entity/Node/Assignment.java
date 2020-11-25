package com.themusicians.musiclms.entity.Node;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.themusicians.musiclms.entity.Attachment.Attachment;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Assignment.java
 *
 * @contributor
 * @author Nathan Tsai
 * @since Nov 2, 2020
 */
@IgnoreExtraProperties
public class Assignment extends Node {

  /** The type of node. Must be final */
  protected final String type = "assignment";

  /**
   * The fields for the Assignment
   *
   * <p>Public properties will be automatically saved by Firebase Private will not
   */
  protected List<String> assignees;

  protected String classId;

  protected long dueDate;

  protected Map<String, Boolean> toDoIds;

  // This must make the field name of toDoIds
  public static final String toDoIdsName = "toDoIds";

  protected Map<Map, Attachment> attachments;

  /** The default constructor for Firebase + loadMultiple */
  public Assignment() {
    super();

    System.out.println("The Assignment base table is: " + getBaseTable());
  }

  /** The constructor used to update an existing or to set an id */
  public Assignment(String id) {
    super(id);
  }

  /** Load an assignment from the database */
  /*
  public Assignment load(String entityId) {
    final String LOAD_ASSIGNMNET_TAG = "Load Assignment";
    final Assignment[] loadAssignment = new Assignment[1];
    entityDatabase.child( entityId )
        .addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            loadAssignment[0] = dataSnapshot.getValue(Assignment.class);
            Log.w(LOAD_ASSIGNMNET_TAG, "loadAssignment:onDataChange");
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w(LOAD_ASSIGNMNET_TAG, "loadAssignment:onCancelled", databaseError.toException());
            // ...
          }
        });

    // Set Assignment to not load
    loadAssignment[0].enforceNew(false);

    return loadAssignment[0];
  }
  /**/

  /**
   * Implement getType()
   *
   * @return the type of node
   */
  @Override
  public String getType() {
    return type;
  }

  /**
   * Return the attachments that can be added to an assignment
   *
   * @return a list of attachment ids
   */
  @Override
  @Exclude
  public List<String> getAllowedAttachments() {
    return new LinkedList<String>() {
      {
        add("comment");
        add("file");
      }
    };
  }

  /**
   * Implement getBaseTable()
   *
   * @return the database table to store the entity
   */
  @Override
  public String getBaseTable() {
    return getEntityType() + "__" + getType();
  }

  /**
   * Get To Do Items location
   *
   * <p>Assumes assignment has an id
   */
  @Exclude
  public DatabaseReference getToDoItemsKeyQuery() {
    return getEntityDatabase().child(getId()).child(toDoIdsName);
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

  public Map<String, Boolean> getToDoIds() {
    if (toDoIds == null) {
      toDoIds = new HashMap<>();
    }
    return toDoIds;
  }

  public void addToDoId(String toDoId) {
    if (toDoIds == null) {
      toDoIds = new HashMap<>();
    }
    toDoIds.put(toDoId, true);
  }

  public void setToDoIds(Map<String, Boolean> toDoIds) {
    this.toDoIds = toDoIds;
  }
  /**/
}

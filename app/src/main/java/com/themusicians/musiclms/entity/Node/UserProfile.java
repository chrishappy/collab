package com.themusicians.musiclms.entity.Node;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Map;

/**
 * @file Assignment.java
 *     <p>....
 *     <p>Contributors: Nathan Tsai Created by Nathan Tsai on 2020-11-02
 *     <p>--------------------------------
 * @todo Create Edit Form
 * @todo Create UI
 */
public class UserProfile extends Node {

  /** Base table for saving the data */
  public static String baseTable = "node__user_profile";

  /** Firebase's Realtime Database */
  private DatabaseReference userDatabse;

  /**
   * The fields for the default Node
   *
   * <p>Public properties will be automatically saved by Firebase Private will not
   */
  public String name;

  //  public List<String> assignees;
  //
  //  public int classId;

  /** The default constructor for Firebase + loadMultiple */
  public UserProfile() {
    super();
  }

  /**
   * The load constructor: the model doesn't work with RealTime model
   *
   * @param id
   */
  //  public Assignment(String id) {
  ////    super(id);
  //
  //    nodeDatabase = FirebaseDatabase.getInstance().getReference(baseTable);
  //
  //    ValueEventListener nodeListener = new ValueEventListener() {
  //      @Override
  //      public void onDataChange(DataSnapshot dataSnapshot) {
  //        // Get node object and use the values to update the UI
  //        Node node = dataSnapshot.getValue(Node.class);
  //
  //        isNew = false;
  //        fieldMap = node.getFields();
  //      }
  //
  //      @Override
  //      public void onCancelled(DatabaseError databaseError) {
  //        Log.w(DATABASE_TAG, "loadPost:onCancelled", databaseError.toException());
  //      }
  //    };
  //
  //    nodeDatabase.addListenerForSingleValueEvent(nodeListener);
  //  }

  /**
   * @param valueMap The fields values for the Node
   * @return
   */
  public UserProfile(Map<String, Object> valueMap) {
    super(valueMap);

    valueMap.put("entityType", "assignment");

    setFields(valueMap);
  }

  /** * End fields ** */

  /** @return */
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
   * Rename file
   *
   * @return
   */
  private boolean writeAssignment() {
    userDatabse = FirebaseDatabase.getInstance().getReference(baseTable);

    // If we're creating an Assignment
    if (isNew) {
      String assignmentID = userDatabse.push().getKey();
      userDatabse.child(assignmentID).setValue(this);
    } else {
      userDatabse.child(id()).setValue(this);
    }

    return true;
  }
}

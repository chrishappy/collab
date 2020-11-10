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
    DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference(baseTable);

    // If we're creating an Assignment
    if (isNew) {
      String assignmentID = userDatabase.push().getKey();
      userDatabase.child(assignmentID).setValue(this);
    } else {
      userDatabase.child( getId()).setValue(this);
    }

    return true;
  }
}

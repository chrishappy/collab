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
public class User extends Node {
  /**
   * Set the type of Node
   */
  protected final String type = "user";

  /**
   * The fields for the default Node
   *
   * <p>Public properties will be automatically saved by Firebase Private will not
   */

  //  public List<String> assignees;
  //
  //  public int classId;

  /** The default constructor for Firebase + loadMultiple */
  public User() {
    super();
  }

  public User( String id ) {
    super( id );
  }

}

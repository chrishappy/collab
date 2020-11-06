package com.themusicians.musiclms.entity.Node;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.themusicians.musiclms.entity.Attachment.Attachment;
import com.themusicians.musiclms.entity.Entity;

import java.util.ArrayList;
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
   * Firebase's Realtime Database
   */
  private DatabaseReference nodeDatabase;

  /**
   * The fields for the default Node
   *
   * Public properties will be automatically saved by Firebase
   * Private will not
   */
  public String name;

  public List<Integer> assignees;

  public int classId;

  public List<Integer> attachmentIds;

  public String[] allowedAttachments;

  private Map<Map, Attachment> attachments;

  /*** End fields ***/

  /**
   *
   * @return
   */
  @Override
  public String getLabel() {
    return this.name;
  }

  /**
   *
   * @param valueMap The fields values for the Node
   *
   * @return
   */
  @Override
  public Assignment create(Map<String, Object> valueMap) {
    return null;
  }


  /**
   *
   * @return
   */
  @Override
  public Assignment load(int id) {
    return null;
  }


  /**
   *
   * @return
   */
  @Override
  public List<Entity> loadMultiple(int[] id) {
    return null;
  }


  /**
   * Save the Node to the Database
   *
   * @return whether it was successful or not
   */
  public boolean save() {

    this.writeNode(this);

    return true;
  }

  /**
   *
   * @return
   */
  private boolean writeNode( Assignment assignment ) {
    nodeDatabase = FirebaseDatabase.getInstance().getReference("node__assignments");

    // If a new Assignment
    if (true) {
      String assignmentID = nodeDatabase.push().getKey();
      nodeDatabase.child(assignmentID).setValue(assignment);
    }

    return true;
  }

}

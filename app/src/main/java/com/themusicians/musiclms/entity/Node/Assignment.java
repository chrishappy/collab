package com.themusicians.musiclms.entity.Node;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.entity.Attachment.Attachment;
import com.themusicians.musiclms.entity.Entity;

import java.lang.reflect.Field;
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
   * Base table for saving the data
   */
  public static String baseTable = "node__assignments";

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

  public List<String> assignees;

  public int classId;

  public List<Integer> attachmentIds;

  public String[] allowedAttachments;

  private Map<Map, Attachment> attachments;

  /**
   * The default constructor for Firebase + loadMultiple
   */
  public Assignment() {
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
  public Assignment(Map<String, Object> valueMap) {
    super(valueMap);

    valueMap.put("entityType", "assignment");

    setFields(valueMap);
  }

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
   * @return
   */
  private boolean writeAssignment() {
    nodeDatabase = FirebaseDatabase.getInstance().getReference(baseTable);

    // If we're creating an Assignment
    if (isNew) {
      String assignmentID = nodeDatabase.push().getKey();
      nodeDatabase.child(assignmentID).setValue(this);
    }
    else {
      nodeDatabase.child( id() ).setValue(this);
    }

    return true;
  }

}

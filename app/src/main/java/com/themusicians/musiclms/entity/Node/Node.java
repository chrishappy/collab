package com.themusicians.musiclms.entity.Node;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.themusicians.musiclms.entity.Attachment.Attachment;
import com.themusicians.musiclms.entity.Entity;
import java.util.List;
import java.util.Map;

/**
 * @file Node.java
 *     <p>....
 *     <p>Contributors: Nathan Tsai Created by Nathan Tsai on 2020-11-02
 *     <p>--------------------------------
 * @todo Create Class
 * @todo Create UI
 */
public class Node extends Entity {

  /** Firebase's Realtime Database */
  protected String entityType = "node";

  /**
   * The fields for the default Node
   *
   * <p>Public properties will be automatically saved by Firebase Private will not
   */
  protected String name;

  protected List<String> attachmentIds;

  protected List<String> allowedAttachments;

  protected Map<Map, Attachment> attachments;

  /*** End fields ***/

  /**
   * Implements getLabel()
   *
   *  @return The name of the node: Assignment 1 or 2, etc
   */
  @Override
  public String getLabel() {
    return this.name;
  }

  /** Default constructor for Firebase */
  public Node() {
    super();
  }

  /**
   * @param id The fields for the default Entity
   * @return The List of the Entities
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

  /** Setters and Getters */
  /* */

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getAttachmentIds() {
    return attachmentIds;
  }

  public void setAttachmentIds(List<String> attachmentIds) {
    this.attachmentIds = attachmentIds;
  }

  public List<String> getAllowedAttachments() {
    return allowedAttachments;
  }

  /**/

}

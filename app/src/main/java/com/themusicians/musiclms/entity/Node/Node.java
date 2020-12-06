package com.themusicians.musiclms.entity.Node;

import android.util.Log;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;
import com.themusicians.musiclms.entity.Attachment.AllAttachment;
import com.themusicians.musiclms.entity.Attachment.Attachment;
import com.themusicians.musiclms.entity.Entity;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @file Node.java
 * @author Nathan Tsai
 * @since 2020-11-02
 */
public abstract class Node extends Entity implements Cloneable {

  /** The entity type. Must be final */
  protected final String entityType = "node";

  /**
   * The fields for the default Node
   *
   * <p>Public properties will be automatically saved by Firebase Private will not
   */
  protected String name;

  protected final static String attachmentName = "attachmentIds";

  protected List<String> allowedAttachments;

  protected Map<String, Boolean> attachmentIds;

  /*** End fields ***/

  /** Default constructor for Firebase */
  public Node() {
    super();
  }

  /** Constructor for an existing Node */
  public Node(String id) {
    super(id);
  }

  /** Clone */
  @NotNull
  @Override
  public Object clone()  throws CloneNotSupportedException {
    return super.clone();
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
   * Implement get entity type
   *
   * @return
   */
  @Override
  public String getEntityType() {
    return entityType;
  }

  /**
   * Get Attachments location
   *
   * <p>Assumes assignment has an id
   */
  @Exclude
  public DatabaseReference getAttachmentsKeyReference() {
    return getEntityDatabase().child(getId()).child(attachmentName);
  }

  /**
   * This function runs before saving an object
   */
  public void postSave() {}

  /**
   * Save the Node
   */
  public void save() {
    // the entity is no longer new
    setIsNew(false);

    // Save the entity
    writeEntity();

    // Run actions afterwards
    postSave();
  }

  /** Write to database */
  private void writeEntity() {

    // Set default created time
    if (isNew && getCreated() == null) {
      setCreated(ServerValue.TIMESTAMP);
    }

    // Set default updated time
//    if (getUpdated() == null) {
      setUpdated(ServerValue.TIMESTAMP);
//    }

    // If we're creating an Assignment
    if (getId() == null) {
      Log.println(Log.INFO, getEntityType() + "__" + getType(), "Create new entity");

      setId(entityDatabase.push().getKey());
    } else {
      Log.println(Log.INFO, getEntityType() + "__" + getType(), "Update Entity: " + getId());
    }

    entityDatabase
        .child(getId())
        .setValue(
            this,
            new DatabaseReference.CompletionListener() {
              @Override
              public void onComplete(
                  DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                  System.out.println(
                      "Entity data could not be saved " + databaseError.getMessage());
                } else {
                  System.out.println("Entity data saved successfully.");
                }
              }
            });

    setIsNew(false);
  }

  /**
   * Deletes the node attachments
   *
   */
  @Override
  public void delete() {
    if (getAttachmentIds() != null && !getAttachmentIds().isEmpty()) {
      Attachment tempAttachment = new AllAttachment();
      for (String attachmentId : getAttachmentIds().keySet()) {
        tempAttachment.setId(attachmentId);
        tempAttachment.delete();
      }
    }

    super.delete();
  }

  /** Setters and Getters */
  /* */

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, Boolean> getAttachmentIds() {
    if (attachmentIds == null) {
      attachmentIds = new HashMap<>();
    }

    return attachmentIds;
  }

  public void addAttachmentIdDirectlyToDatabase(String attachmentId) {
    getAttachmentsKeyReference()
        .child(attachmentId)
        .setValue(true);

    addAttachmentId(attachmentId);
  }

  public Node addAttachmentId(String attachmentId) {
    if (getAttachmentIds().get(attachmentId) == null) {
      getAttachmentIds().put(attachmentId, true);
    }

    return this;
  }

  public List<String> getAllowedAttachments() {
    return allowedAttachments;
  }

  /**/

}

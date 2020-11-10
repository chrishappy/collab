package com.themusicians.musiclms.entity.Node;

import com.google.firebase.database.DatabaseReference;
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

  /** /** Firebase's Realtime Database */
  protected DatabaseReference mDatabase;

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
   * Constructor by field valueMap
   *
   * @param valueMap The fields values for the Node
   */
  public Node(Map<String, Object> valueMap) {
    super(valueMap);
  }

  /**
   * @param id The fields for the default Entity
   * @return The List of the Entities
   */
  @Override
  public List<Entity> loadMultiple(int[] id) {
    return null;
  }

  /** @return Boolean */
  private boolean writeNode() {
    return true;
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

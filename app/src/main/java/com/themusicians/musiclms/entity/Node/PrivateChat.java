package com.themusicians.musiclms.entity.Node;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @file Assignment.java
 * @contributor Shifan He
 * @author Nathan Tsai
 * @since Nov 16, 2020
 */
@IgnoreExtraProperties
public class PrivateChat extends Node {

  protected final String type = "chat_private";

  /** The chat will be between the author (uid) and this user (otherUid) */
  protected String otherUid;

  /** The default constructor for Firebase + loadMultiple */
  public PrivateChat() {
    super();
  }

  public PrivateChat(String id) {
    super(id);
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

  /** Implement getType() */
  @Override
  public String getType() {
    return type;
  }

  /** Setters and getters */
  public String getOtherUid() {
    return otherUid;
  }

  public void setOtherUid(String otherUid) {
    this.otherUid = otherUid;
  }
}

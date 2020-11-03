package com.themusicians.musiclms;

import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Map;

public class Node extends Entity {

  /**
   * Firebase's Realtime Database
   */
  private DatabaseReference mDatabase;

  /**
   * The fields for the default Node
   */
  public String name;

  private List<Attachment> attachments;

  private String[] allowedAttachments;

  /* End fields */

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
  public Node create(Map<String, Object> valueMap) {
    return null;
  }


  /**
   *
   * @return
   */
  @Override
  public Node load(int id) {
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
   *
   * @return
   */
  private boolean writeNode() {
    return true;
  }

}

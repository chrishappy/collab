package com.themusicians.musiclms.entity.Node;

import com.google.firebase.database.DatabaseReference;
import com.themusicians.musiclms.entity.Attachment.Attachment;
import com.themusicians.musiclms.entity.Entity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @file Node.java
 *
 * ....
 *
 * Contributors: Nathan Tsai
 * Created by Nathan Tsai on 2020-11-02
 *
 * --------------------------------
 *
 * @todo Create Class
 * @todo Create UI
 */

public class Node extends Entity {

  /**
   * Firebase's Realtime Database
   */
  private DatabaseReference mDatabase;

  /**
   * The fields for the default Node
   *
   * Public properties will be automatically saved by Firebase
   * Private will not
   */
  public String name;

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
  public Node(Map<String, Object> valueMap) {
    super(valueMap);
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

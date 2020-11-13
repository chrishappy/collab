package com.themusicians.musiclms.entity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;
import com.themusicians.musiclms.entity.Node.User;

import java.util.List;
import java.util.Map;

/**
 * @file Entity.java
 *     <p>Contains the default class to be extended by Attachments and Nodes
 *     <p>Contributors: Nathan Tsai
 *     <p>Created by Nathan Tsai on 2020-11-02
 */
@IgnoreExtraProperties
public abstract class Entity implements EntityInterface {

  /** Firebase's Realtime Database */
  protected DatabaseReference entityDatabase;

  /** The fields for the default Entity */
  protected String id;

  /** Different types of entity: Assignments or Users profiles, etc */
  protected String type;

  /** Node or attachment */
  protected String entityType;

  /**
   * The time the Entity was created in UTC format The type is Object in order to save
   * ServerValue.TIMESTAMP. after loading.
   */
  protected Object created;

  /**
   * The time the Entity was last updated in UTC format The type is Object in order to save
   * ServerValue.TIMESTAMP. Cast to long after loading.
   */
  protected Object updated;

  /** Set status to false if the entity is unpublished */
  protected boolean status;

  /** Users' id number */
  protected String uid; // User who the entity belongs to

  /** Whether we could create a new database record for the entity when saving */
  protected boolean isNew = false;

  /** Tags for Log */
  protected final String LOG_TAG_DATABASE = "FirebaseDatabase";

  /** Default constructor without arguments for Firebase and ::loadMultiple */
  public Entity() {
    isNew = true;
  }

  /** Constructor for loading */
  public Entity(String id) {
    isNew = false;
    setId(id);
  }

  /**
   * Get the database table
   *
   * @return String the database table id
   */
  public String getBaseTable() {
    return getEntityType() + "__" + getType();
  }

  /**
   * @param valueMap The fields values for the Entity Create a new entity with data in valueMap
   * @return Entity
   */
  //    @Override
  //    public abstract Entity create(Map<String, Object> valueMap);

  /**
   * @param id The fields for the default Entity
   * @return The List of the Entities
   */
  @Override
  public abstract List<Entity> loadMultiple(int[] id);

  /** @return True if the data is correctly saved */
  @Override
  public abstract boolean save();

  /**
   * Implements delete()
   * Deletes the entity
   *
   * @return True if the data is correctly deleted */
  @Override
  public boolean delete() {
    return true;
  }

  /**
   * Get the id of the entity
   *
   * @return The fields for the default Entity */
  @Override
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /** @return The type of the entity: Assignments or Users profiles, etc */
  @Override
  public String getType() {
    return this.type;
  }

  /** @return The Entity Type (e.g. Node, Attachment) */
  @Override
  public String getEntityType() {
    return this.entityType;
  }

  /** @return The time that the Entity was created in UTC format */
  @Override
  public Object getCreated() {
    return this.created;
  }

  public void setCreated(Object created) {
    this.created = created;
  }

  /** @return The time that the Entity was last updated in UTC format */
  @Override
  public Object getUpdated() {
    return this.updated;
  }

  public void setUpdated(Object updated) {
    this.updated = updated;
  }

  /**
   * Allow entities to be unpublished, aka exist in database, but not
   * visible to everyone.
   *
   * @return bool true if entity is published
   */
  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  /**
   * Get the authour id
   *
   * @return String the Firebase user id
   */
  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  /**
   * Force the entity to be saved again in the database
   */
  public void enforceNew() {
    isNew = true;
  }

  /**
   * Conditionally enfore the entity to be saved again in the database
   *
   * @param inputIsNew true if the entity should be resaved.
   */
  public void enforceNew(boolean inputIsNew) {
    isNew = inputIsNew;
  }

  /**
   * Need to save user information first
   */
//  public User getAuthor() {
//    User author = new User;
//
//    return author;
//  }

}

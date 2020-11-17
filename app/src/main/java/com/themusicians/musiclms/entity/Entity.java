package com.themusicians.musiclms.entity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Contains the default class to be extended by Attachments and Nodes
 *
 * @author Nathan Tsai
 * @since 2020-11-02
 */
@IgnoreExtraProperties
public abstract class Entity implements EntityInterface {

  /** Firebase's Realtime Database */
  protected DatabaseReference entityDatabase;

  /** The fields for the default Entity */
  protected String id;

  /** Different types of entity: Assignments or Users profiles, etc */
  protected String type;

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
    entityDatabase = FirebaseDatabase.getInstance().getReference( getBaseTable() );

    System.out.println("The Entity base table is: " + getBaseTable());

    isNew = true;
  }

  /** Constructor for loading */
  public Entity(String id) {
    entityDatabase = FirebaseDatabase.getInstance().getReference( getBaseTable() );

    System.out.println("The Entity2 base table is: " + getBaseTable());

    isNew = false;
    setId(id);
  }

  /**
   * Get the database table
   *
   * @return String the database table id
   */
  public abstract String getBaseTable();

  /**
   * Implement get entity type
   * @return the type of entity
   */
  @Override
  public abstract String getEntityType();

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
  public boolean getStatus() {
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
   * Conditionally enfore the entity to be saved again in the database
   *
   * @param inputIsNew true if the entity should be resaved.
   */
  public void setIsNew(boolean inputIsNew) {
    isNew = inputIsNew;
  }

  /**
   * Get the relevant database
   */
  @Exclude
  public DatabaseReference getEntityDatabase() {
    return entityDatabase;
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

package com.themusicians.musiclms.entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * @file EntityInterface.java
 *     <p>Contains the interface to be extended by Attachments and Nodes
 *     <p>Contributors: Nathan Tsai
 *     <p>Created by Nathan Tsai on 2020-11-02
 */
public interface EntityInterface {

  /** @return the entity id */
  String getId();

  /** @return The type of the entity: Assignments or Users profiles, etc */
  String getType();

  /** @return The Entity Type (e.g. Node, Attachment) */
  String getEntityType();

  /** @return The time that the Entity was created in UTC format */
  Object getCreated();

  /** @return The time that the Entity was last updated in UTC format */
  Object getUpdated();

  /**
   * @param valueMap The fields values for the Entity
   * @return The label of the entity: Assignment 1 or 2, etc
   */
  //    Entity create(Map<String, Object> valueMap );

  /**
   * @param id The id of the Entity to retrieve from the database
   * @return The Entity
   */
  //    Entity load( int id);

  /**
   * @param id The id array
   * @return The List of the Entities
   */
  List<Entity> loadMultiple(int[] id);

  /**
   * Set a bunch of properties dynamically
   *
   * @param valueMap The fields values for the Entity
   * @return the Entity class
   */
  //    Entity setFields(Map<String, Object> valueMap);

  /**
   * Set a property dynamically
   *
   * @param fieldName The name of the property
   * @param value The value to set it to.
   * @return The Entity with value
   */
  //    Entity setField(String fieldName, Object value);

  /**
   * Get all properties dynamically
   *
   * @return a map of the the fields
   */
  //    Map<String, Object> getFields();

  /**
   * Get a property dynamically
   *
   * @param fieldName
   * @return the value of the property
   */
  //    Object getField(String fieldName);

  /** @return True if the data is correctly saved */
  boolean save();

  /** @return True if the data is correctly deleted */
  boolean delete();
}

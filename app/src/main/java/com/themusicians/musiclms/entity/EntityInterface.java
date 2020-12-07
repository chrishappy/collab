package com.themusicians.musiclms.entity;

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

  /** Save and write the entity to the database */
  void save();

  /** @return True if the data is correctly deleted */
  void delete();
}

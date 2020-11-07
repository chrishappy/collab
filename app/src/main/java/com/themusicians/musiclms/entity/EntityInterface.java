package com.themusicians.musiclms.entity;

import java.util.List;
import java.util.Map;

/**
 * @file
 * EntityInterface.java
 *
 * Contains the interface to be extended by Attachments and Nodes
 *
 * Contributors: Nathan Tsai
 *
 * Created by Nathan Tsai on 2020-11-02
 */

public interface EntityInterface {

    /**
     *
     * @return the entity id
     */
    String id();

    /**
     *
     * @return
     */
    String getType();

    /**
     *
     * @return
     */
    String getEntityType();

    /**
     *
     * @return
     */
    long getCreatedTime();

    /**
     *
     * @return
     */
    long getUpdatedTime();

    /**
     *
     * @return
     */
    String getLabel();

    /**
     *
     * @param valueMap The fields values for the Entity
     *
     * @return
     */
//    Entity create(Map<String, Object> valueMap );

    /**
     *
     * @param id The id of the Entity to retrieve from the database
     *
     * @return
     */
//    Entity load( int id);

    /**
     *
     * @return
     */
    List<Entity> loadMultiple(int[] ids );

    /**
     * Set a bunch of properties dynamically
     *
     * @param valueMap
     *
     * @return the Entity class
     */
    Entity setFields(Map<String, Object> valueMap);

    /**
     * Set a property dynamically
     *
     * @param fieldName The name of the property
     * @param value The value to set it to.
     *
     * @return
     *
     */
    Entity setField(String fieldName, Object value);

    /**
     * Get all properties dynamically
     *
     * @return a map of the the fields
     */
    Map<String, Object> getFields();

    /**
     * Get a property dynamically
     *
     * @param fieldName
     *
     * @return the value of the property
     */
//    Object getField(String fieldName);

    /**
     *
     * @return
     */
    boolean save();

    /**
     *
     * @return
     */
    boolean delete();
}

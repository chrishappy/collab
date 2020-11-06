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
    int getCreatedTime();

    /**
     *
     * @return
     */
    int getUpdatedTime();

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
    Entity load( int id);

    /**
     *
     * @return
     */
    List<Entity> loadMultiple(int[] ids );

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

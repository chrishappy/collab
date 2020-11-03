package com.themusicians.musiclms;

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
     *
     * @return
     */
    int id();

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
     * @return
     */
    Attachment[] getAttachments();

    /**
     *
     * @param values The fields values for the Entity
     *
     * @return
     */
    Entity create( Object[] values );

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
    Entity[] loadMultiple( int[] ids );

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

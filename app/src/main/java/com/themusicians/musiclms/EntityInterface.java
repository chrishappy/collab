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
     * @return
     */
    Entity create( );

    /**
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

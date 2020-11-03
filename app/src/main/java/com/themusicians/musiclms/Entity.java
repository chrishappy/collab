package com.themusicians.musiclms;

/**
 * @file
 * Entity.java
 *
 * Contains the default class to be extended by Attachments and Nodes
 *
 * Contributors: Nathan Tsai
 *
 * Created by Nathan Tsai on 2020-11-02
 */
public class Entity implements EntityInterface {
    /**
     *
     * @return
     */
    public int id() {
        return 0;
    }

    /**
     *
     * @return
     */
    String bundle();

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
    Entity create();

    /**
     *
     * @return
     */
    Entity load();

    /**
     *
     * @return
     */
    Entity[] loadMultiple();

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

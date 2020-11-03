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
public abstract class Entity implements EntityInterface {


//    private String baseTable = "node";
//    private String dataTable = "node_field_data";

    /**
     * The fields for the default Entity
     */
    private int id;

    private String type;

    private String entityType;

    private int created; // In UTC timestamp format

    private int updated; // In UTC format

    private int uid; // User who the entity belongs to

    /**
     *
     * @return
     */
    public int id() {
        return this.id;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return this.type;
    }

    /**
     *
     * @return The Entity Type (e.g. Node, Attachment)
     */
    public String getEntityType() {
        return this.entityType;
    }

    /**
     *
     * @return
     */
    public int getCreatedTime() {
        return this.created;
    }

    /**
     *
     * @return
     */
    public int getUpdatedTime() {
        return this.updated;
    }

    /**
     *
     * @return
     */
    public abstract String getLabel();

    /**
     *
     * @return
     */
    public Attachment[] getAttachments() {

        // Dummy code
        Attachment temp = new Attachment();
        Attachment[] results = { temp  };
        return results;
    }

    /**
     *
     * @return
     */
    public abstract Entity create( Object[] values );

    /**
     *
     * @return
     */
    public abstract Entity load( int id );

    /**
     *
     * @return
     */
    public abstract Entity[] loadMultiple( int[] id);

    /**
     *
     * @return
     */
    public boolean save() {
        return true;
    }

    /**
     *
     * @return
     */
    public boolean delete() {
        return true;
    }
}

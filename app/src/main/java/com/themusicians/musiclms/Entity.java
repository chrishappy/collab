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


//    private String baseTable = "node";
//    private String dataTable = "node_field_data";

    /**
     * The fields for the default Entity
     */
//    private String

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
    public String getType() {
        return " "; //$this->bundle;
    }

    /**
     *
     * @return The Entity Type (e.g. Node, Attachment)
     */
    public String getEntityType() {
    return " "; //$this->entityType;
    }

    /**
     *
     * @return
     */
    public int getCreatedTime() {
        return 0;
    }

    /**
     *
     * @return
     */
    public int getUpdatedTime() {
        return 0;

    }

    /**
     *
     * @return
     */
    public String getLabel() {
        return " ";
    }

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
    Entity create();

    /**
     *
     * @return
     */
    public Entity load( int id ) {
        // Dummy code
        Entity temp = new Entity();
        return temp;
    }

    /**
     *
     * @return
     */
    public Entity[] loadMultiple( int[] id) {

        // Dummy code
        Entity temp = new Entity();
        Entity[] results = {  temp  };
        return results;
    }

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

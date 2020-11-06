package com.themusicians.musiclms.entity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

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
    public String id;

    public String type;

    public String entityType;

    public int created; // In UTC timestamp format

    public int updated; // In UTC format

    public boolean status;

    public int uid; // User who the entity belongs to

    /**
     * Default constructor without arguments for Firebase
     */
    public Entity() {
    }

    /**
     *
     * @param valueMap
     */
    public Entity(Map<String, Object> valueMap){
        for (String key : valueMap.keySet()){
            setField(key, valueMap.get(key));
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String id() {
        return this.id;
    }

    /**
     *
     * @return
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     *
     * @return The Entity Type (e.g. Node, Attachment)
     */
    @Override
    public String getEntityType() {
        return this.entityType;
    }

    /**
     *
     * @return
     */
    @Override
    public int getCreatedTime() {
        return this.created;
    }

    /**
     *
     * @return
     */
    @Override
    public int getUpdatedTime() {
        return this.updated;
    }

    /**
     *
     * @return
     */
    @Override
    public abstract String getLabel();

    /**
     *
     * @param valueMap The fields values for the Entity
     *
     * @return
     */
//    @Override
//    public abstract Entity create(Map<String, Object> valueMap);

    /**
     *
     * @return
     */
    @Override
    public abstract Entity load( int id );

    /**
     *
     * @return
     */
    @Override
    public abstract List<Entity> loadMultiple(int[] id);

    /**
     *
     * @return
     */
    @Override
    public boolean save() {
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean delete() {
        return true;
    }

    /**
     *
     * @param fieldName
     * @param value
     */
    private void setField(String fieldName, Object value) {
        Field field;
        try {
            field = getClass().getDeclaredField(fieldName);
            field.set(this, value);
        } catch (SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}

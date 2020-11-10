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

    /**
     * Base table for saving the data
     */
    protected String baseTable;

    /**
     * The fields for the default Entity
     */
    public String id;

    /**
     * Different types of entity: Assignments or Users profiles, etc
     */
    public String type;

    /**
     * Node or attachment
     */
    public String entityType;

    /**
     * The time the Entity was created in UTC format
     * The type is Object in order to save ServerValue.TIMESTAMP. after loading.
     */
    public Object created;

    /**
     * The time the Entity was last updated in UTC format
     * The type is Object in order to save ServerValue.TIMESTAMP. Cast to long after loading.
     */
    public Object updated;

    /**
     * Set status to 0 if the entity is unpublished
     */
    public boolean status;

    /**
     * Users' id number
     */
    public String uid; // User who the entity belongs to

    /**
     * The field map: contains all the data (Probably gonna delete it)
     */
    protected Map<String, Object> fieldMap;

    /**
     * Whether we could create a new database record for the entity when saving
     */
    protected boolean isNew = false;

    /**
     * Tags for Log
     */
    final protected String DATABASE_TAG = "FirebaseDatabase";

    /**
     * Default constructor without arguments for Firebase and ::loadMultiple
     */
    public Entity() {
    }

    /**
     * Constructor for loading
     */
    public Entity(String id) {

    }

    /**
     * The constructor for creating an Entity
     *
     * @param valueMap The fields values for the Entity
     */
    public Entity(Map<String, Object> valueMap){
        // If constructed, set value to true
        isNew = true;
    }

    /**
     *
     * @return The fields for the default Entity
     */
    @Override
    public String id() {
        return this.id;
    }

    /**
     *
     * @return The type of the entity: Assignments or Users profiles, etc
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
     * @return The time that the Entity was created in UTC format
     */
    @Override
    public Object getCreated() {
        return this.created;
    }

    /**
     *
     * @return The time that the Entity was last updated in UTC format
     */
    @Override
    public Object getUpdated() {
        return this.updated;
    }

    /**
     *
     * @return The label of the entity: Assignment 1 or 2, etc
     */
    @Override
    public abstract String getLabel();

    /**
     *
     * @param valueMap The fields values for the Entity
     * Create a new entity with data in valueMap
     * @return Entity
     */
//    @Override
//    public abstract Entity create(Map<String, Object> valueMap);

    /**
     * @param id The fields for the default Entity
     * @return The List of the Entities
     */
    @Override
    public abstract List<Entity> loadMultiple(int[] id);

    /**
     *
     * @return True if the data is correctly saved
     */
    @Override
    public boolean save() {
        return true;
    }

    /**
     *
     * @return True if the data is correctly deleted
     */
    @Override
    public boolean delete() {
        return true;
    }

    /**
     *
     * @param valueMap
     */
    public Entity setFields(Map<String, Object> valueMap) {
        for (String key : valueMap.keySet()){
            setField(key, valueMap.get(key));
        }

        // Allow chaining
        return this;
    }

    /**
     *
     * @return The fieldMap of the subject
     */
//    @Override
//    public Map<String, Object> getFields() {
//        fieldMap.put("id", id);
//        fieldMap.put("type", type);
//        fieldMap.put("entityType", entityType);
//        fieldMap.put("created", created);
//        fieldMap.put("updated", updated);
//        fieldMap.put("status", status);
//        fieldMap.put("uid", uid);
//
//        return fieldMap;
//    }

    /**
     *
     * @param fieldName The name of the property
     * @param value The value to set it to
     */
    public Entity setField(String fieldName, Object value) {
        Field field;
        try {
            field = getClass().getField(fieldName); // Can't get getDeclaredField because not recursive
            field.set(this, value);
        } catch (SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     *
     */
    public Object getField(String fieldName) {
        Field field;
        Object value = null;

        try {
            field = getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            value = field.get(null);
        } catch (SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return value;
    }
}

package com.themusicians.musiclms.entity.Attachment;

import android.util.Log;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.themusicians.musiclms.entity.Entity;
import java.util.List;

/**
 * @file Attachment.java
 * @author Nathan Tsai
 * @since Nov 2, 2020
 */
public abstract class Attachment extends Entity {

  /** Set the Node Type. Must be final */
  protected final String entityType = "attachment";

  /** The default constructor for Firebase + loadMultiple */
  public Attachment() {
    super();
  }

  public Attachment(String id) {
    super(id);
  }
  /**
   * Save the Node to the Database
   *
   * @return whether it was successful or not
   */
  public void save() {
    writeEntity();
  }

  /** @return Boolean */
  protected boolean writeEntity() {
    entityDatabase = FirebaseDatabase.getInstance().getReference(getBaseTable());

    // Set default created time
    if (isNew && getCreated() == null) {
      setCreated(ServerValue.TIMESTAMP);
    }

    // Set default updated time
    if (getUpdated() == null) {
      setUpdated(ServerValue.TIMESTAMP);
    }

    // If we're creating an Assignment
    if (getId() == null) {
      Log.println(Log.INFO, getBaseTable(), "Create new entity");

      setId(entityDatabase.push().getKey());
    } else {
      Log.println(Log.INFO, getBaseTable(), "Update Entity: " + getId());
    }

    final boolean[] result = {false};
    entityDatabase
        .child(getId())
        .setValue(
            this,
            new DatabaseReference.CompletionListener() {
              @Override
              public void onComplete(
                  DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                  result[0] = false;
                  System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                  result[0] = true;
                  System.out.println("Data saved successfully.");
                }
              }
            });

    setIsNew(true);

    return result[0];
  }

  @Override
  public List<Entity> loadMultiple(int[] id) {
    return null;
  }

  /**
   * Implement get entity type
   *
   * @return
   */
  @Override
  public String getEntityType() {
    return entityType;
  }

  /** Delete self */
  @Override
  public void delete() {
    getEntityDatabase().child(getId()).removeValue();

    super.delete();
  }
}

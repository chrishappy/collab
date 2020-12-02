package com.themusicians.musiclms.entity.Attachment;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * The class used to store data in Firebase for the All Attachment
 *
 * @author Nathan Tsai
 * @since Nov 10, 2020
 */
@IgnoreExtraProperties
public class AllAttachment extends Attachment {

  protected final String type = "all";

  /** Comment fields */
  protected String comment;

  /** File Fields */
  protected String fileId;

  /** Default constructor */
  public AllAttachment() {
    super();
  }

  /** Default constructor */
  public AllAttachment(String id) {
    super(id);
  }

  /**
   * Implement getBaseTable()
   *
   * @return the database table to save files to
   */
  @Override
  public String getBaseTable() {
    return getEntityType() + "__" + getType();
  }

  /**
   * Implement getType()
   *
   * @return the type of attachment
   */
  @Override
  public String getType() {
    return type;
  }

  /**
   * Save the file to database + save the file to FireStore
   *
   * @return Whether the entity was saved or not
   */
  @Override
  public boolean save() {
    super.save();
    // Save the file here

    return true;
  }

  /** Comment setters and getters */
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  /** File setters and getters */
  public String getFileId() {
    return fileId;
  }

  public void setFileId(String fileId) {
    this.fileId = fileId;
  }
}

package com.themusicians.musiclms.entity.Attachment;

/**
 * The File Attachment class
 *
 * @contributor Mingyang Wei
 * @author Nathan Tsai
 * @since Nov 12, 2020
 */
public class File extends Attachment {

  protected final String type = "file";

  /** Where the file is stored */
  protected String fileId;

  /** Default constructor */
  public File() {
    super();
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
  public void save() {
    super.save();
    // Save the file here

  }

  /** The setters and getters for where the file is stored */
  public String getFileId() {
    return fileId;
  }

  public void setFileId(String fileId) {
    this.fileId = fileId;
  }
}

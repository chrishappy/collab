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
  protected String fileUri;

  protected String downloadFileUri;

  /** Zoom Fields */
  protected String zoomId;

  protected String zoomPassword;

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
  public void save() {
    super.save();
    // @Todo Save the file here

  }
  /** Delete the file */
  @Override
  public void delete() {
    // @Todo delete the file here

    super.delete();
  }

  /** Comment setters and getters */
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  /** File setters and getters */
  public String getFileUri() {
    return fileUri;
  }

  public void setFileUri(String fileUri) {
    this.fileUri = fileUri;
  }

  public String getDownloadFileUri() {
    return downloadFileUri;
  }

  public void setDownloadFileUri(String downloadFileUri) {
    this.downloadFileUri = downloadFileUri;
  }

  /** Zoom Setters and Getters */
  public String getZoomId() {
    return zoomId;
  }

  public void setZoomId(String zoomId) {
    this.zoomId = zoomId;
  }

  public String getZoomPassword() {
    return zoomPassword;
  }

  public void setZoomPasscode(String zoomPassword) {
    this.zoomPassword = zoomPassword;
  }
}

package com.themusicians.musiclms.entity.Attachment;

/**
 * The Comment Attachment class
 *
 * @author Nathan Tsai
 * @since Nov 10, 2020
 */
public class Comment extends Attachment {

  protected final String type = "comment";

  protected String comment;

  /**
   *
   * @return
   */
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   *
   * @return
   */
  @Override
  public String getBaseTable() {
    return getEntityType() + "__" + getType();
  }

  /**
   *
   */
  public Comment() {
    super();
  }

  @Override
  public String getType() {
    return type;
  }
}

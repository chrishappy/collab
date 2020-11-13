package com.themusicians.musiclms.entity.Attachment;

/**
 * The Comment Attachment class
 *
 * @author Nathan Tsai
 * @since Nov 10, 2020
 */
public class Comment extends Attachment {

  protected String type = "comment";

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
    return "node__comment";
  }

  /**
   *
   */


  public Comment() {
    super();
  }
}

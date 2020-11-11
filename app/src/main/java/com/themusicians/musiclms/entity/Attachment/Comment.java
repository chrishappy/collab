package com.themusicians.musiclms.entity.Attachment;

/**
 * The Comment Attachment class
 *
 * @author Nathan Tsai
 * @since Nov 10, 2020
 */
public class Comment extends Attachment {

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  protected String comment;

  public Comment() {
    super();

    type = "comment";
  }
}

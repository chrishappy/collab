package com.themusicians.musiclms.entity.Attachment;

/**
 * The Comment Attachment class
 *
 * @author Nathan Tsai
 * @since Nov 10, 2020
 */
public class Comment extends Attachment {

  protected final String type = "comment";
  
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  protected String comment;

  public Comment() {
    super();
  }
}

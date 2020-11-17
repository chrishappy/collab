package com.themusicians.musiclms.entity.Attachment;



/**
 * @file Attachment.java
 * @contributor Shifan He
 * @author Nathan Tsai
 * @since Nov 12, 2020
 */
public class ChatMessage extends Attachment {

  protected final String type = "chat_message";

  protected String messageText;
  protected String messageUser;
  protected long messageTime;

  public ChatMessage() {
    super();
  }

  /**
   * Implement getBaseTable()
   *
   * @return the database to save chat messages
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

  public String getMessageText() {
    return messageText;
  }

  public void setMessageText(String messageText) {
    this.messageText = messageText;
  }

  public String getMessageUser() {
    return messageUser;
  }

  public void setMessageUser(String messageUser) {
    this.messageUser = messageUser;
  }

  public long getMessageTime() {
    return messageTime;
  }

  public void setMessageTime(long messageTime) {
    this.messageTime = messageTime;
  }
}

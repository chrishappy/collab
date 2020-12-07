package com.themusicians.musiclms.chat;


/**
 * Class for chat information
 *
 * @author Jerome Lau
 * @since Nov 24, 2020
 */
public class ChatClass {

  private String sender;
  private String receiver;
  private String message;

  public ChatClass(String sender, String receiver, String message) {
    this.sender = sender;
    this.receiver = receiver;
    this.message = message;
  }

  public ChatClass() {}

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

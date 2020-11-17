package com.themusicians.musiclms.entity.Node;

import java.util.List;

/**
 * @file Assignment.java
 * @contributor Jerome Lau
 * @author Nathan Tsai
 * @since Nov 2, 2020
 *     <p>--------------------------------
 * @todo Create Edit Form
 * @todo Create UI
 */
public class User extends Node {
  /** Set the type of Node. Must be final */
  protected final String type = "user";

  /**
   * The user's email
   *
   * @todo implement later
   */
  protected String email;

  /**
   * Whether the user is a teacher, student
   *
   * @todo add parent
   */
  protected String role;

  /**
   * Tech Experience
   *
   * <p>Create a list: List<String> techExperience = new LinkedList<String>();
   * techExperience.add("makePhoneCall"); techExperience.add("ZoomCall"); user.setTechExperience(
   * techExperience ); user.save();
   */
  protected List<String> techExperience;

  /** Instruments */
  protected List<String> instruments;

  /**
   * The fields for the default Node
   *
   * <p>Public properties will be automatically saved by Firebase Private will not
   */

  //  public List<String> assignees;
  //
  //  public int classId;

  /** The default constructor for Firebase + loadMultiple */
  public User() {
    super();
  }

  public User(String id) {
    super(id);
  }

  /**
   * Implement getBaseTable()
   *
   * @return the database table to store the entity
   */
  @Override
  public String getBaseTable() {
    return getEntityType() + "__" + getType();
  }

  /**
   * Implement getType()
   *
   * @return the type of entity
   */
  @Override
  public String getType() {
    return type;
  }

  /** Fields setters and getters */
  public List<String> getTechExperience() {
    return techExperience;
  }

  public void setTechExperience(List<String> techExperience) {
    this.techExperience = techExperience;
  }

  public List<String> getInstruments() {
    return instruments;
  }

  public void setInstruments(List<String> instruments) {
    this.instruments = instruments;
  }

  /** */
  protected String sendText;

  protected String makeCall;
  protected String joinZoom;
  protected String scheduleZoom;
  protected String watchYoutube;
  protected String uploadYoutube;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSendText() {
    return sendText;
  }

  public void setSendText(String sendText) {
    this.sendText = sendText;
  }

  public String getMakeCall() {
    return makeCall;
  }

  public void setMakeCall(String makeCall) {
    this.makeCall = makeCall;
  }

  public String getJoinZoom() {
    return joinZoom;
  }

  public void setJoinZoom(String joinZoom) {
    this.joinZoom = joinZoom;
  }

  public String getScheduleZoom() {
    return scheduleZoom;
  }

  public void setScheduleZoom(String scheduleZoom) {
    this.scheduleZoom = scheduleZoom;
  }

  public String getWatchYoutube() {
    return watchYoutube;
  }

  public void setWatchYoutube(String watchYoutube) {
    this.watchYoutube = watchYoutube;
  }

  public String getUploadYoutube() {
    return uploadYoutube;
  }

  public void setUploadYoutube(String uploadYoutube) {
    this.uploadYoutube = uploadYoutube;
  }
}

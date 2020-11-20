package com.themusicians.musiclms.entity.Node;

import com.themusicians.musiclms.data.Result;

import java.util.ArrayList;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

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

  public List<String> getInstruments() { return instruments; }

  public void setInstruments(List<String> instruments) {
    setInstrument(instruments.get(0));
    this.instruments = instruments;
  }

  private String instrument;

  public String getInstrument() {
    return instrument;
  }

  public void setInstrument(String instrument) {
    this.instrument = instrument;
  }

  /** */
}

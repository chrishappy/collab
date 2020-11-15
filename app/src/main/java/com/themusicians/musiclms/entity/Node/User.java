package com.themusicians.musiclms.entity.Node;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

/**
 * @file Assignment.java
 *
 * @contributor Jerome Lau
 * @author Nathan Tsai
 * @since Nov 2, 2020
 *     <p>--------------------------------
 * @todo Create Edit Form
 * @todo Create UI
 */
public class User extends Node {
  /**
   * Set the type of Node
   */
  protected String type = "user";

  /**
   * Decide on how to store the tech experience
   */
  protected String techExperience;
  protected List<String> techExperienceList;

  /**
   * The user's email
   * @todo implement later
   */
  protected String email;

  /**
   * Whether the user is a teacher, student
   * @todo add parent
   */
  protected String role;

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

  public User( String id ) {
    super( id );
  }

  /**
   * Implement getBaseTable()
   * @return the database table to store the entity
   */
  @Override
  public String getBaseTable() {
    return getEntityType() + "__" + getType();
  }

  /**
   * Implement getType()
   * @return the type of entity
   */
  @Override
  public String getType() {
    return type;
  }
  /**
   *
   */
  public String getTechExperience() {
    return techExperience;
  }

  public void setTechExperience(String techExperience) {
    this.techExperience = techExperience;
  }

  public List<String> getTechExperienceList() {
    return techExperienceList;
  }

  public void setTechExperienceList(List<String> techExperienceList) {
    this.techExperienceList = techExperienceList;
  }
}

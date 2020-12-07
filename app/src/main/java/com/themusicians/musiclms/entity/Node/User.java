package com.themusicians.musiclms.entity.Node;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.Map;

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

  /** Instruments the user plays or teaches */
  protected List<String> instruments;

  private List<String> addedUsers;

  private String recentText;

  private String viewUser;

  private Map<String, Boolean> relatedAssignments;

  private static final String relatedAssignmentsName = "relatedAssignments";

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

  /**
   * Return the query to the related assignments
   *
   * @return database query
   */
  @Exclude
  public DatabaseReference getRelatedAssignmentDbReference() {
    return getEntityDatabase()
        .child(getId())
        .child(relatedAssignmentsName);
  }

  // For Firebase database
  public Map<String, Boolean> getRelatedAssignments() {
    return relatedAssignments;
  }

  public void setRelatedAssignments(Map<String, Boolean> relatedAssignments) {
    this.relatedAssignments = relatedAssignments;
  }

  /** Fields setters and getters */
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

  public String getViewUser() {
    return viewUser;
  }

  public void setViewUser(String viewUser) {
    this.viewUser = viewUser;
  }

  public String getRecentText() {
    return recentText;
  }

  public void setRecentText(String recentText) {
    this.recentText = recentText;
  }

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

  public List<String> getAddedUsers() {
    return addedUsers;
  }

  public void setAddedUsers(List<String> addedUsers) {
    this.addedUsers = addedUsers;
  }
}

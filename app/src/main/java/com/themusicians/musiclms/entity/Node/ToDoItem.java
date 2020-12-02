package com.themusicians.musiclms.entity.Node;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Assignment.java
 *
 * @contributor
 * @author Nathan Tsai
 * @since Nov 13, 2020
 */
@IgnoreExtraProperties
public class ToDoItem extends Node {

  protected final String type = "to_do_item";

  /**
   * The fields for the To Do Item
   *
   * <p>Public properties will be automatically saved by Firebase Private will not
   */
  protected boolean requireRecording;

  protected boolean completeToDo;

  protected String recordingYoutubeId;

  protected List<String> recordingFeedback;

  protected long toDoState;

  protected String attachedAssignment;

  protected Object TimeCompleted;

  /** The default constructor for Firebase + loadMultiple */
  public ToDoItem() {
    super();
  }

  public ToDoItem(String id) {
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

  /** Settings and Getters */
  public boolean getRequireRecording() {
    return requireRecording;
  }

  public void setRequireRecording(boolean requireRecording) {
    this.requireRecording = requireRecording;
  }

  public String getRecordingYoutubeId() {
    return recordingYoutubeId;
  }

  public void setRecordingYoutubeId(String recordingYoutubeId) {
    this.recordingYoutubeId = recordingYoutubeId;
  }

  public List<String> getRecordingFeedback() {
    return recordingFeedback;
  }

  public void addRecordingFeedback(String recordingFeedbackString) {
    if (this.recordingFeedback == null) {
      recordingFeedback = new ArrayList<>();
    }

    recordingFeedback.add(recordingFeedbackString);
    Collections.sort(recordingFeedback);
  }

  public void setRecordingFeedback(List<String> recordingFeedback) {
    this.recordingFeedback = recordingFeedback;
  }

  public long getToDoState() {
    return toDoState;
  }

  public void setToDoState(long toDoState) {
    this.toDoState = toDoState;
  }

  public String getAttachedAssignment() {
    return attachedAssignment;
  }

  public void setAttachedAssignment(String attachedAssignments) {
    this.attachedAssignment = attachedAssignments;
  }

  public Object getTimeCompleted() {
    return TimeCompleted;
  }

  public void setTimeCompleted(Object timeCompleted) {
    TimeCompleted = timeCompleted;
  }

  @Override
  public String getType() {
    return type;
  }

  public boolean getcompleteToDo() {
    return completeToDo;
  }

  public void setcompleteToDo(boolean completeToDo) {
    this.completeToDo = completeToDo;
  }

}

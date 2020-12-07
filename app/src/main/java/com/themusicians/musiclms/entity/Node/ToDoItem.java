package com.themusicians.musiclms.entity.Node;

import android.util.Log;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements the To Do Item entity class
 *
 * @author Nathan Tsai
 * @since Nov 13, 2020
 */
@IgnoreExtraProperties
public class ToDoItem extends Node {

  protected final String type = "to_do_item";

  /**
   * The fields for the To Do Item
   *
   * <p>Public properties will be automatically saved by Firebase
   */
  protected boolean requireRecording;

  protected boolean completeToDo;

  protected String recordingYoutubeId;

  protected List<String> recordingFeedback;

  //  protected long toDoState;

  protected String attachedAssignment;

  private Assignment assignment;

  protected Object timeCompleted;

  /** For saving recordings */
  public static final String feedbackFormat = "%02d:%02d | %s";

  /**
   * For processing youtube videos
   *
   * <p>Source: https://stackoverflow.com/a/31940028 Author: Jakki @
   * https://stackoverflow.com/u/2605766
   */
  private static final String youtubeUrlRegexPattern =
      ".*(?:youtu.be/|v/|u/\\w/|embed/|e/|watch\\?v=)([^#&?\\s]{11}).*";

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

  /** After save, add to do item to assignment */
  @Override
  public void postSave() {
    if (getAttachedAssignment() != null) {
      updateAttachedAssignment();
    }
  }

  /** Get the assignment entity to save the to do item */
  @Exclude
  public Assignment getAttachedAssignmentEntity() {
    if (assignment == null) {
      assignment = new Assignment(getAttachedAssignment());
    }
    Log.w("testing", "Assignment id is: " + assignment.getId());
    return assignment;
  }

  /** Add or update the to do item on the attached assignment */
  public void updateAttachedAssignment() {
    getAttachedAssignmentEntity()
        .getToDoItemsKeyQuery()
        .child(this.getId())
        .setValue(getCompleteToDo());
  }

  /**
   * Settings and Getters
   *
   * <p>Required for Firebase
   */

  /** The Youtube recording related fields */
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

  /** The recording feedback fields */
  public List<String> getRecordingFeedback() {
    if (recordingFeedback == null) {
      recordingFeedback = new ArrayList<>();
    }

    return recordingFeedback;
  }

  public void addRecordingFeedback(String feedback, int timeInSeconds) {
    int minutes = timeInSeconds / 60;
    int seconds = timeInSeconds % 60;

    addRecordingFeedback(String.format(Locale.CANADA, feedbackFormat, minutes, seconds, feedback));
  }

  public void addRecordingFeedback(String recordingFeedbackString) {
    getRecordingFeedback().add(recordingFeedbackString);
    Collections.sort(recordingFeedback);
  }

  public void setRecordingFeedback(List<String> recordingFeedback) {
    this.recordingFeedback = recordingFeedback;
  }

  //  public long getToDoState() {
  //    return toDoState;
  //  }
  //
  //  public void setToDoState(long toDoState) {
  //    this.toDoState = toDoState;
  //  }

  public String getAttachedAssignment() {
    return attachedAssignment;
  }

  public void setAttachedAssignment(String attachedAssignments) {
    this.attachedAssignment = attachedAssignments;
  }

  public Object getTimeCompleted() {
    return timeCompleted;
  }

  public void setTimeCompleted(Object timeCompleted) {
    this.timeCompleted = timeCompleted;
  }

  @Override
  public String getType() {
    return type;
  }

  public boolean getCompleteToDo() {
    return completeToDo;
  }

  public void setCompleteToDo(boolean completeToDo) {
    this.completeToDo = completeToDo;
  }

  /**
   * Extract a youtube url for its id
   *
   * <p>Source: https://stackoverflow.com/a/31940028 Author: Jakki @
   * https://stackoverflow.com/u/2605766
   *
   * @param youtubeUrl the url of the youtube video
   * @return the youtube id
   */
  public static String getYoutubeIdFromUrl(String youtubeUrl) {
    Pattern compiledPattern = Pattern.compile(youtubeUrlRegexPattern);
    Matcher matcher =
        compiledPattern.matcher(
            youtubeUrl); // url is youtube url for which you want to extract the id.
    if (matcher.find() && matcher.group(1) != null) {
      return matcher.group(1);
    }
    return null;
  }
}

package com.themusicians.musiclms.entity.Node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Assignment Tester.
 *
 * @author Nathan Tsai
 * @since Nov 13, 2020
 */

public class AssignmentUnitTest {

  protected Assignment assignment;;

  protected String assignmentName = "Assignment Name";
  protected String otherAssignmentName = "Other Assignment Name";
  protected List<String> dummyList;

  @Before
  public void setUp() throws Exception {
    // Set Lists Fields
    dummyList = new LinkedList<>();
    dummyList.add("fewsfj32");
    dummyList.add("wfef");

    // Create an assignment
    assignment = new Assignment();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void getBaseTable() {
    assertEquals(assignment.getBaseTable(), "node__assignment");

    assertEquals(assignment.getEntityType() + "__" + assignment.getType(), "node__assignment");
  }

  @Test
  public void getAssignees() {
  }

  @Test
  public void setAssignees() {
  }

  @Test
  public void getClassId() {
  }

  @Test
  public void setClassId() {
  }

  @Test
  public void getDueDate() {
  }

  @Test
  public void setDueDate() {
  }

  @Test
  public void getAllowedAttachments() {
  }
}
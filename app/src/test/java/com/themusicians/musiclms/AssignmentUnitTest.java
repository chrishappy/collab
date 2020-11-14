package com.themusicians.musiclms;

import static org.junit.Assert.*;

import com.themusicians.musiclms.entity.Node.Assignment;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Assignment Tester.
 *
 * @author Nathan Tsai
 * @since Nov 7, 2020
 */
public class AssignmentUnitTest {

  protected Assignment assignment;
  protected Assignment loadAssignment;
  protected Map<String, Object> fieldMap;

  protected String assignmentName = "Assignment Name";
  protected String otherAssignmentName = "Other Assignment Name";
  protected List<String> dummyList;

  @Before
  public void before() throws Exception {
    // Set Lists Fields
    dummyList = new LinkedList<>();
    dummyList.add("fewsfj32");
    dummyList.add("wfef");

    // Create an assignment
    assignment = new Assignment();
  }

  @After
  public void after() throws Exception {}
}

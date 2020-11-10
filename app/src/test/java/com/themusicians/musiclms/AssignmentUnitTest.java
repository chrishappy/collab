package com.themusicians.musiclms;

import com.themusicians.musiclms.entity.Node.Assignment;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


/** 
* Assignment Tester. 
* 
* @author <Nathan Tsai>
* @since <pre>Nov 7, 2020</pre>
*/ 
public class AssignmentUnitTest {

  protected Assignment assignment;
  protected Assignment loadAssignment;
  protected Map<String, Object> fieldMap;

  protected String assignmentName = "Assignment Name";
  private String otherAssignmentName = "Other Assignment Name";

  @Before
  public void before() throws Exception {
    // Map the fields
    fieldMap = new HashMap<>();
    fieldMap.put("name", assignmentName);
    fieldMap.put("classId", -1);
    fieldMap.put("dueDate", 123);

    // Set Lists Fields
    List<String> dummyList =  new LinkedList<>();
    dummyList.add("fewsfj32");
    dummyList.add("wfef");
    fieldMap.put("assignees", dummyList);
    dummyList.add("3-09ger");
    fieldMap.put("attachmentIds", dummyList);

    // Create an assignment
    assignment = new Assignment();
  }

  @After
  public void after() throws Exception {
  }

  /**
  *
  * Method: getLabel()
  *
  */
  @Test
  public void testGetLabel() throws Exception {
    assignment.setField("name", assignmentName);
    String testedAssignmentName = assignment.getLabel();
    assertEquals( testedAssignmentName,  assignmentName );

  }

  /**
   *
   * Method: save()
   *
   */
  @Test
  public void testSetField__onName() throws Exception {
    assignment.setField("name", otherAssignmentName);
    assertEquals(assignment.name, otherAssignmentName);
  }

  /**
   *
   * Method: save()
   *
   */
  @Test
  public void testSetField__onDueDate() throws Exception {
    Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("PDT"));
    final Calendar cldr = cal.getInstance();
    long dueDateTimestamp;
    dueDateTimestamp = TimeUnit.MILLISECONDS.toSeconds( cldr.getTimeInMillis() );

    assignment.setField("dueDate", dueDateTimestamp);
    assertEquals(assignment.dueDate, dueDateTimestamp);
  }


  /**
   *
   * Method: testSetFields()
   *
   */
  @Test
  public void testSetFields() throws Exception {
    assertNotNull( assignment.setFields( fieldMap ) );
  /*
  try {
     Method method = Assignment.getClass().getMethod("writeAssignment");
     method.setAccessible(true);
     method.invoke(<Object>, <Parameters>);
  } catch(NoSuchMethodException e) {
  } catch(IllegalAccessException e) {
  } catch(InvocationTargetException e) {
  }
  */
  }


  /**
  *
  * Method: writeAssignment()
  *
  */
  @Test
  public void testWriteAssignment() throws Exception {
  //TODO: Test goes here...
  /*
  try {
     Method method = Assignment.getClass().getMethod("writeAssignment");
     method.setAccessible(true);
     method.invoke(<Object>, <Parameters>);
  } catch(NoSuchMethodException e) {
  } catch(IllegalAccessException e) {
  } catch(InvocationTargetException e) {
  }
  */
  }

} 

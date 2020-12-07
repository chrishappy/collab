package com.themusicians.musiclms.entity.Node;

import static org.junit.Assert.*;

import java.util.Map;
import org.junit.Test;

/**
 * Test To Do Item entity class
 *
 * @author Nathan Tsai
 * @since Dec 7, 2020
 */
public class ToDoItemTest {

  @Test
  public void getYoutubeIdFromUrl() {
    Map<String, String> youtubeUrlsToTest =
        Map.of(
            "http://www.youtube.com/watch?v=dQw4w9WgXcQ&a=GxdCwVVULXctT2lYDEPllDR0LRTutYfW",
            "dQw4w9WgXcQ",
            "http://www.youtube.com/watch?v=dQw5w9WgXcQ",
            "dQw5w9WgXcQ",
            " http://www.youtube.com/embed/dQw4w9WgXcQ",
            "dQw4w9WgXcQ",
            " http://www.youtube.com/v/dQw4w9WgXcQ  ",
            "dQw4w9WgXcQ",
            "http://youtu.be/dQw4w9WgXcQ ",
            "dQw4w9WgXcQ",
            "https://youtu.be/dQw4w9WgXcQ ",
            "dQw4w9WgXcQ",
            "http://www.youtu.be/dQw4w9WgXcQ ",
            "dQw4w9WgXcQ",
            " http://www.youtube.com/e/dQw4w9WgXcQ",
            "dQw4w9WgXcQ",
            "https://www.youtube.com/e/dQw4w9WgXcQ",
            "dQw4w9WgXcQ",
            "  http://www.youtube.com/watch?v=dQw4w9WgXcQ",
            "dQw4w9WgXcQ");

    for (String youTubeUrl : youtubeUrlsToTest.keySet()) {
      assertEquals(ToDoItem.getYoutubeIdFromUrl(youTubeUrl), youtubeUrlsToTest.get(youTubeUrl));
    }
  }
}

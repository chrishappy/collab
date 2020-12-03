package com.themusicians.musiclms.nodeViews;

import android.content.Intent;
import android.os.Bundle;

import com.themusicians.musiclms.nodeForms.NodeActivity;

/**
 * The class to be extended by node create forms
 *
 * @author Nathan Tsai
 * @since Nov 17, 2020
 */
public abstract class NodeViewActivity extends NodeActivity {

  /** Used to restore entity id after instance is saved See: https://stackoverflow.com/q/26359130 */
  static final String SAVED_ENTITY_ID = "SAVED_ENTITY_ID";

  /** The entity id to edit */
  protected String viewEntityId;

  /** Log tag for loading the assignment */
  public static final String LOAD_ENTITY_DATABASE_TAG = "Load Assignment To Edit";

  /**
   * Load the entity Id
   *
   * @param savedInstanceState when the activity deallocates when opening an new activity this
   *     stores the previous state
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Get id to edit
    Intent intent = getIntent();
    viewEntityId = intent.getStringExtra(ACCEPT_ENTITY_ID);
  }
}

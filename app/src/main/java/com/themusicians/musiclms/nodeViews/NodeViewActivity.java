package com.themusicians.musiclms.nodeViews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.themusicians.musiclms.R;
import com.themusicians.musiclms.nodeForms.NodeActivity;

import java.util.Objects;

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

  /**
   * Hide delete button if not the author of the node
   */
  @Override
  public boolean onPrepareOptionsMenu(Menu menu){
    super.onPrepareOptionsMenu(menu);

    boolean isVisible = (Objects.equals(getNodeForAttachments().getUid(), currentUser.getUid()));
    try {
      menu.findItem(R.id.action_assignment_delete).setVisible(isVisible);
    }
    catch(Exception e) {
      Log.e("PrepareOptionsMenu", "onPrepareOptionsMenu error");
    }
    return true;
  }
}

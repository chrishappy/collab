package com.themusicians.musiclms.nodeForms;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;

import com.themusicians.musiclms.R;

import java.util.LinkedList;
import java.util.List;

// import com.themusicians.musiclms.attachmentDialogs.AddFileDialogFragment;

/**
 * The class to be extended by node create forms
 *
 * @author Nathan Tsai
 * @since Nov 17, 2020
 */
public abstract class NodeCreateFormActivity extends NodeActivity {

  /** Used to restore entity id after instance is saved See: https://stackoverflow.com/q/26359130 */
  static final String SAVED_ENTITY_ID = "SAVED_ENTITY_ID";

  /** The entity id to edit */
  protected String editEntityId;

  /** Whether we are editing a node */
  protected boolean inEditMode = false;

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
    editEntityId = intent.getStringExtra(ACCEPT_ENTITY_ID);
    if (editEntityId != null) {
      inEditMode = true;
    }
  }

  /**
   * Hide delete button if not the author of the node
   */
  @Override
  public boolean onPrepareOptionsMenu(Menu menu){
    super.onPrepareOptionsMenu(menu);

    boolean isVisible = (getNodeForAttachments().getId() != null);
    try {
      menu.findItem(R.id.action_assignment_delete).setVisible(isVisible);
    }
    catch(Exception e) {
      Log.e("PrepareOptionsMenu", "onPrepareOptionsMenu error");
    }

    return true;
  }

  /**
   * List of Edit Text that can not be empty before saving
   */
  private final List<EditText> requiredEditTexts = new LinkedList<>();

  /**
   * Validate the form before save
   */
  public boolean validateForm() {
    boolean result = true;

    if (!requiredEditTexts.isEmpty()) {
      for (EditText editText : requiredEditTexts) {
        if (editText.getText() == null || TextUtils.isEmpty(editText.getText().toString())) {
          String hint = editText.getHint() == null ? "" : editText.getHint().toString();
          editText.setError(String.format(getString(R.string.node_create__empty_field_error), hint));
          result = false;
        }
      }
    }

    return result;
  }

  public void addToRequired(EditText editTextThatMustBeFilled) {
    requiredEditTexts.add(editTextThatMustBeFilled);
  }
}

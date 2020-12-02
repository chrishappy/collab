package com.themusicians.musiclms.nodeForms;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.themusicians.musiclms.entity.Node.Node;

// import com.themusicians.musiclms.attachmentDialogs.AddFileDialogFragment;

/**
 * The class to be extended by node create forms
 *
 * @author Nathan Tsai
 * @since Nov 17, 2020
 */
public abstract class NodeCreateFormActivity extends AppCompatActivity {

  /** The Firebase Auth Instance */
  protected FirebaseUser currentUser;

  /** The request code for retrieving to do items */
  public static final String ACCEPT_ENTITY_ID = "ENTITY_ID_FOR_EDIT";

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

  /** Load the current user on start */
  @Override
  public void onStart() {
    super.onStart();
    // Initalize the current user
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
  }

  /** Return the node we are editing */
  public abstract Node getNode();
}

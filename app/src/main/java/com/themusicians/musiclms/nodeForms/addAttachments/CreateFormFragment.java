package com.themusicians.musiclms.nodeForms.addAttachments;

import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class CreateFormFragment extends Fragment {

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
  public static final String LOAD_ENTITY_DATABASE_TAG = "Load Attachment To Edit";

  /** Load the current user on start */
  @Override
  public void onStart() {
    super.onStart();

    currentUser = FirebaseAuth.getInstance().getCurrentUser();
  }
}

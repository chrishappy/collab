package com.themusicians.musiclms.nodeForms;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.themusicians.musiclms.entity.Node.Node;
import com.themusicians.musiclms.nodeForms.addAttachments.ShowAllAttachmentsFragment;

// import com.themusicians.musiclms.attachmentDialogs.AddFileDialogFragment;

/**
 * The class to be extended by node create forms
 *
 * @author Nathan Tsai
 * @since Nov 17, 2020
 */
public abstract class NodeActivity extends AppCompatActivity {

  /** The Firebase Auth Instance */
  protected FirebaseUser currentUser;

  /** The request code for retrieving to do items */
  public static final String ACCEPT_ENTITY_ID = "ENTITY_ID_FOR_EDIT";

  /** Load the current user on start */
  @Override
  public void onStart() {
    super.onStart();
    // Initialize the current user
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
  }

  /** Return the node we are editing */
  public abstract Node getNodeForAttachments();

  /**
   * Populate the showAttachment fragment
   */
  protected void initShowAttachments(int layoutId, String tagAddition) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment content = fragmentManager.findFragmentById(layoutId);
    if (!(content instanceof ShowAllAttachmentsFragment)) {
      Log.w("debugMissingNode", "it's here! missing id: " + getNodeForAttachments().getId());

      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.add(
          layoutId,
          ShowAllAttachmentsFragment.newInstance(getNodeForAttachments()),
          "ShowAllAttachmentsFragment " + tagAddition);
      fragmentTransaction.commitAllowingStateLoss();
    }
    else {
      Log.w("debugMissingNode", "the missing id: " + getNodeForAttachments().getId());
    }
  }


}

package com.themusicians.musiclms.nodeForms;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.themusicians.musiclms.R;
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

  /** Populate the showAttachment fragment */
  protected void initShowAttachments(int layoutId, String tagAddition) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment content = fragmentManager.findFragmentById(layoutId);
    if (!(content instanceof ShowAllAttachmentsFragment)) {
      Log.w("debugMissingNode", "it's here! missing id: " + getNodeForAttachments().getId());

      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.add(
          layoutId,
          new ShowAllAttachmentsFragment(), // .newInstance(getNodeForAttachments()),
          "ShowAllAttachmentsFragment " + tagAddition);
      fragmentTransaction.commitAllowingStateLoss();
    } else {
      Log.w("debugMissingNode", "the missing id: " + getNodeForAttachments().getId());
    }
  }

  /**
   * Ask to Delete
   *
   * <p>Source: https://stackoverflow.com/a/11740348
   */
  protected AlertDialog askToDeleteAttachmentNode() {
    return new AlertDialog.Builder(this)
        // set message, title, and icon
        .setTitle(R.string.delete)
        .setMessage(
            String.format(
                getString(R.string.delete_confirm_message), getNodeForAttachments().getName()))
        .setIcon(R.drawable.ic_baseline_delete_24__grey)
        .setPositiveButton(
            R.string.Delete,
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int whichButton) {
                getNodeForAttachments().delete();
                dialog.dismiss();
                finish();
              }
            })
        .setNegativeButton(
            R.string.cancel,
            (dialog, which) -> {
              dialog.dismiss();
            })
        .create();
  }

  /** Add delete button */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_delete_activity_options, menu);
    return true;
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.action_assignment_delete) {
      askToDeleteAttachmentNode().show();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}

package com.themusicians.musiclms.attachmentDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.themusicians.musiclms.R;

/**
 * A simple {@link DialogFragment} subclass
 */
public abstract class AddAttachmentDialogFragment extends DialogFragment {

  /**
   * To be implemented in activities related
   */
  public interface AddAttachmentDialogListener {
    void onDialogPositiveClick(DialogFragment dialog);
    void onDialogNegativeClick(DialogFragment dialog);
  }
  // Use this instance of the interface to deliver action events
  protected AddAttachmentDialogListener listener;

  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  protected static final String ENTITY_ID = "entityId";

  /** The entity to attach the comment to */
  protected String entityToUpdateId;

  public AddAttachmentDialogFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      entityToUpdateId = getArguments().getString(ENTITY_ID);
    }
  }

  @Override
  abstract public Dialog onCreateDialog(Bundle savedInstanceState);

  /**
   * Override the Fragment.onAttach() method to instantiate the AddAttachmentDialogListener
   */
  @Override
  public void onAttach(Context context) throws ClassCastException {
    super.onAttach(context);
    // Verify that the host activity implements the callback interface
    try {
      // Instantiate the NoticeDialogListener so we can send events to the host
      listener = (AddAttachmentDialogListener) context;
    } catch (ClassCastException e){
      // The activity doesn't implement the interface, throw exception
      // @todo Replace with actual activity name
      throw new ClassCastException("activity.toString()"
          + " must implement NoticeDialogListener");
    }
  }


}
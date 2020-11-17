package com.themusicians.musiclms.attachmentDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import com.themusicians.musiclms.R;

/**
 * A simple {@link AddAttachmentDialogFragment} subclass. Use the {@link
 * AddCommentDialogFragment#newInstance} factory method to create an instance of this fragment.
 */
public class AddCommentDialogFragment extends AddAttachmentDialogFragment {

  public AddCommentDialogFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of this fragment using the provided
   * parameters.
   *
   * @param in_entityToUpdateId The entity the comment will be added to
   * @return A new instance of fragment DialogAddCommentFragment.
   */
  public static AddCommentDialogFragment newInstance(String in_entityToUpdateId) {
    AddCommentDialogFragment fragment = new AddCommentDialogFragment();
    Bundle args = new Bundle();
    args.putString(ENTITY_ID, in_entityToUpdateId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState); // Entity id is set
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    // Get the layout inflater
    LayoutInflater inflater = requireActivity().getLayoutInflater();

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    builder
        .setView(inflater.inflate(R.layout.fragment_add_comment_dialog, null))
        // Add action buttons
        .setPositiveButton(
            R.string.ok,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int id) {
                listener.onDialogPositiveClick(AddCommentDialogFragment.this);
              }
            })
        .setNegativeButton(
            R.string.cancel,
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                listener.onDialogNegativeClick(AddCommentDialogFragment.this);
              }
            });
    return builder.create();
  }
}

package com.themusicians.musiclms.nodeForms.addAttachments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Attachment.AllAttachment;

public class createFormAttachmentsFragment extends createFormFragment {

  private AddAttachmentsViewModel homeViewModel;

  /** The attachment to be edited or saved */
  protected AllAttachment attachment;


  /** Receive the entity id of the attachment to edit */
  public static createFormAttachmentsFragment newInstance(String editEntityId) {
    createFormAttachmentsFragment fragment = new createFormAttachmentsFragment();
    Bundle args = new Bundle();
    args.putString(ACCEPT_ENTITY_ID, editEntityId);
    fragment.setArguments(args);
    return fragment;
  }

  /** Set the user object + fill in the edit information */
  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    attachment = new AllAttachment();

//    homeViewModel = new ViewModelProvider(this).get(addAttachmentsViewModel.class);

    View root = inflater.inflate(R.layout.fragment_add_attachments, container, false);

    // The comment fields
    final EditText editComment = root.findViewById(R.id.editComment);


    // The file fields

    //        final TextView textView = root.findViewById(R.id.text_home);
    //        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
    //            @Override
    //            public void onChanged(@Nullable String s) {
    //                textView.setText(s);
    //            }
    //        });

    // Save the data
    final Button saveAction = root.findViewById(R.id.saveAction);
    saveAction.setOnClickListener(
        view -> {

          attachment.setComment( editComment.getText().toString() );
          attachment.setStatus( true );
          attachment.setUid( currentUser.getUid() );
          attachment.save();

          //Display notification
          String saveMessage = (editEntityId != null) ? "Attachment updated" : "Attachment Saved";
          Snackbar.make(view, saveMessage, Snackbar.LENGTH_LONG)
              .setAction("Action", null)
              .show();
        });

    final Button cancelAction = root.findViewById(R.id.cancelAction);
    cancelAction.setOnClickListener(
        view -> {
          //Display notification
          String saveMessage = (editEntityId != null) ? "Attachment update cancelled" : "Attachment save cancelled";
          Snackbar.make(view, saveMessage, Snackbar.LENGTH_LONG)
              .setAction("Action", null)
              .show();
        });

    return root;
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    attachment.save();

    // Save the user's current game state
    savedInstanceState.putString(SAVED_ENTITY_ID, attachment.getId());

    // Always call the superclass so it can save the view hierarchy state
    super.onSaveInstanceState(savedInstanceState);
  }
}

package com.themusicians.musiclms.nodeForms.addAttachments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Attachment.AllAttachment;
import com.themusicians.musiclms.entity.Attachment.Attachment;

/**
 * The adapter for the All Attachments pages
 *
 * @author Nathan Tsai
 * @since Nov 19, 2020
 */

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class ShowAllAttachmentsAdapter
    extends FirebaseRecyclerAdapter<AllAttachment, ShowAllAttachmentsAdapter.AllAttachmentViewHolder> {

  private ItemClickListener itemClickListener;

  public final static String editAllAttachments = "editAllAttachments";
  public final static String SHOW_PDF = "SHOW_PDF";

  public ShowAllAttachmentsAdapter(@NonNull FirebaseRecyclerOptions<AllAttachment> options) {
    super(options);
  }

  @Override
  protected void onBindViewHolder(
      @NonNull AllAttachmentViewHolder holder, int position, @NonNull AllAttachment allAttachment) {

    assert holder.comment != null;

    if (allAttachment.getComment() != null) {
      holder.comment.setText(allAttachment.getComment());
    }

    if (allAttachment.getFileUploadUri() != null) {
      holder.fileDownload.setVisibility(View.VISIBLE);
      holder.fileDownload.setOnClickListener(
          view -> {
            itemClickListener.onEditButtonClick(SHOW_PDF, allAttachment.getFileUploadUri(), null);
          }
      );
    }

    holder.editButton.setOnClickListener(
        view -> {
          if (itemClickListener != null) {
            itemClickListener.onEditButtonClick(editAllAttachments, allAttachment.getId(), holder.allAttachmentWrapper);
          }
        });

    /*
     * This section will be added to all Nodes. Please use variables to allow us
     * to quickly move these functions into a separate class
     *
     * @todo Save Comment into database
     * @todo Create "Add File Button" -> use the same functions
     */

    /*
    // Add a Comment
    final Button addCommentButton = findViewById(R.id.addCommentButton);
    addCommentButton.setOnClickListener(
        view -> {
          String dialogTag = "addComment";
          DialogFragment newAddCommentDialog = new AddCommentDialogFragment();
          newAddCommentDialog.show(getSupportFragmentManager(), dialogTag);
        });
    */
  }

  // Function to tell the class about the Card view (here
  // "Assignment.xml")in
  // which the data will be shown
  @NonNull
  @Override
  public AllAttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.viewholder_show_attachment, parent, false);
    return new AllAttachmentViewHolder(view);
  }

  // Sub Class to create references of the views in Crad
  // view (here "person.xml")
  static class AllAttachmentViewHolder extends RecyclerView.ViewHolder {
    TextView comment;
    Button fileDownload, editButton;
    ConstraintLayout allAttachmentWrapper;

    public AllAttachmentViewHolder(@NonNull View itemView) {
      super(itemView);

      allAttachmentWrapper = itemView.findViewById(R.id.allAttachmentWrapper);
      comment = itemView.findViewById(R.id.viewComment);
      fileDownload = itemView.findViewById(R.id.attachmentDownloadFile);
      editButton = itemView.findViewById(R.id.edit_button);
    }
  }

  /**
   * Allow users to click the edit button
   *
   * <p>From: https://stackoverflow.com/questions/39551313/
   */
  public interface ItemClickListener {
    void onEditButtonClick(String type, String entityId, View clickedView);
  }

  public void addItemClickListener(ItemClickListener listener) {
    itemClickListener = listener;
  }

  /**
   *

   case 86:
   if (resultCode == RESULT_OK && data != null) {
   pdfUri = data.getData();
   String notificationText = "A file is selected : " + data.getData().getLastPathSegment();
   notification.setText(notificationText);
   } else {
   Toast.makeText(
   AssignmentCreateFormActivity.this, "Please select a file", Toast.LENGTH_SHORT)
   .show();
   }
   break;
   */

}

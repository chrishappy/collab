package com.themusicians.musiclms.nodeForms.addAttachments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Attachment.AllAttachment;

/**
 * The adapter for the All Attachments pages
 *
 * @author Nathan Tsai
 * @since Nov 19, 2020
 */

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class AllAttachmentsAdapter
    extends FirebaseRecyclerAdapter<AllAttachment, AllAttachmentsAdapter.AllAttachmentViewHolder> {

  private ItemClickListener itemClickListener;

  public static String editAllAttachments = "editAllAttachments";

  public AllAttachmentsAdapter(@NonNull FirebaseRecyclerOptions<AllAttachment> options) {
    super(options);
  }

  // Function to bind the view in Card view(here
  // "person.xml") iwth data in
  // allAttachment class(here "person.class")
  @Override
  protected void onBindViewHolder(
      @NonNull AllAttachmentViewHolder holder, int position, @NonNull AllAttachment allAttachment) {

    if (holder.comment != null && allAttachment.getComment() != null) {
      holder.comment.setText(allAttachment.getComment());
    }

    /*
    if (allAttachment.getUid() != null) {
      holder.authorName.setText(String.format("%s...", allAttachment.getUid().substring(0, 20)));
    }

    if (allAttachment.getDueDate() != 0) {
      Date date = new Date(allAttachment.getDueDate());
//      DateFormat dateFormat = new SimpleDateFormat( getText(R.string.date_format__month_day), Locale.CANADA);
      DateFormat dateFormat = new SimpleDateFormat( "MMM d", Locale.CANADA);
      holder.dueDate.setText(dateFormat.format(date));
    }
    */

    holder.editButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (itemClickListener != null) {
              itemClickListener.onEditButtonClick( editAllAttachments, allAttachment.getId() );
            }
          }
        });
  }

  // Function to tell the class about the Card view (here
  // "Assignment.xml")in
  // which the data will be shown
  @NonNull
  @Override
  public AllAttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.viewholder_attachment, parent, false);
    return new AllAttachmentViewHolder(view);
  }

  /**
   * Archive the assignment on swipe
   * @param position
   */
  public void deleteAssignment(int position) {
//    mRecentlyDeletedItem = mListItems.get(position);
//    mRecentlyDeletedItemPosition = position;
//    items.remove(position);
    notifyItemRemoved(position);
  }

  // Sub Class to create references of the views in Crad
  // view (here "person.xml")
  class AllAttachmentViewHolder extends RecyclerView.ViewHolder {
    TextView comment;
    Button editButton;

    public AllAttachmentViewHolder(@NonNull View itemView) {
      super(itemView);

      comment = itemView.findViewById(R.id.viewComment);
      editButton = itemView.findViewById(R.id.edit_button);
    }
  }

  /**
   * Allow users to click the edit button
   *
   * From: https://stackoverflow.com/questions/39551313/
   */
  public interface ItemClickListener {
    void onEditButtonClick(String type, String entityId);
  }

  public void addItemClickListener(ItemClickListener listener) {
    itemClickListener = listener;
  }
}

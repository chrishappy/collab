package com.themusicians.musiclms.nodeViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Assignment;
import com.themusicians.musiclms.entity.Node.User;
import com.themusicians.musiclms.nodeForms.ToDoAssignmentFormAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * The adapter for the Assignment Form pages
 *
 * @author Nathan Tsai
 * @since Nov 16, 2020
 */

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class ToDoRecordingFeedbackAdapter extends RecyclerView.Adapter<ToDoRecordingFeedbackAdapter.ToDoRecordingFeedbackViewHolder> {

  private ItemClickListener itemClickListener;
  private final List<String> items;
  private Context context;

  public ToDoRecordingFeedbackAdapter(Context context, @NonNull List<String> items) {
    this.items = items;
    this.context = context;
  }

  @NonNull
  @Override
  public ToDoRecordingFeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v= LayoutInflater.from(getContext()).inflate(R.layout.item_recording_feedback, parent,false);
    return new ToDoRecordingFeedbackViewHolder(v);
  }

  @Override
  public void onBindViewHolder(ToDoRecordingFeedbackViewHolder holder, int position) {
    // Get the data item for this position
    String feedback = items.get(position);
    String[] feedbackParts = feedback.split(" [|] ", 2);
    String[] timeParts = feedbackParts[0].split("[:]", 2);
    String feedbackPart = "";

    if (timeParts.length != 2 || feedbackParts.length != 2) {
      timeParts = new String[]{"xx", "xx"};
      feedbackPart = getContext().getString(R.string.to_do_view__error_feedback);
    }
    else {
      feedbackPart = feedbackParts[1];
    }

    // Populate the data into the template view using the data object
    holder.feedbackTime.setText(String.format("%s:%s", timeParts[0], timeParts[1]));
    holder.feedbackText.setText(feedbackPart);

    // On item click
    int timeToSkip = (Integer.decode(timeParts[0]) * 60) + Integer.decode(timeParts[1]);

    holder.feedbackLayout.setOnClickListener(view -> {
      itemClickListener.onToDoRecordingFeedbackClick("feedbackTimeClicked", timeToSkip, position);
    });
  }

  private Context getContext() {
    return context;
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class ToDoRecordingFeedbackViewHolder extends RecyclerView.ViewHolder {
    TextView feedbackTime, feedbackText;
    LinearLayout feedbackLayout;

    public ToDoRecordingFeedbackViewHolder(@NonNull View itemView) {
      super(itemView);

      feedbackTime = itemView.findViewById(R.id.recording_feedback__time);
      feedbackText = itemView.findViewById(R.id.recording_feedback__text);
      feedbackLayout = itemView.findViewById(R.id.recording_feedback);
    }
  }

  /**
   * Allow users to click the edit button
   *
   * <p>From: https://stackoverflow.com/questions/39551313/
   */
  public interface ItemClickListener {
    void onToDoRecordingFeedbackClick(String type, int time, int position);
  }

  public void addItemClickListener(ItemClickListener listener) {
    itemClickListener = listener;
  }
}

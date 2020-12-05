package com.themusicians.musiclms.nodeViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
public class ToDoRecordingFeedbackAdapter extends ArrayAdapter<String> {

  private ItemClickListener itemClickListener;

  public ToDoRecordingFeedbackAdapter(Context context, @NonNull List<String> items) {
    super(context, 0, items);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Get the data item for this position
    String feedback = getItem(position);
    String[] feedbackParts = feedback.split("|", 2);
    String[] timeParts;
    if (feedbackParts.length == 2) {
      timeParts = feedbackParts[1].split(":", 2);
    }
    else {
      timeParts = new String[]{"00", "00"};
    }

    // Check if an existing view is being reused, otherwise inflate the view
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_recording_feedback, parent, false);
    }

    // Lookup view for data population
    TextView feedbackTime = convertView.findViewById(R.id.recording_feedback__time);
    TextView feedbackText = convertView.findViewById(R.id.recording_feedback__text);

    // Populate the data into the template view using the data object
    feedbackTime.setText(String.format("%s:%s", timeParts[0], timeParts[1]));
    feedbackTime.setOnClickListener(view -> {
      itemClickListener.onToDoRecordingFeedbackClick("feedbackTimeclicked", Integer.decode(feedbackParts[2]), position);
    });
    feedbackText.setText(feedbackParts[3]);

    // Return the completed view to render on screen
    return convertView;
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

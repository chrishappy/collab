package com.themusicians.musiclms.nodeViews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Node;
import com.themusicians.musiclms.entity.Node.ToDoItem;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used to create and update assignments node entities
 *
 * @author Nathan Tsai
 * @since Nov 24, 2020
 */
public class ToDoViewActivity extends NodeViewActivity implements ToDoRecordingFeedbackAdapter.ItemClickListener {
  /** The Firebase Auth Instance */
  private FirebaseUser currentUser;

  /** Fields */
  private TextView toDoItemName;
  private CheckBox toDoCheck;
  private VideoView recordingVideo;

  /** For video recording */
  static final int REQUEST_VIDEO_CAPTURE = 1;

  /** The To Do Item object */
  ToDoItem toDoItem;

  /**
   * Youtube Player
   */
  private static final int RECOVERY_REQUEST = 1;
  private LinearLayout youtubePlayerAndFeedbackLayout;
  private Button changeYoutubeVideo;
  private YouTubePlayerView youTubeView;
  private Button seekToButton;
  private EditText seekToInput;

  /** Recording Feedback */
  private ToDoRecordingFeedbackAdapter recordingFeedbackAdapter;
  private ListView recordingFeedbackListView;
  private EditText feedbackText;
  private Button addFeedback;

  /** Add & Store Recording fields */
  private static final String youtubeUrlRegexPattern = ".*(?:youtu.be/|v/|u/\\w/|embed/|e/|watch\\?v=)([^#&?\\s]{11}).*";
  private LinearLayout youtubeRecordingLayout;
  private EditText addYoutubeUrl;
  private Button saveYoutubeUrl;
  private Uri recordingVideoUri;
  private Button addRecording;
  private boolean alreadyInitAddRecording;

  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    // If we are editing an to do item
    if (viewEntityId != null) {
      toDoItem
          .getEntityDatabase()
          .child(viewEntityId)
          .addListenerForSingleValueEvent(
              new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                  toDoItem = dataSnapshot.getValue(ToDoItem.class);
                  assert toDoItem != null;
                  toDoItemName.setText(toDoItem.getName());

                  // Set checked
                  toDoCheck.setChecked(toDoItem.getCompleteToDo());

                  String youtubeId = toDoItem.getRecordingYoutubeId();

                  if (youtubeId == null) {
                    hideYoutubeVideoAndRecordingFeedback();
                    showAddYoutubeRecording();
                  }
                  else {
                    showYoutubeVideoAndRecordingFeedback();
                    hideAddYoutubeRecording();
                  }

                  Log.w(LOAD_ENTITY_DATABASE_TAG, "loadToDoItem:onDataChange");
                }

                @Override
                public void onCancelled(@NotNull DatabaseError databaseError) {
                  // Getting Post failed, log a message
                  Log.w(
                      LOAD_ENTITY_DATABASE_TAG,
                      "loadToDoItem:onCancelled",
                      databaseError.toException());
                  // ...
                }
              });
    }
    else {
      Toast.makeText(
          ToDoViewActivity.this,
          "There was a problem loading this ToDo",
          Toast.LENGTH_LONG)
          .show();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_to_do_item_view);
    toDoItem = new ToDoItem();

    // Get fields
    toDoItemName = findViewById(R.id.to_do_item_name);

    // To Do Checkbox
    toDoCheck = findViewById(R.id.complete_to_do_itemCB);
    toDoCheck.setOnClickListener(view -> {
      toDoItem.setCompleteToDo(toDoCheck.isChecked());
      toDoItem.save();
    });

    /*
     Record Video
     */
    youtubeRecordingLayout = findViewById(R.id.youtube_recorder_wrapper);
    recordingVideo = findViewById(R.id.tempRecordingVideo);
    addRecording = findViewById(R.id.add_recording_video);
    addYoutubeUrl = findViewById(R.id.addYoutubeUrl);

    addRecording.setOnClickListener(view -> {
      dispatchTakeVideoIntent();
    });


    /*
     Play Video
     */
    youtubePlayerAndFeedbackLayout = findViewById(R.id.youtube_player_and_recording_wrapper);
    changeYoutubeVideo = findViewById(R.id.changeRecording);
    youTubeView = findViewById(R.id.youtube_player_view);
    seekToInput = findViewById(R.id.seek_to_text);
    seekToButton = findViewById(R.id.seek_to_button);

    // Load Youtube (for performance)
    getLifecycle().addObserver(youTubeView);

    changeYoutubeVideo.setOnClickListener(view -> {
      showAddYoutubeRecording();
      hideYoutubeVideoAndRecordingFeedback();
    });

    /*
     Add Feedback for Recording
     */
    recordingFeedbackListView = findViewById(R.id.recordingFeedbackListView);
    feedbackText = findViewById(R.id.add_recording_feedback__text);
    addFeedback = findViewById(R.id.add_recording_feedback__save);

    addFeedback.setOnClickListener(view -> {
      int timeOfFeedback = Integer.decode(seekToInput.getText().toString());
      toDoItem.addRecordingFeedback(timeOfFeedback, feedbackText.getText().toString());
      toDoItem.save();

      if (recordingFeedbackAdapter != null ) {
        // Update recording feedback
        recordingFeedbackAdapter.clear();
        recordingFeedbackAdapter.addAll(toDoItem.getRecordingFeedback());
      }
      else {
        initRecordingFeedbackAdapter();
      }
    });


    // Testing share video
//    Button tempButton1 = findViewById(R.id.tempButton1);
//    tempButton1.setOnClickListener(v -> {
//      String videoPath = getDataColumn(getApplicationContext(), videoUri, null, null);
//      uploadYoutubeVideo(videoPath);
//    });

    // Initialize Attachments
    initShowAttachments();

//    final EditText seekToText = findViewById(R.id.seek_to_text);
//    Button seekToButton = findViewById(R.id.seek_to_button);
//    seekToButton.setOnClickListener(v -> {
//      int skipToSecs = Integer.parseInt(seekToText.getText().toString());
//      player.seekToMillis(skipToSecs * 1000);
//    });
  }

//  @Override
//  public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
//    this.player = player;
//
//    if (!wasRestored) {
//      player.cueVideo("fhWaJi1Hsfo"); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
//    }
//  }
//
//  @Override
//  public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
//    if (errorReason.isUserRecoverableError()) {
//      errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
//    } else {
//      String error = String.format(getString(R.string.player_error), errorReason.toString());
//      Toast.makeText(this, error, Toast.LENGTH_LONG).show();
//    }
//  }

  /** Return the node to add attachments to */
  @Override
  public Node getNodeForAttachments() {
    return toDoItem;
  }

//  protected Provider getYouTubePlayerProvider() {
//    return youTubeView;
//  }

  /**
   * Helper functions for toggle displays of recording player vs add recording
   */
  private void showAddYoutubeRecording() {
    initAddRecording();
    youtubeRecordingLayout.setVisibility(View.VISIBLE);
  }

  private void hideAddYoutubeRecording() {
    youtubeRecordingLayout.setVisibility(View.GONE);
  }

  private void showYoutubeVideoAndRecordingFeedback() {
    youtubePlayerAndFeedbackLayout.setVisibility(View.VISIBLE);

    initYoutubeVideoAndRecordingFeedback(toDoItem.getRecordingYoutubeId());
    initRecordingFeedbackAdapter();
  }

  private void hideYoutubeVideoAndRecordingFeedback() {
    youtubePlayerAndFeedbackLayout.setVisibility(View.GONE);
  }

  /**
   * Initialize add Recording
   */
  private void initAddRecording() {
    if (alreadyInitAddRecording) {  return;  }

    // After user input youtube url
    addYoutubeUrl.addTextChangedListener(new TextWatcher() {
      public void afterTextChanged(Editable youtubeUrlEditable) {
        String youtubeId = getYoutubeIdFromUrl(youtubeUrlEditable.toString());
        if (youtubeId != null) {
          toDoItem.setRecordingYoutubeId(youtubeId);
          toDoItem.save();

          showYoutubeVideoAndRecordingFeedback();
          hideAddYoutubeRecording();

          Toast.makeText(ToDoViewActivity.this, "Youtube Video saved and loading", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(ToDoViewActivity.this, "We could not process the Youtube Url", Toast.LENGTH_LONG).show();
        }
      }

      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }
    });

    alreadyInitAddRecording = true;
  }

  /**
   * Initialize the Recording Feedback adapter
   *
   * Assumes toDoItem.getRecordingFeedback() is not null
   */
  private void initRecordingFeedbackAdapter() {
    if (recordingFeedbackAdapter == null) {
      recordingFeedbackAdapter =
          new ToDoRecordingFeedbackAdapter(this, toDoItem.getRecordingFeedback());
      recordingFeedbackAdapter.addItemClickListener(this);
      recordingFeedbackListView.setAdapter(recordingFeedbackAdapter);
    }
  }

  /**
   * Initialize the youtube video player
   *
   * change addTextChanged Listener to getOnFocusChangeListener
   * https://stackoverflow.com/questions/8666380/android-evaluate-edittext-after-the-user-finishes-editing
   */
  private void initYoutubeVideoAndRecordingFeedback(@NonNull String videoId) {
    // TODO programmatically create the youtube view to improve performance

      youTubeView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
        @Override
        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
          youTubePlayer.loadVideo(videoId, 0);

//          seekToInput.addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable youtubeSeekTime) {
//              int skipToSecs = Integer.parseInt(seekToInput.getText().toString());
//              youTubePlayer.seekTo(skipToSecs);
//
//              Toast.makeText(ToDoViewActivity.this, "Text: Player time changed to: " + skipToSecs, Toast.LENGTH_LONG).show();
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//          });

          seekToButton.setOnClickListener(view -> {
            int skipToSecs = Integer.parseInt(seekToInput.getText().toString());
            youTubePlayer.seekTo(skipToSecs);

            Toast.makeText(ToDoViewActivity.this, "Button: Player time changed to: " + skipToSecs, Toast.LENGTH_LONG).show();
          });
        }
      });
  }

  /**
   * Parse a youtube url for its id
   *
   * Source: https://stackoverflow.com/a/31940028
   * Author: Jakki @ https://stackoverflow.com/u/2605766
   *
   * @param youtubeUrl the url of the youtube video
   * @return the youtube id
   */
  private String getYoutubeIdFromUrl(String youtubeUrl) {
    Pattern compiledPattern = Pattern.compile(youtubeUrlRegexPattern);
    Matcher matcher = compiledPattern.matcher(youtubeUrl); //url is youtube url for which you want to extract the id.
    if (matcher.find() && matcher.group(1) != null) {
      return matcher.group(1);
    }
    return null;
  }

  /**
   * When clicking an item of the feedback
   * @param type the type of click
   * @param time the time to skip youtube recording to
   * @param position the position of the feedback
   */
  @Override
  public void onToDoRecordingFeedbackClick(String type, int time, int position) {
    seekToInput.setText(time);
    seekToButton.performClick();
  }

  /**
   * Record video
   *
   * See: https://developer.android.com/training/camera/videobasics
   * For YT: https://www.sitepoint.com/using-the-youtube-api-to-embed-video-in-an-android-app/
   */
  private void dispatchTakeVideoIntent() {
    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
      startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);

//    if (requestCode == RECOVERY_REQUEST) {
//      // Retry initialization if user performed a recovery action
//      getYouTubePlayerProvider().initialize(YoutubeConfig.YOUTUBE_API_KEY, this);
//    }

    if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
      recordingVideoUri = intent.getData();
      uploadYoutubeVideo(recordingVideoUri);
    }
  }

  /*
   * Upload youtube video
   *
   * @param videoPath the uri of the video
   */
  private void uploadYoutubeVideo(Uri videoUri) {
    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    sharingIntent.setType("video/*");
    sharingIntent.setPackage("com.google.android.youtube");
    sharingIntent.putExtra(Intent.EXTRA_TITLE, "Description");
    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Testing Title 2");
    sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, videoUri);
    startActivity(Intent.createChooser(sharingIntent,"Share To:"));
  }

  /*
   * Attempt to get upload youtube to work
   */
//  public static String getDataColumn(Context context, Uri uri, String selection,
//                                     String[] selectionArgs) {
//    Cursor cursor = null;
//    final String column = MediaStore.MediaColumns.DATA;
//    final String[] projection = {
//        column
//    };
//
//    try {
//      cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
//          null);
//      if (cursor != null && cursor.moveToFirst()) {
//        final int column_index = cursor.getColumnIndexOrThrow(column);
//        return cursor.getString(column_index);
//      }
//    } finally {
//      if (cursor != null)
//        cursor.close();
//    }
//    return null;
//

}

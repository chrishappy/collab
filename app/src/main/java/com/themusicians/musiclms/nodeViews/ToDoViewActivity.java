package com.themusicians.musiclms.nodeViews;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBarListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.ToDoItem;

import org.jetbrains.annotations.NotNull;

/**
 * Used to create and update assignments node entities
 *
 * @author Nathan Tsai
 * @since Nov 24, 2020
 */
public class ToDoViewActivity extends NodeViewActivity {
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
   * Youtube
   */
  private static final int RECOVERY_REQUEST = 1;
  private YouTubePlayerView youTubeView;
  private ListView recordingFeedbackListView;
  private Button seekToButton;
  private EditText seekToText;

  /** Recording Feedback */
  private EditText feedbackText;
  private Button addFeedback;

  /** Add Recording fields */
  private Uri videoUri;
  private Button addRecording;

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
                  toDoItemName.setText(toDoItem.getName());

                  if (toDoItem.getcompleteToDo() == true) {
                      toDoCheck.setChecked(true);
                  }else{
                      toDoCheck.setChecked(false);
                  }

                  String videoId = toDoItem.getRecordingYoutubeId();
                  if (videoId != null) {
                    youTubeView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                      @Override
                      public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(videoId, 0);

                        seekToButton.setOnClickListener(v -> {
                          int skipToSecs = Integer.parseInt(seekToText.getText().toString());
                          youTubePlayer.seekTo(skipToSecs);
                        });

                        ArrayAdapter<String> arrayAdapter =
                            new ArrayAdapter<String>(ToDoViewActivity.this, android.R.layout.simple_list_item_1, toDoItem.getRecordingFeedback());
                        // Set The Adapter
                        recordingFeedbackListView.setAdapter(arrayAdapter);
                      }
                    });
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
    seekToText = findViewById(R.id.seek_to_text);
    seekToButton = findViewById(R.id.seek_to_button);
    recordingFeedbackListView = findViewById(R.id.recordingFeedbackListView);

    // Load Youtube
    youTubeView = findViewById(R.id.youtube_player_view);
    getLifecycle().addObserver(youTubeView);

    // To Do Checkbox
    toDoCheck = findViewById(R.id.complete_to_do_itemCB);
    toDoCheck.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(toDoCheck.isChecked()){
          toDoItem.setcompleteToDo(true);
          toDoItem.save();
        } else{
          toDoItem.setcompleteToDo(false);
          toDoItem.save();
        }
      }
    });

    // Recording feedback
    feedbackText = findViewById(R.id.add_recording_feedback__text);
    addFeedback = findViewById(R.id.add_recording_feedback__save);

    addFeedback.setOnClickListener(view -> {
      toDoItem.addRecordingFeedback(feedbackText.getText().toString());
      toDoItem.save();
    });

    // Add Recording
    recordingVideo = findViewById(R.id.tempRecordingVideo);
    addRecording = findViewById(R.id.add_recording_video);
    addRecording.setOnClickListener(view -> {
      dispatchTakeVideoIntent();
    });

    Button tempButton1 = findViewById(R.id.tempButton1);
    tempButton1.setOnClickListener(v -> {
      String videoPath = getDataColumn(getApplicationContext(), videoUri, null, null);
      uploadYoutubeVideo(videoPath);
    });

    Button tempButton2 = findViewById(R.id.tempButton2);
    tempButton2.setOnClickListener(v -> {
      Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
      sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Testing Title 2");
      sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, videoUri);
      startActivity(Intent.createChooser(sharingIntent,"_share_:"));
    });
    // Set up Feedback




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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);

//    if (requestCode == RECOVERY_REQUEST) {
//      // Retry initialization if user performed a recovery action
//      getYouTubePlayerProvider().initialize(YoutubeConfig.YOUTUBE_API_KEY, this);
//    }

    if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
      videoUri = intent.getData();
      recordingVideo.setVideoURI(videoUri);
    }
  }

//  protected Provider getYouTubePlayerProvider() {
//    return youTubeView;
//  }

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

  /**
   * Upload youtube video
   *
   * @param videoPath the local(?) path of the video
   */
  private void uploadYoutubeVideo(String videoPath) {
    ContentValues content = new ContentValues(4);
    content.put(MediaStore.Video.VideoColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
    content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
    content.put(MediaStore.Video.Media.DATA, videoPath);
    ContentResolver resolver = getBaseContext().getContentResolver();
    Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);

    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Testing Title::");
    sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
    startActivity(Intent.createChooser(sharingIntent,"_share_::"));
  }

  public static String getDataColumn(Context context, Uri uri, String selection,
                                     String[] selectionArgs) {
    Cursor cursor = null;
    final String column = MediaStore.MediaColumns.DATA;
    final String[] projection = {
        column
    };

    try {
      cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
          null);
      if (cursor != null && cursor.moveToFirst()) {
        final int column_index = cursor.getColumnIndexOrThrow(column);
        return cursor.getString(column_index);
      }
    } finally {
      if (cursor != null)
        cursor.close();
    }
    return null;
  }

}

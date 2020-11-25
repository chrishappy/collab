package com.themusicians.musiclms.nodeViews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.ToDoItem;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

/**
 * Used to create and update assignments node entities
 *
 * @author Nathan Tsai
 * @since Nov 24, 2020
 */
public class ToDoViewActivity extends NodeViewActivity implements YouTubePlayer.OnInitializedListener {
  /** The Firebase Auth Instance */
  private FirebaseUser currentUser;

  /** Fields */
  private TextView toDoItemName;
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
  private YouTubePlayer player;

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

  private YouTubePlayer YPlayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_to_do_item_view);
    toDoItem = new ToDoItem();

    // Get fields
    toDoItemName = findViewById(R.id.to_do_item_name);
    youTubeView = findViewById(R.id.youtube_view);
    youTubeView.initialize(YoutubeConfig.YOUTUBE_API_KEY, this);

    final EditText seekToText = findViewById(R.id.seek_to_text);
    Button seekToButton = findViewById(R.id.seek_to_button);
    seekToButton.setOnClickListener(v -> {
      int skipToSecs = Integer.parseInt(seekToText.getText().toString());
      player.seekToMillis(skipToSecs * 1000);
    });
  }

  @Override
  public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
    this.player = player;

    if (!wasRestored) {
      player.cueVideo("fhWaJi1Hsfo"); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
    }
  }

  @Override
  public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
    if (errorReason.isUserRecoverableError()) {
      errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
    } else {
      String error = String.format(getString(R.string.player_error), errorReason.toString());
      Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (requestCode == RECOVERY_REQUEST) {
      // Retry initialization if user performed a recovery action
      getYouTubePlayerProvider().initialize(YoutubeConfig.YOUTUBE_API_KEY, this);
    }

    if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
      Uri videoUri = intent.getData();
      recordingVideo.setVideoURI(videoUri);
    }
  }

  protected Provider getYouTubePlayerProvider() {
    return youTubeView;
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

}

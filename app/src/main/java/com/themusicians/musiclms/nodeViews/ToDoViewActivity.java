package com.themusicians.musiclms.nodeViews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Node.Node;
import com.themusicians.musiclms.entity.Node.ToDoItem;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Used to create and update assignments node entities
 *
 * @author Nathan Tsai
 * @since Nov 24, 2020
 */
public class ToDoViewActivity extends NodeViewActivity
    implements ToDoRecordingFeedbackAdapter.ItemClickListener {
  /** The Firebase Auth Instance */
  private FirebaseUser currentUser;

  /** Fields */
  private TextView toDoItemName;

  private CheckBox toDoCheck;

  /** For video recording */
  static final int REQUEST_VIDEO_CAPTURE = 1;

  /** The To Do Item object */
  protected ToDoItem toDoItem;

  /** Youtube Player */
  private static final int RECOVERY_REQUEST = 1;

  private LinearLayout youtubePlayerAndFeedbackLayout;
  private Button changeYoutubeVideo;
  private YouTubePlayerView youTubeView;
  private Button seekToButton;
  private EditText seekToInput;

  /** Recording Feedback */
  private ToDoRecordingFeedbackAdapter recordingFeedbackAdapter;

  private RecyclerView recordingFeedbackListView;
  private EditText feedbackText;
  private Button addFeedback;

  /** Add & Store Recording fields */
  private LinearLayout youtubeRecordingLayout;

  private EditText addYoutubeUrl;
  private Uri recordingVideoUri;
  private Button addRecording;
  private boolean alreadyInitAddRecording = false;
  private boolean alreadyInitYoutubeRecording = false;

  /** Tech experience */
  List<String> exp = new ArrayList<>();

  int count = 0;

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

                  // Check if user can delete to do item
                  invalidateOptionsMenu();

                  toDoItemName.setText(toDoItem.getName());

                  // Set checked
                  toDoCheck.setChecked(toDoItem.getCompleteToDo());

                  String youtubeId = toDoItem.getRecordingYoutubeId();

                  if (youtubeId == null) {
                    hideYoutubeVideoAndRecordingFeedback();
                    showAddYoutubeRecording();
                  } else {
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
    } else {
      Toast.makeText(
              ToDoViewActivity.this, "There was a problem loading this ToDo", Toast.LENGTH_LONG)
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
    toDoCheck.setOnClickListener(
        view -> {
          toDoItem.setCompleteToDo(toDoCheck.isChecked());
          toDoItem.save();
        });

    /*
    Record Video
    */
    youtubeRecordingLayout = findViewById(R.id.youtube_recorder_wrapper);
    addRecording = findViewById(R.id.add_recording_video);
    addYoutubeUrl = findViewById(R.id.addYoutubeUrl);

    addRecording.setOnClickListener(
        view -> {
          dispatchTakeVideoIntent();
        });

    /*
    Play Video
    */
    youtubePlayerAndFeedbackLayout = findViewById(R.id.youtube_player_and_recording_wrapper);
    changeYoutubeVideo = findViewById(R.id.changeRecording);
    youTubeView = findViewById(R.id.youtube_player_view);
    seekToInput = findViewById(R.id.seek_to_input2);
    seekToButton = findViewById(R.id.seek_to_button);

    // Load Youtube (for performance)
    getLifecycle().addObserver(youTubeView);

    changeYoutubeVideo.setOnClickListener(
        view -> {
          showAddYoutubeRecording();
          hideYoutubeVideoAndRecordingFeedback();
        });

    /*
    Add Feedback for Recording
    */
    recordingFeedbackListView = findViewById(R.id.recordingFeedbackListView);
    feedbackText = findViewById(R.id.add_recording_feedback__text);
    addFeedback = findViewById(R.id.add_recording_feedback__save);

    addFeedback.setOnClickListener(
        view -> {
          int timeOfFeedback = Integer.decode(seekToInput.getText().toString());
          toDoItem.addRecordingFeedback(feedbackText.getText().toString(), timeOfFeedback);
          toDoItem.save();

          if (recordingFeedbackAdapter != null) {
            // Update recording feedback
            //        recordingFeedbackAdapter.clear();
            recordingFeedbackAdapter.notifyDataSetChanged();

          } else {
            initRecordingFeedbackAdapter();
          }
        });

    // Initialize Attachments
    //    initShowAttachments(R.id.showAttachments__to_do__view, "todo__view");

    /** Checks to see if user needs help with uploading youtube videos */
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference tech =
        FirebaseDatabase.getInstance()
            .getReference()
            .child("node__user")
            .child(currentUser.getUid())
            .child("techExperience");

    tech.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()) {
              exp.add(ds.getValue(String.class));
              count++;
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });

    tech.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            boolean popup = true;
            for (DataSnapshot ds : snapshot.getChildren()) {
              if (ds.getValue(String.class).equals(getString(R.string.upload_youtube))) {
                popup = false;
              }
            }
            if (popup) {
              final String[] listItems = {
                getString(R.string.youtube_help), getString(R.string.not_again)
              };
              AlertDialog.Builder mBuilder = new AlertDialog.Builder(ToDoViewActivity.this);
              mBuilder.setTitle(R.string.youtube_tutorial);
              mBuilder.setSingleChoiceItems(
                  listItems,
                  -1,
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      if (which == 0) {
                        Intent viewIntent =
                            new Intent(
                                "android.intent.action.VIEW",
                                Uri.parse(
                                    "https://www.businessinsider.com/how-to-upload-a-video-to-youtube"));
                        startActivity(viewIntent);
                      }
                      if (which == 1) {
                        tech.child(Integer.toString(count))
                            .setValue(getString(R.string.upload_youtube));
                      }
                      dialog.dismiss();
                    }
                  });
              AlertDialog mDialog = mBuilder.create();
              mDialog.show();
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });
  }

  /** Return the node to add attachments to */
  @Override
  public Node getNodeForAttachments() {
    return toDoItem;
  }

  /** Helper functions for toggle displays of recording player vs add recording */
  private void showAddYoutubeRecording() {
    // Only show if recording is required or is the author
    if (toDoItem.getRequireRecording()) {
      initAddRecording();
      youtubeRecordingLayout.setVisibility(View.VISIBLE);
    }
  }

  private void hideAddYoutubeRecording() {
    youtubeRecordingLayout.setVisibility(View.GONE);
  }

  private void showYoutubeVideoAndRecordingFeedback() {
    youtubePlayerAndFeedbackLayout.setVisibility(View.VISIBLE);

    if (toDoItem.getRecordingYoutubeId() != null) {
      initYoutubeVideoAndRecordingFeedback(toDoItem.getRecordingYoutubeId());
    }
    initRecordingFeedbackAdapter();
  }

  private void hideYoutubeVideoAndRecordingFeedback() {
    youtubePlayerAndFeedbackLayout.setVisibility(View.GONE);
  }

  /** Initialize add Recording */
  private void initAddRecording() {
    if (alreadyInitAddRecording) {
      return;
    }

    // After user input youtube url
    addYoutubeUrl.addTextChangedListener(
        new TextWatcher() {
          public void afterTextChanged(Editable youtubeUrlEditable) {
            String youtubeId = ToDoItem.getYoutubeIdFromUrl(youtubeUrlEditable.toString());

            if (youtubeId != null) {
              toDoItem.setRecordingYoutubeId(youtubeId);
              toDoItem.save();

              showYoutubeVideoAndRecordingFeedback();
              hideAddYoutubeRecording();

              Toast.makeText(
                      ToDoViewActivity.this, "Youtube Video saved and loading", Toast.LENGTH_SHORT)
                  .show();
            } else {
              Toast.makeText(
                      ToDoViewActivity.this,
                      "We could not process the Youtube Url",
                      Toast.LENGTH_LONG)
                  .show();
            }
          }

          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

    alreadyInitAddRecording = true;
  }

  /**
   * Initialize the Recording Feedback adapter
   *
   * <p>Assumes toDoItem.getRecordingFeedback() is not null
   */
  private void initRecordingFeedbackAdapter() {
    if (recordingFeedbackAdapter == null) {
      // Set up layout manager
      LinearLayoutManager llm = new LinearLayoutManager(this);
      llm.setOrientation(LinearLayoutManager.VERTICAL);
      recordingFeedbackListView.setLayoutManager(llm);

      // Set up the adapter
      recordingFeedbackAdapter =
          new ToDoRecordingFeedbackAdapter(this, toDoItem.getRecordingFeedback());
      recordingFeedbackAdapter.addItemClickListener(this);
      recordingFeedbackListView.setAdapter(recordingFeedbackAdapter);
    }
  }

  //  private void calculateHeightOfRecordingFeedbackAdapter() {
  //    if(recordingFeedbackAdapter.getCount() > 5){
  //      View item = recordingFeedbackAdapter.getView(0, null, recordingFeedbackListView);
  //      item.measure(0, 0);
  //      ViewGroup.LayoutParams params = new
  // ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (5.5 *
  // item.getMeasuredHeight()));
  //      recordingFeedbackListView.setLayoutParams(params);
  //    }
  //  }

  /**
   * Initialize the youtube video player
   *
   * <p>change addTextChanged Listener to getOnFocusChangeListener
   * https://stackoverflow.com/questions/8666380/android-evaluate-edittext-after-the-user-finishes-editing
   */
  private void initYoutubeVideoAndRecordingFeedback(@NonNull String videoId) {
    if (!alreadyInitYoutubeRecording) {
      youTubeView.addYouTubePlayerListener(
          new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
              youTubePlayer.loadVideo(videoId, 0);
              youTubePlayer.pause();

              seekToButton.setOnClickListener(
                  view -> {
                    int skipToSecs = Integer.parseInt(seekToInput.getText().toString());
                    youTubePlayer.seekTo(skipToSecs);
                    youTubePlayer.play();
                    youTubePlayer.pause();

                    Toast.makeText(
                            ToDoViewActivity.this,
                            "Button: Player time changed to: " + skipToSecs,
                            Toast.LENGTH_LONG)
                        .show();
                  });
            }

            /** On pause, move focus to feedback text */
            public void onStateChange(
                @NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
              switch (state) {
                case PAUSED:
                  /*
                   * Set focus back to addFeedback
                   * --------
                   * Source: https://stackoverflow.com/a/8991563
                   * Author: David Merriman (https://stackoverflow.com/u/1106671)
                   */
                  addFeedback.requestFocus();
                  InputMethodManager imm =
                      (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                  imm.showSoftInput(addFeedback, InputMethodManager.SHOW_IMPLICIT);
                  break;

                case ENDED:
                  youTubePlayer.seekTo(0);
                  //              youTubePlayer.pause();
                  break;
              }
            }

            /** Update the seek text */
            @Override
            public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second) {
              seekToInput.setText(String.valueOf((int) second));
            }
          });

      alreadyInitYoutubeRecording = true;
    } else {
      // Just update the video id
      youTubeView.getYouTubePlayerWhenReady(
          youTubePlayer -> {
            youTubePlayer.loadVideo(videoId, 0);
          });
    }
  }

  /**
   * When clicking an item of the feedback
   *
   * @param type the type of click
   * @param time the time to skip youtube recording to
   * @param position the position of the feedback
   */
  @Override
  public void onToDoRecordingFeedbackClick(String type, int time, int position) {
    seekToInput.setText(String.valueOf(time));
    seekToButton.performClick();
  }

  /**
   * Record video
   *
   * <p>See: https://developer.android.com/training/camera/videobasics For YT:
   * https://www.sitepoint.com/using-the-youtube-api-to-embed-video-in-an-android-app/
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
    sharingIntent.putExtra(Intent.EXTRA_TITLE, "Testing Title 2");
    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Description of youtube video");
    sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, videoUri);
    startActivity(Intent.createChooser(sharingIntent, "Share To:"));
  }
}

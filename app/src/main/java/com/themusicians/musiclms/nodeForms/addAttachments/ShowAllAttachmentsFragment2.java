package com.themusicians.musiclms.nodeForms.addAttachments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.entity.Attachment.AllAttachment;
import com.themusicians.musiclms.entity.Node.Node;
import com.themusicians.musiclms.nodeForms.NodeActivity;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;
import static com.themusicians.musiclms.nodeForms.addAttachments.ShowAllAttachmentsAdapter.OPEN_ZOOM;
import static com.themusicians.musiclms.nodeForms.addAttachments.ShowAllAttachmentsAdapter.SHOW_PDF;
import static com.themusicians.musiclms.nodeForms.addAttachments.ShowAllAttachmentsAdapter.editAllAttachments;

/**
 * Displays the AllAttachment
 *
 * <p>Based on
 * https://www.geeksforgeeks.org/how-to-populate-recyclerview-with-firebase-data-using-firebaseui-in-android-studio/
 *
 * @contributor
 * @author Nathan Tsai
 * @since Nov 19, 2020
 */
public class ShowAllAttachmentsFragment2 extends ShowAllAttachmentsAdapter {

  public ShowAllAttachmentsFragment2(@NonNull FirebaseRecyclerOptions<AllAttachment> options) {
    super(options);
  }
}

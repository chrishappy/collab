package com.themusicians.musiclms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.chat.NewChat;
import com.themusicians.musiclms.entity.Node.User;
import com.themusicians.musiclms.nodeViews.AssignmentOverviewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Display the user information and link to chat page
 *
 * @contributors Harveer Khangura
 * @author Jerome Lau
 * @since Nov 10, 2020
 * @todo View user info
 */
public class UserProfile extends AppCompatActivity {

  protected TextView myName, myEmail, newName, newEmail;
  protected FirebaseUser currentUser;
  protected FirebaseAuth fAuth;

  protected Button addInstrument, userProfileBack;
  protected List<String> instruments;
  ArrayList<String> myArrayList = new ArrayList<>();
  boolean reInput;
  protected ListView instrumentList;
  protected User currUser;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_profile_main);

    /*
     * Initialize variables
     */
    final EditText newInstrument;

    myName = findViewById(R.id.user_name);
    myEmail = findViewById(R.id.user_email);

    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    currUser = new User(currentUser.getUid());

    newInstrument = findViewById(R.id.enterInstruments);
    addInstrument = findViewById(R.id.addButton);
    instruments = new ArrayList<>();
    instrumentList = findViewById(R.id.InstrumentList);
    final ArrayAdapter<String> myArrayAdapter =
        new ArrayAdapter<String>(
            UserProfile.this, android.R.layout.simple_list_item_1, myArrayList);
    DatabaseReference InstrumentRef =
        FirebaseDatabase.getInstance()
            .getReference()
            .child("node__user")
            .child(currentUser.getUid())
            .child("instruments");

    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    /*
     * References the user node and current user to fetch user name/email and displays them
     */
    DatabaseReference reference =
        FirebaseDatabase.getInstance()
            .getReference()
            .child("node__user")
            .child(currentUser.getUid());
    reference.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            Object name = snapshot.child("name").getValue();
            /*
             * Checks if names exists
             */
            if (name != null) {
              myName.setText(name.toString());
            }

            Object email = snapshot.child("email").getValue();
            /*
             * Checks if email exists
             */
            if (email != null) {
              myEmail.setText(email.toString());
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });

    /** Saves user instruments in Firebase */
    reInput = true;
    currUser
        .getEntityDatabase()
        .child(currentUser.getUid())
        .addListenerForSingleValueEvent(
            new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                currUser = snapshot.getValue(User.class);
                addInstrument.setOnClickListener(
                    (v) -> {
                      String instrumentName = newInstrument.getText().toString().trim();
                      /*
                       * Checks if instrument is new
                       */
                      boolean isNew = true;
                      for (int i = 0; i < myArrayList.size(); i++) {
                        if (instrumentName.toLowerCase().equals(myArrayList.get(i).toLowerCase())) {
                          isNew = false;
                        }
                      }
                      /*
                       * Checks if instrument is empty
                       */
                      if (instrumentName.isEmpty()) {
                        Toast.makeText(
                                UserProfile.this, "No instrument entered", Toast.LENGTH_SHORT)
                            .show();
                      } else if (isNew == false) {
                        Toast.makeText(
                                UserProfile.this,
                                instrumentName.toLowerCase() + " is already added",
                                Toast.LENGTH_SHORT)
                            .show();
                      } else {
                        instruments.add(instrumentName);
                        currUser.setInstruments(instruments);
                        currUser.save();
                        Toast.makeText(UserProfile.this, "Instrument added", Toast.LENGTH_SHORT)
                            .show();
                        reInput = false;
                      }
                      newInstrument.setText("");
                    });
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {}
            });

    /*
     * Display and update instruments from Firebase
     */
    instrumentList.setAdapter(myArrayAdapter);
    InstrumentRef.addChildEventListener(
        new ChildEventListener() {
          @Override
          /*
           * Runs on each instance of data and each time new child is added
           * Takes each instance of data and stores it in a list
           * @param snapshot data snapshot to fetch data from Firebase
           * @param previousChildName
           */
          public void onChildAdded(
              @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            String value = snapshot.getValue(String.class);
            /*
             * Checks to pull instruments to list once
             */
            if (reInput == true) {
              instruments.add(value);
            }
            myArrayList.add(value);
            myArrayAdapter.notifyDataSetChanged();
          }

          /**
           * Updates instrument list if instrument is changed
           *
           * @param snapshot data snapshot to fetch data from Firebase
           * @param previousChildName
           */
          @Override
          public void onChildChanged(
              @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            String value = snapshot.getValue(String.class);
            myArrayList.add(value);
            myArrayAdapter.notifyDataSetChanged();
          }

          /**
           * Updates instrument list if instrument is removed
           *
           * @param snapshot data snapshot to fetch data from Firebase
           */
          @Override
          public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            String value = snapshot.getValue(String.class);
            myArrayList.remove(value);
            myArrayAdapter.notifyDataSetChanged();
          }

          @Override
          public void onChildMoved(
              @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });
  }

  /** Redirects to add users */
  public void toAddUser(View view) {
    Intent toAdd = new Intent(this, UserAddUsers.class);
    startActivity(toAdd);
  }

  /** Redirects User to Editing data */
  public void toEditData(View view) {
    setContentView(R.layout.user_profile_edit_data);
  }

  /** Updates user name in Firebase */
  public void changeName(View view) {
    newName = findViewById(R.id.changeName);
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String name = newName.getText().toString().trim();

    FirebaseDatabase.getInstance()
        .getReference()
        .child("node__user")
        .child(currentUser.getUid())
        .child("name")
        .setValue(name)
        .addOnSuccessListener(
            aVoid -> Toast.makeText(UserProfile.this, "Name updated", Toast.LENGTH_SHORT).show());
  }

  /** Updates user email in Firebase */
  public void changeEmail(View view) {
    newEmail = findViewById(R.id.changeEmail);
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String email = newEmail.getText().toString().trim();

    FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("email").setValue(email);
    currentUser
        .updateEmail(email)
        .addOnSuccessListener(
            aVoid -> Toast.makeText(UserProfile.this, "Email updated", Toast.LENGTH_SHORT).show());
  }

  /** Updates user password in Firebase through email verification */
  public void changePassword(View view) {
    fAuth = FirebaseAuth.getInstance();

    /** Email verification dialog */
    EditText resetMail = new EditText(view.getContext());
    AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder((view.getContext()));

    passwordResetDialog.setTitle("Reset Password");
    passwordResetDialog.setMessage("Enter your email to receive password reset link");
    passwordResetDialog.setView(resetMail);

    passwordResetDialog.setPositiveButton(
        "Yes",
        (dialog, which) -> {
          String mail = resetMail.getText().toString();
          fAuth
              .sendPasswordResetEmail(mail)
              .addOnSuccessListener(
                  aVoid ->
                      Toast.makeText(
                              UserProfile.this, "Resent link sent to email", Toast.LENGTH_SHORT)
                          .show())
              .addOnFailureListener(
                  e ->
                      Toast.makeText(
                              UserProfile.this,
                              "Error! Reset link not sent" + e.getMessage(),
                              Toast.LENGTH_SHORT)
                          .show());
        });

    passwordResetDialog.setNegativeButton("No", (dialog, which) -> {});

    passwordResetDialog.create().show();
  }

  /** Redirects user to user profile page */
  public void editDataBack(View view) {
    Intent reload = new Intent(this, UserProfile.class);
    startActivity(reload);
  }

  /** Redirects user to Assignment Overview */
  public void backFromProfile(View view) {
    Intent toAssignmentOverview = new Intent(this, AssignmentOverviewActivity.class);
    startActivity(toAssignmentOverview);
  }

  /** Shifan's code */
  public void toChat(View view) {
    Intent toChatPage = new Intent(this, NewChat.class);
    startActivity(toChatPage);
  }
}

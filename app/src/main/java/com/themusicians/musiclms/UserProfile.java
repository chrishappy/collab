package com.themusicians.musiclms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.entity.Node.User;
import java.util.ArrayList;
import java.util.List;

/**
 * ....
 *
 * <p>
 * @Contributors: Jerome Lau, Harveer Khangura
 * @Author Jerome Lau
 * @Since Nov 10, 2020
 * <p>--------------------------------
 *
 * @todo View user info
 */
public class UserProfile extends AppCompatActivity {

  protected TextView myName;
  protected TextView myEmail;

  protected FirebaseUser currentUser;
  protected FirebaseAuth fAuth;

  protected Button add;
  protected List<String> Instruments;
  ArrayList<String> myArrayList = new ArrayList<>();
  boolean reInput;

  protected User currUser;
  protected ListView InstrumentList;
/*
  protected TabLayout tabLayout = findViewById(R.id.accountTeachersTab);
  protected TabItem tabAccounts = findViewById(R.id.accTab);
  protected TabItem tabTeachers = findViewById(R.id.teachersTab);
  protected ViewPager2 viewpager1 = findViewById(R.id.viewpager1);
*/


  protected TextView newName, newEmail;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_profile_main);

    /**
     * Initialize variables
     */

    /*
    tabLayout = findViewById(R.id.accountTeachersTab);
    tabAccounts = findViewById(R.id.accTab);
    TabItem tabTeachers = findViewById(R.id.teachersTab);
    ViewPager2 viewpager1 = findViewById(R.id.viewpager1);*/


    final EditText newInstrument;

    myName = findViewById(R.id.user_name);
    myEmail = findViewById(R.id.user_email);

    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    currUser = new User(currentUser.getUid());

    newInstrument = findViewById(R.id.enterInstruments);
    add = findViewById(R.id.addButton);
    Instruments = new ArrayList<String>();
    InstrumentList = (ListView) findViewById(R.id.InstrumentList);
    final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(UserProfile.this, android.R.layout.simple_list_item_1, myArrayList);
    DatabaseReference InstrumentRef = FirebaseDatabase.getInstance().getReference().child("node__user").child(currentUser.getUid()).child("instruments");

    /**
     * Fetch and display user name/email from Firebase
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
            /**
             * Checks if names exists
             */
            if (name != null) {
              myName.setText(name.toString());
            }

            Object email = snapshot.child("email").getValue();
            /**
             * Checks if email exists
             */
            if (email != null) {
              myEmail.setText(email.toString());
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });

    /**
     * Save user instruments in Firebase
     */
    reInput = true;
    currUser.getEntityDatabase().child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        currUser = snapshot.getValue(User.class);
        add.setOnClickListener(
          (v) -> {
            String instrumentName = newInstrument.getText().toString().trim();
            /**
             * Checks if instrument is new
             */
            boolean isNew = true;
            for (int i = 0; i < myArrayList.size(); i++) {
              if (instrumentName.toLowerCase().equals(myArrayList.get(i).toLowerCase())) {
                isNew = false;
              }
            }

            if (instrumentName.isEmpty()) {
              Toast.makeText(UserProfile.this, "No instrument entered", Toast.LENGTH_SHORT).show();
            } else if (isNew == false){
              Toast.makeText(UserProfile.this, instrumentName.toLowerCase() + " is already added",Toast.LENGTH_SHORT).show();
            } else {
              Instruments.add(instrumentName);
              currUser.setInstruments(Instruments);
              currUser.save();
              reInput = false;
            }
          });
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

    /**
     * Display and update instruments from Firebase
     */
    InstrumentList.setAdapter(myArrayAdapter);
    InstrumentRef.addChildEventListener(new ChildEventListener() {
      @Override
      public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        /**
         * Updates display if instrument is added
         */
        String value = snapshot.getValue(String.class);
        /**
         * Checks to pull instruments to list once
         */
        if(reInput == true) {
          Instruments.add(value);
        }
        myArrayList.add(value);
        myArrayAdapter.notifyDataSetChanged();
      }

      @Override
      public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        String value = snapshot.getValue(String.class);
        myArrayList.add(value);
        myArrayAdapter.notifyDataSetChanged();
      }

      @Override
      public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        /**
         * Updates display if intrument is removed
         */
        String value = snapshot.getValue(String.class);
        myArrayList.remove(value);
        myArrayAdapter.notifyDataSetChanged();
      }

      @Override
      public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  public void toSearchTeachers(View view){
    Intent toSearch = new Intent(this, UserSearch.class);
    startActivity(toSearch);
  }

  /**
   * Redirects User to Editing data
   */
  public void toEditData(View view) {
    setContentView(R.layout.user_profile_edit_data);
  }

  /**
   * Updates user name in Firebase
   */
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
            new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                Toast.makeText(UserProfile.this, "Name updated", Toast.LENGTH_SHORT).show();
              }
            });
  }

  /**
   * Updates user email in Firebase
   */
  public void changeEmail(View view) {
    newEmail = findViewById(R.id.changeEmail);
    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String email = newEmail.getText().toString().trim();

    currentUser
        .updateEmail(email)
        .addOnSuccessListener(
            new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                Toast.makeText(UserProfile.this, "Email updated", Toast.LENGTH_SHORT).show();
              }
            });
  }

  /**
   * Updates user password in Firebase through email verification
   */
  public void changePassword(View view) {
    fAuth = FirebaseAuth.getInstance();

    /**
     * Email verification dialog
     */
    EditText resetMail = new EditText(view.getContext());
    AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder((view.getContext()));

    passwordResetDialog.setTitle("Reset Password");
    passwordResetDialog.setMessage("Enter your email to receive password reset link");
    passwordResetDialog.setView(resetMail);

    passwordResetDialog.setPositiveButton(
        "Yes",
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

            String mail = resetMail.getText().toString();
            fAuth
                .sendPasswordResetEmail(mail)
                .addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                        Toast.makeText(
                                UserProfile.this, "Resent link sent to email", Toast.LENGTH_SHORT)
                            .show();
                      }
                    })
                .addOnFailureListener(
                    new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                        Toast.makeText(
                                UserProfile.this,
                                "Error! Reset link not sent" + e.getMessage(),
                                Toast.LENGTH_SHORT)
                            .show();
                      }
                    });
          }
        });

    passwordResetDialog.setNegativeButton(
        "No",
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {}
        });

    passwordResetDialog.create().show();
  }

  /**
   * Redirects user to user profile page
   */
  public void editDataBack(View view) {
    Intent reload = new Intent(this, UserProfile.class);
    startActivity(reload);
  }

  /** Shifan's code */
  public void goChat(View view) {
    Intent Chatpage = new Intent(this, Chat.class);
    startActivity(Chatpage);
  }
}

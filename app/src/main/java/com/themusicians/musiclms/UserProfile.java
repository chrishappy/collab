package com.themusicians.musiclms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

/**
 * ....
 *
 * <p>Contributors: Jerome Lau, Harveer Khangura Created by Jerome Lau on 2020-11-06
 *
 * <p>--------------------------------
 *
 * @todo View user info
 */
public class UserProfile extends AppCompatActivity {

  protected TextView myName;
  protected TextView myEmail;
  protected FirebaseUser currentUser;
  protected FirebaseAuth fAuth;
  protected ListView listView;
  protected Button add;
  protected EditText edit;

  protected TextView newName, newEmail;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_profile_main);

    myName = findViewById(R.id.user_name);
    myEmail = findViewById(R.id.user_email);
    // list view for user_profile_main page
    listView = findViewById(R.id.instrument_list);
    edit = findViewById(R.id.enterInstruments);
    add = findViewById(R.id.addButton);
    currentUser = FirebaseAuth.getInstance().getCurrentUser();

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
            if (name != null) {
              myName.setText(name.toString());
            }

            Object email = snapshot.child("email").getValue();
            if (email != null) {
              myEmail.setText(email.toString());
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });

    add.setOnClickListener(
        (v) -> {
          String instrumentName = edit.getText().toString();
          if (instrumentName.isEmpty()) {
            Toast.makeText(userProfile.this, "No instrument entered", Toast.LENGTH_SHORT).show();
          } else {
            FirebaseDatabase.getInstance()
                .getReference()
                .child("node__user")
                .child("user_instruments")
                .setValue(instrumentName);
          }
        });

    final ArrayList<String> list = new ArrayList<>();
    final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.user_profile_main, list);
    listView.setAdapter(adapter);

    DatabaseReference instrumentReference =
        FirebaseDatabase.getInstance()
            .getReference()
            .child("node__user")
            .child(currentUser.getUid())
            .child("user_instruments");
    reference.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot inSnapshot) {
            list.clear();
            for (DataSnapshot instrumentSnapshot : inSnapshot.getChildren()) {
              list.add(instrumentSnapshot.getValue().toString());
            }
            adapter.notifyDataSetChanged();
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });
  }

  public void toEditData(View view) {
    setContentView(R.layout.user_profile_edit_data);
  }

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

    // make prompt that says it worked or smth
  }

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

  public void changePassword(View view) {
    fAuth = FirebaseAuth.getInstance();

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

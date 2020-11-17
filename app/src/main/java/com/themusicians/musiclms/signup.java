package com.themusicians.musiclms;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.themusicians.musiclms.entity.Node.User;

/**
 * ....
 *
 * <p>Contributors: Jerome Lau Created by Jerome Lau on 2020-11-03
 *
 * <p>--------------------------------
 *
 * @todo Authenticate users via Firebase
 * @todo Store miscellaneous user info in Firebase
 * @todo Proceed through sign up layouts
 */
public class signup extends AppCompatActivity {
  protected EditText newEmail, newPassword, newName;
  protected Button teacher, student;
  protected FirebaseAuth fAuth;
  protected CheckBox sendText, makeCall, joinZoom, scheduleZoom, watchYoutube, uploadYoutube;
  DatabaseReference reference;

  /** Save User Date */
  protected FirebaseUser currentUser;

  protected User newUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_signup_main);

    newEmail = findViewById(R.id.newEmail);
    newPassword = findViewById(R.id.newPassword);
    newName = findViewById(R.id.newName);

    // Store user
    fAuth = FirebaseAuth.getInstance();
    teacher = findViewById(R.id.signup_teacher);
    student = findViewById(R.id.signup_student);

    // Sign up page teacher button
    teacher.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            String email = newEmail.getText().toString().trim();
            String password = newPassword.getText().toString().trim();
            String name = newName.getText().toString().trim();

            // checks if email is empty
            if (TextUtils.isEmpty(email)) {
              newEmail.setError("Email is Required.");
              return;
            }

            // checks if password is empty
            if (TextUtils.isEmpty(password)) {
              newPassword.setError("Password is Required");
              return;
            }

            // checks for password minimum length
            if (password.length() < 6) {
              newPassword.setError("Password must be more than 5 characters");
              return;
            }

            //checks if name is empty
            if (TextUtils.isEmpty(name)) {
              newName.setError("Name is Required");
              return;
            }

            // registers account to firebase and sends to next screen
            fAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                          // Get current user
                          currentUser = FirebaseAuth.getInstance().getCurrentUser();

                          newUser = new User(currentUser.getUid());
                          newUser.setStatus(true);
                          newUser.setEmail(email);
                          newUser.setName(name);
                          newUser.save();

                          Toast.makeText(signup.this, "User Created", Toast.LENGTH_SHORT).show();
                          setContentView(R.layout.user_signup_tech);
                        } else {
                          Toast.makeText(
                                  signup.this,
                                  "Error" + task.getException().getMessage(),
                                  Toast.LENGTH_SHORT)
                              .show();
                        }
                      }
                    });
          }
        });

    // Sign up page student button
    student.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          String email = newEmail.getText().toString().trim();
          String password = newPassword.getText().toString().trim();
          String name = newName.getText().toString().trim();

          // checks if email is empty
          if (TextUtils.isEmpty(email)) {
            newEmail.setError("Email is Required.");
            return;
          }

          // checks if password is empty
          if (TextUtils.isEmpty(password)) {
            newPassword.setError("Password is Required");
            return;
          }

          // checks for password minimum length
          if (password.length() < 6) {
            newPassword.setError("Password must be more than 5 characters");
            return;
          }

          //checks if name is empty
          if (TextUtils.isEmpty(name)) {
            newName.setError("Name is Required");
            return;
          }

          // registers account to firebase and sends to next screen
          fAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
              new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                  if (task.isSuccessful()) {

                    // Get current user
                    currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    newUser = new User(currentUser.getUid());
                    newUser.setStatus(true);
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.save();

                    Toast.makeText(signup.this, "User Created", Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.user_signup_tech);
                  } else {
                    Toast.makeText(
                      signup.this,
                      "Error" + task.getException().getMessage(),
                      Toast.LENGTH_SHORT)
                      .show();
                  }
                }
              });
        }
      });
  }

  // Sign up tech page
  public void signUpFinish(View view) {

    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    sendText = findViewById(R.id.sendText);
    makeCall = findViewById(R.id.makeCall);
    joinZoom = findViewById(R.id.joinZoom);
    scheduleZoom = findViewById(R.id.scheduleZoom);
    watchYoutube = findViewById(R.id.watchYoutube);
    uploadYoutube = findViewById(R.id.uploadYoutube);

    reference =
        FirebaseDatabase.getInstance()
            .getReference()
            .child("node__user")
            .child(currentUser.getUid());

    String sT = "Can send Text";
    String mC = "Can make Call";
    String jZ = "Can join Zoom";
    String sZ = "Can schedule Zoom";
    String wY = "Can watch Youtube";
    String uY = "Can upload Youtube";

    if (sendText.isChecked()) {
      newUser.setSendText(sT);
      newUser.save();
    }

    if (makeCall.isChecked()) {
      newUser.setMakeCall(mC);
      newUser.save();
    }

    if (joinZoom.isChecked()) {
      newUser.setJoinZoom(jZ);
      newUser.save();
    }

    if (scheduleZoom.isChecked()) {
      newUser.setScheduleZoom(sZ);
      newUser.save();
    }

    if (watchYoutube.isChecked()) {
      newUser.setWatchYoutube(wY);
      newUser.save();
    }

    if (uploadYoutube.isChecked()) {
      newUser.setUploadYoutube(uY);
      newUser.save();
    }

    Intent signupFinish = new Intent(this, Placeholder.class);
    startActivity(signupFinish);
  }

  public void signUpTechBack(View view) {
    setContentView(R.layout.user_signup_main);
  }
}

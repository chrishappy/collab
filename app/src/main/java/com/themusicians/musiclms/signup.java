package com.themusicians.musiclms;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
  EditText newEmail, newPassword, newName;
  Button next;
  FirebaseAuth fAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);

    newEmail = findViewById(R.id.newEmail);
    newPassword = findViewById(R.id.newPassword);
    fAuth = FirebaseAuth.getInstance();
    next = findViewById(R.id.signup_next);

    // Sign up page
    next.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            String email = newEmail.getText().toString().trim();
            String password = newPassword.getText().toString().trim();

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

            // registers account to firebase and sends to next screen
            fAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                          Toast.makeText(signup.this, "User Created", Toast.LENGTH_SHORT).show();
                          setContentView(R.layout.signup_details);
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

  // Sign up details page
  public void signUpDetailsNext(View view) {
    newName = findViewById(R.id.newName);
    String name = newName.getText().toString().trim();
    if (TextUtils.isEmpty(name)) {
      newName.setError("Name is Required");
      return;
    }

    setContentView(R.layout.signup_tech);
  }

  // Sign up tech page
  public void signUpFinish(View view) {
    Intent signupFinish = new Intent(this, Placeholder.class);
    startActivity(signupFinish);
  }

  // All back button functions
  public void signUpBack(View view) {
    Intent signupBack = new Intent(this, MainActivity.class);
    startActivity(signupBack);
  }

  public void signUpDetailsBack(View view) {
    setContentView(R.layout.activity_signup);
  }

  public void signUpTechBack(View view) {
    setContentView(R.layout.signup_details);
  }
}

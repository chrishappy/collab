package com.themusicians.musiclms;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.themusicians.musiclms.nodeViews.AssignmentOverviewActivity;

/**
 * The user login page
 *
 * @contributors Jerome Lau
 * @author: Jerome Lau
 * @since Nov 4, 2020
 *
 */
public class UserLogin extends AppCompatActivity {

  EditText myEmail, myPassword;
  Button signIn;
  FirebaseAuth fAuth;
  TextView register;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_signin);

    /** Initialize Variables */
    myEmail = findViewById(R.id.myEmail);
    myPassword = findViewById(R.id.myPassword);
    fAuth = FirebaseAuth.getInstance();
    signIn = findViewById(R.id.signin);
    register = findViewById(R.id.orRegister);

    /** Listens for when the User clicks Sign In */
    signIn.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            String email = myEmail.getText().toString().trim();
            String password = myPassword.getText().toString().trim();

            /** checks if user email is empty */
            if (TextUtils.isEmpty(email)) {
              myEmail.setError(getString(R.string.email_error));
              return;
            }

            /** checks if user password is empty */
            if (TextUtils.isEmpty(password)) {
              myPassword.setError(getString(R.string.password_error));
              return;
            }

            /**
             * Verifies account with Firebase Redirects user to Assignment Overview
             *
             * @param email references myEmail from user input
             * @param password references myPassword from user input
             */
            fAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                          Toast.makeText(UserLogin.this, R.string.login_successful, Toast.LENGTH_SHORT)
                              .show();
                          startActivity(
                              new Intent(
                                  getApplicationContext(), AssignmentOverviewActivity.class));
                        } else {
                          Toast.makeText(
                                  UserLogin.this,
                                  getString(R.string.login_error) + task.getException().getMessage(),
                                  Toast.LENGTH_SHORT)
                              .show();
                        }
                      }
                    });
          }
        });

    /** Redirects user to Registration */
    register.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent nextPageLogin = new Intent(UserLogin.this, SignUp.class);
            startActivity(nextPageLogin);
          }
        });
  }
}

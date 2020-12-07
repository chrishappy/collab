package com.themusicians.musiclms;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.themusicians.musiclms.entity.Node.User;
import com.themusicians.musiclms.nodeViews.AssignmentOverviewActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * The activity that manages the user sign up
 *
 * @contributor Harveer Khangura
 * @author Jerome Lau
 * @since Nov 3, 2020
 */
public class SignUp extends AppCompatActivity {

  protected EditText newEmail, newPassword, newName;
  protected Button teacher, student;
  protected FirebaseAuth fAuth;
  protected CheckBox sendText, makeCall, joinZoom, scheduleZoom, watchYoutube, uploadYoutube;
  protected TextView toLogin;
  DatabaseReference reference;

  /** Save User Date */
  protected FirebaseUser currentUser;

  protected User newUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_signup_main);

    /*
     * Initialize Variables
     */
    newEmail = findViewById(R.id.newEmail);
    newPassword = findViewById(R.id.newPassword);
    newName = findViewById(R.id.newName);
    toLogin = findViewById(R.id.toSignIn);
    fAuth = FirebaseAuth.getInstance();
    teacher = findViewById(R.id.signup_teacher);
    student = findViewById(R.id.signup_student);

    /*
     * Registers user as a teacher through Firebase
     */
    teacher.setOnClickListener(
        v -> {
          String email = newEmail.getText().toString().trim();
          String password = newPassword.getText().toString().trim();
          String name = newName.getText().toString().trim();

          /*
           * Checks if user email is empty
           */
          if (TextUtils.isEmpty(email)) {
            newEmail.setError(getString(R.string.email_error));
            return;
          }

          /*
           * Checks if user password is empty
           */
          if (TextUtils.isEmpty(password)) {
            newPassword.setError(getString(R.string.password_error));
            return;
          }

          /*
           * Checks if user password is at least 6 characters
           */
          if (password.length() < 6) {
            newPassword.setError(getString(R.string.password_length_error));
            return;
          }

          /*
           * Checks if user name is empty
           */
          if (TextUtils.isEmpty(name)) {
            newName.setError(getString(R.string.name_error));
            return;
          }

          /*
           * Verifies user credentials with Firebase and registers account
           * @param email references newEmail from user input
           * @param password references newPassword from user input
           */
          fAuth
              .createUserWithEmailAndPassword(email, password)
              .addOnCompleteListener(
                  task -> {
                    if (task.isSuccessful()) {

                      currentUser = FirebaseAuth.getInstance().getCurrentUser();

                      /*
                       * Saves data in Firebase
                       */
                      assert currentUser != null;
                      newUser = new User(currentUser.getUid());
                      newUser.setStatus(true);
                      newUser.setEmail(email);
                      newUser.setName(name);
                      newUser.setRole("Teacher");
                      newUser.save();

                      reference =
                          FirebaseDatabase.getInstance().getReference().child("node__isTeacher");
                      reference.child(currentUser.getUid()).setValue(true);

                      Toast.makeText(SignUp.this, R.string.user_created, Toast.LENGTH_SHORT).show();
                      setContentView(R.layout.user_signup_tech);
                    } else {
                      Toast.makeText(
                              SignUp.this,
                              getString(R.string.error) + task.getException().getMessage(),
                              Toast.LENGTH_SHORT)
                          .show();
                    }
                  });
        });

    /*
     * Registers user as a student through Firebase
     */
    student.setOnClickListener(
        v -> {
          String email = newEmail.getText().toString().trim();
          String password = newPassword.getText().toString().trim();
          String name = newName.getText().toString().trim();

          /*
           * Checks if user email is empty
           */
          if (TextUtils.isEmpty(email)) {
            newEmail.setError(getString(R.string.email_error));
            return;
          }

          /*
           * Checks if user password is empty
           */
          if (TextUtils.isEmpty(password)) {
            newPassword.setError(getString(R.string.password_error));
            return;
          }

          /*
           * Checks if user password is at least 6 characters
           */
          if (password.length() < 6) {
            newPassword.setError(getString(R.string.password_length_error));
            return;
          }

          /*
           * Checks if user name is empty
           */
          if (TextUtils.isEmpty(name)) {
            newName.setError(getString(R.string.name_error));
            return;
          }

          /*
           * Verifies user credentials with Firebase and registers account
           * @param email references newEmail from user input
           * @param password references newPassword from user input
           */
          fAuth
              .createUserWithEmailAndPassword(email, password)
              .addOnCompleteListener(
                  task -> {
                    if (task.isSuccessful()) {

                      currentUser = FirebaseAuth.getInstance().getCurrentUser();

                      /*
                       * Saves data in Firebase
                       */
                      assert currentUser != null;
                      newUser = new User(currentUser.getUid());
                      newUser.setStatus(true);
                      newUser.setEmail(email);
                      newUser.setName(name);
                      newUser.setRole("Student");
                      newUser.save();

                      Toast.makeText(SignUp.this, R.string.name_created, Toast.LENGTH_SHORT).show();
                      setContentView(R.layout.user_signup_tech);
                    } else {
                      Toast.makeText(
                              SignUp.this,
                              getString(R.string.error) + task.getException().getMessage(),
                              Toast.LENGTH_SHORT)
                          .show();
                    }
                  });
        });
  }

  /**
   * Saves user tech experience in
   *
   * <p>Currently not used
   */
  public void signUpFinish(View view) {

    currentUser = FirebaseAuth.getInstance().getCurrentUser();
    sendText = findViewById(R.id.sendText);
    makeCall = findViewById(R.id.makeCall);
    joinZoom = findViewById(R.id.joinZoom);
    scheduleZoom = findViewById(R.id.scheduleZoom);
    watchYoutube = findViewById(R.id.watchYoutube);
    uploadYoutube = findViewById(R.id.uploadYoutube);

    List<String> TechExp = new ArrayList<String>();

    String sT = getString(R.string.make_text);
    String mC = getString(R.string.make_call);
    String jZ = getString(R.string.join_zoom);
    String sZ = getString(R.string.schedule_zoom);
    String wY = getString(R.string.watch_youtube);
    String uY = getString(R.string.upload_youtube);

    if (sendText.isChecked()) {
      TechExp.add(sT);
      newUser.setTechExperience(TechExp);
      newUser.save();
    }

    if (makeCall.isChecked()) {
      TechExp.add(mC);
      newUser.setTechExperience(TechExp);
      newUser.save();
    }

    if (joinZoom.isChecked()) {
      TechExp.add(jZ);
      newUser.setTechExperience(TechExp);
      newUser.save();
    }

    if (scheduleZoom.isChecked()) {
      TechExp.add(sZ);
      newUser.setTechExperience(TechExp);
      newUser.save();
    }

    if (watchYoutube.isChecked()) {
      TechExp.add(wY);
      newUser.setTechExperience(TechExp);
      newUser.save();
    }

    if (uploadYoutube.isChecked()) {
      TechExp.add(uY);
      newUser.setTechExperience(TechExp);
      newUser.save();
    }

    /*
     * Redirects user to Assignment Overview
     */
    Intent signUpFinish = new Intent(this, AssignmentOverviewActivity.class);
    startActivity(signUpFinish);
  }

  /** Redirects user to previous sign up page */
  public void signUpTechBack(View view) {
    DatabaseReference r =
        FirebaseDatabase.getInstance()
            .getReference()
            .child("node__user")
            .child(currentUser.getUid());
    r.removeValue();
    currentUser.delete();
    Intent back = new Intent(this, SignUp.class);
    startActivity(back);
  }

  /** Redirects user to login page */
  public void toSignIn(View view) {
    Intent toLogin = new Intent(this, UserLogin.class);
    startActivity(toLogin);
  }
}

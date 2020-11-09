package com.themusicians.musiclms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.themusicians.musiclms.nodeForms.AssignmentCreateFormActivity;
import com.themusicians.musiclms.nodeViews.AssignmentOverviewActivity;

/**
 *
 * ....
 *
 * Contributors: Jerome Lau, Harveer Khangura
 * Created by Jerome Lau on 2020-11-02
 *
 * --------------------------------
 *
 * @todo Open main activity
 * @todo Prompt user for sign in status (teacher/student/login)
 */

public class MainActivity extends AppCompatActivity {
  FirebaseAuth fAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //checks if user is signed in
    fAuth = FirebaseAuth.getInstance();
    if(fAuth.getCurrentUser() != null) {
      startActivity(new Intent(getApplicationContext(), Placeholder.class));
      finish();
    }

    setContentView(R.layout.activity_main);

    // Allows to go to login page ny clicking on text
    TextView textview;
    textview = (TextView)findViewById(R.id.Or_login);
    textview.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent nextPageLogin = new Intent(MainActivity.this, myLogin.class);
        startActivity(nextPageLogin);
      }
    });
  }

  //Redirects to teacher signup
  public void teacherSignup(View view) {
    Intent nextPageT = new Intent(this, signup.class);
    startActivity(nextPageT);
  }

  //Redirects to student signup
  public void studentSignup(View view) {
    Intent nextPageS = new Intent(this, signup.class);
    startActivity(nextPageS);
  }
}
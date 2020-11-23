package com.themusicians.musiclms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.themusicians.musiclms.nodeViews.AssignmentOverviewActivity;

public class MainActivity extends AppCompatActivity {
  private Button button;
  private FirebaseAuth fAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    fAuth = FirebaseAuth.getInstance();

    FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
    if (mFirebaseUser != null) {
      Intent redirectOverview = new Intent(this, AssignmentOverviewActivity.class);
      startActivity(redirectOverview);
    } else {
      Intent redirectSignUp = new Intent(this, SignUp.class);
      startActivity(redirectSignUp);
    }
    setContentView(R.layout.activity_main);
  }
}

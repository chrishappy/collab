package com.themusicians.musiclms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.themusicians.musiclms.nodeForms.AssignmentCreateFormActivity;

public class MainActivity extends AppCompatActivity {
  private Button button;
  private FirebaseAuth fAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    fAuth = FirebaseAuth.getInstance();

    FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
    if (mFirebaseUser != null) {
      Intent redirectOverview = new Intent(this, Placeholder.class);
      startActivity(redirectOverview);
    } else {
      Intent redirectSignUp = new Intent(this, signup.class);
      startActivity(redirectSignUp);
    }
    setContentView(R.layout.activity_main);
  }
}
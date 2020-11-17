package com.themusicians.musiclms;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.themusicians.musiclms.nodeForms.AssignmentCreateFormActivity;
import com.themusicians.musiclms.nodeViews.AssignmentOverviewActivity;

/**
 * ....
 *
 * @contributor Nathan Tsai
 * @author Jerome Lau
 * @since Nov 4, 2020
 *
 * @todo Act as placeholder for main activity page
 */
public class Placeholder extends AppCompatActivity {

  FirebaseAuth fAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Redirect to Assignment Overview Activity
    Intent redirectToAssignment = new Intent(this, AssignmentOverviewActivity.class);
    startActivity(redirectToAssignment);

    Toast.makeText(Placeholder.this, "Using Placeholder class", Toast.LENGTH_SHORT)
        .show();

//    setContentView(R.layout.activity_placeholder);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    fAuth = FirebaseAuth.getInstance();
    switch (item.getItemId()) {
      case R.id.logout:
        fAuth.signOut();
        Intent logout = new Intent(Placeholder.this, myLogin.class);
        startActivity(logout);
        return true;
      case R.id.userprofile:
        Intent toUserProfile = new Intent(Placeholder.this, userProfile.class);
        startActivity(toUserProfile);
        return true;
      case R.id.createassignment:
        Intent toCreateAssignment =
            new Intent(Placeholder.this, AssignmentCreateFormActivity.class);
        startActivity(toCreateAssignment);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}

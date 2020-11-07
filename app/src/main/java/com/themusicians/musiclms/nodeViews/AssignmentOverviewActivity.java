package com.themusicians.musiclms.nodeViews;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.ui.login.signin;

public class AssignmentOverviewActivity extends AppCompatActivity {
  FirebaseAuth fAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_assignment_overview);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
    toolBarLayout.setTitle(getTitle());

//    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//        android.R.layout.simple_list_item_1, myStringArray);

//    ListView listView = (ListView) findViewById(R.id.assignment_overview);
//    listView.setAdapter(adapter);



    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.dropdown_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    fAuth = FirebaseAuth.getInstance();
    switch(item.getItemId()) {
      case R.id.item1:
        fAuth.signOut();
        Intent nextPageLogin = new Intent(AssignmentOverviewActivity.this, signin.class);
        startActivity(nextPageLogin);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
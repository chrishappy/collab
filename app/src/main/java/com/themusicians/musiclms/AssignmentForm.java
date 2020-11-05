package com.themusicians.musiclms;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AssignmentForm extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.assignment_form);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    //Initialize buttons and Edit Texts for form
    Button btnSubmit = (Button) findViewById(R.id.button_submit);
    Button btnSrc = (Button) findViewById(R.id.buttonSrc);
    final EditText name = (EditText) findViewById(R.id.editText1);
    final EditText email = (EditText) findViewById(R.id.editText2);
    final EditText phone = (EditText) findViewById(R.id.editText4);

    //Listener on Submit button
    btnSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent sender = new Intent(AssignmentForm.this, MainActivity.class);
        Bundle b1 = new Bundle(); //Bundle to wrap all data
        b1.putString("name", name.getText().toString()); //Adding data to bundle
        b1.putString("email", email.getText().toString());
        b1.putString("phone", phone.getText().toString());
        sender.putExtras(b1); //putExtras method to send the bundle
        startActivity(sender);
        AssignmentForm.this.finish(); //Finish form activity to remove it from stack
      }
    });

    //Listener on source button
    btnSrc.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent j = new Intent(AssignmentForm.this, MainActivity.class);
        startActivity(j);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {

      AssignmentForm.this.finish();
      return true;
    }
    return super.onOptionsItemSelected(item);


  }
}

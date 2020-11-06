package com.themusicians.musiclms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.themusicians.musiclms.ui.login.signin;

public class Assignments_Overview extends AppCompatActivity {
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments__overview);
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
                Intent nextPageLogin = new Intent(Assignments_Overview.this, signin.class);
                startActivity(nextPageLogin);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
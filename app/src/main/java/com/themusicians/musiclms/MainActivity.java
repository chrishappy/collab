package com.themusicians.musiclms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.themusicians.musiclms.ui.login.signin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Allows to go to login page ny clicking on text
        TextView textview;
        textview = (TextView)findViewById(R.id.Or_login);
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextPageLogin = new Intent(MainActivity.this, signin.class);
                startActivity(nextPageLogin);
            }
        });

    }

    public void teacherSignup(View view) {
        Intent nextPageT = new Intent(this, signup.class);
        startActivity(nextPageT);
    }

    public void studentSignup(View view) {
        Intent nextPageS = new Intent(this, signup.class);
        startActivity(nextPageS);
    }
}
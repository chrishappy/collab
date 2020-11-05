package com.themusicians.musiclms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
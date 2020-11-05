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

public class MainActivity extends AppCompatActivity {
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
    public void orLogin(View view) {
        TextView textview = findViewById(R.id.Or_login);
        String text = "or Login";
        SpannableString ss1 = new SpannableString(text);

        ClickableSpan clickablespanLogin = new ClickableSpan(){
            @Override
            public void onClick(@NonNull View view) {
                Toast.makeText(MainActivity.this,"one",Toast.LENGTH_SHORT);
            }
        };
        ss1.setSpan(clickablespanLogin,3,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(ss1);
        textview.setMovementMethod(LinkMovementMethod.getInstance());

    }

}
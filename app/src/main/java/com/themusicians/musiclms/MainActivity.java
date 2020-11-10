package com.themusicians.musiclms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.themusicians.musiclms.nodeForms.AssignmentCreateFormActivity;
import com.themusicians.musiclms.nodeViews.AssignmentOverviewActivity;
import com.themusicians.musiclms.ui.login.signin;

import java.sql.BatchUpdateException;

public class MainActivity extends AppCompatActivity {
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        button = (Button) findViewById(R.id.assignment_overview_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openActivity_assignment_create_form();
//            }
//        });

        // Temporary to test Assignment Page
//        Intent redirectToAssignment = new Intent(this, AssignmentOverviewActivity.class);
//        startActivity(redirectToAssignment);





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

    public void openActivity_assignment_create_form(){
        Intent intent = new Intent(this,AssignmentCreateFormActivity.class);
        startActivity(intent);
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
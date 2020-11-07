package com.themusicians.musiclms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.themusicians.musiclms.entity.Node.Assignment;
import com.themusicians.musiclms.entity.Node.UserProfile;
import com.themusicians.musiclms.nodeViews.AssignmentOverviewActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class signup extends AppCompatActivity {
    EditText newEmail, newPassword, newName;
    Button next;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        newEmail = findViewById(R.id.newEmail);
        newPassword = findViewById(R.id.newPassword);
        fAuth = FirebaseAuth.getInstance();
        next = findViewById(R.id.signup_next);

        //check if user is signed in
        if(fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), AssignmentOverviewActivity.class));
            finish();
        }

        //when the next button is clicked
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = newEmail.getText().toString().trim();
                String password = newPassword.getText().toString().trim();

                //checks if email is empty
                if(TextUtils.isEmpty(email)) {
                    newEmail.setError("Email is Required.");
                    return;
                }

                //checks if password is empty
                if(TextUtils.isEmpty(password)) {
                    newPassword.setError("Password is Required");
                    return;
                }

                //checks for password minimum length
                if(password.length() < 6){
                    newPassword.setError("Password must be more than 5 characters");
                    return;
                }

                //registers account to firebase and sends to next screen
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(signup.this, "User Created", Toast.LENGTH_SHORT).show();
                            setContentView(R.layout.signup_details);
                        }else {
                            Toast.makeText(signup.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void signUpDetailsNext(View view){
        newName = findViewById(R.id.newName);
        String name = newName.getText().toString().trim();
        if(TextUtils.isEmpty(name)) {
            newName.setError("Name is Required");
            return;
        }

        setContentView(R.layout.signup_tech);
    }

    //Sign up tech
    public void signUpFinish(View view) {
        Intent signupFinish = new Intent(this, AssignmentOverviewActivity.class);
        startActivity(signupFinish);
    }
}
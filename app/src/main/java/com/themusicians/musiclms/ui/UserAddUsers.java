package com.themusicians.musiclms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.themusicians.musiclms.R;
import com.themusicians.musiclms.UserProfile;
import com.themusicians.musiclms.UserSearch;
import com.themusicians.musiclms.entity.Node.User;

public class UserAddUsers<button> extends AppCompatActivity {

  // for add_students_teachers page the tabs
  protected TabLayout tabLayout;
  protected TabItem tabAccounts;
  protected TabItem usersTab;
  protected ViewPager viewpager1;

  //
  protected User currUser;
  protected TextView myName;
  protected FirebaseUser currentUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.user_add_main);
    myName = findViewById(R.id.name);
    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    // display name
    currUser = new User(currentUser.getUid());
    DatabaseReference reference =
        FirebaseDatabase.getInstance()
            .getReference()
            .child("node__user")
            .child(currentUser.getUid());
    reference.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            Object name = snapshot.child("name").getValue();
            /** Checks if names exists */
            if (name != null) {
              myName.setText(name.toString());
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });
    // tab layout
    tabLayout = findViewById(R.id.accountUsersTab);
    tabAccounts = findViewById(R.id.accTab);
    usersTab = findViewById(R.id.usersTab);
    viewpager1 = findViewById(R.id.viewpager1);

    PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

    viewpager1.setAdapter(adapter);

    tabLayout.addOnTabSelectedListener(
        new TabLayout.OnTabSelectedListener() {
          @Override
          public void onTabSelected(TabLayout.Tab tab) {
            viewpager1.setCurrentItem(tab.getPosition());
          }

          @Override
          public void onTabUnselected(TabLayout.Tab tab) {}

          @Override
          public void onTabReselected(TabLayout.Tab tab) {}
        });
  }
  /** Redirects to search for teachers */
  public void toSearchTeachers(View view) {
    Intent toSearch = new Intent(this, UserSearch.class);
    startActivity(toSearch);
  }

  /** Redirects back to User Profile */
  public void backUserMain(View view) {
    Intent toUserProfile = new Intent(this, UserProfile.class);
    startActivity(toUserProfile);
  }
}

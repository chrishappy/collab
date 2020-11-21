package com.themusicians.musiclms.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.themusicians.musiclms.R;

public class UserAddUsers extends AppCompatActivity {

  protected TabLayout tabLayout;
  protected TabItem tabAccounts;
  protected TabItem tabTeachers;
  protected ViewPager viewpager1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_add_main);

        tabLayout = findViewById(R.id.accountTeachersTab);
        tabAccounts = findViewById(R.id.accTab);
        tabTeachers = findViewById(R.id.teachersTab);
        viewpager1 = findViewById(R.id.viewpager1);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        viewpager1.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager1.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

  }
}
package com.themusicians.musiclms.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.themusicians.musiclms.R;

public class add_students_teachers extends AppCompatActivity {


    // for add_students_teachers page the tabs
    protected TabLayout tabLayout;
    protected TabItem tabAccounts;
    protected TabItem tabTeachers;
    protected ViewPager2 viewpager1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setTitle("Add teachers");
        setContentView(R.layout.add_students_teachers);

        tabLayout = findViewById(R.id.accountTeachersTab);
        tabAccounts = findViewById(R.id.accTab);
        tabTeachers = findViewById(R.id.teachersTab);
        viewpager1 = findViewById(R.id.viewpager1);

        PagerAdapter Adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        viewpager1.setAdapter(Adapter);

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
*/
    }



}

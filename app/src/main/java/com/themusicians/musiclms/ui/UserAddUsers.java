package com.themusicians.musiclms.ui;

<<<<<<< HEAD:app/src/main/java/com/themusicians/musiclms/ui/UserAddUsers.java
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
=======
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
>>>>>>> origin/userProfile:app/src/main/java/com/themusicians/musiclms/ui/UserAdd.java

import com.themusicians.musiclms.R;

<<<<<<< HEAD:app/src/main/java/com/themusicians/musiclms/ui/UserAddUsers.java
public class UserAddUsers extends AppCompatActivity {

  protected TabLayout tabLayout;
  protected TabItem tabAccounts;
  protected TabItem tabTeachers;
  protected ViewPager viewpager1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_add_main);

=======
public class UserAdd extends AppCompatActivity {

/*
    // for add_students_teachers page the tabs
    protected TabLayout tabLayout;
    protected TabItem tabAccounts;
    protected TabItem tabTeachers;
    protected ViewPager viewpager1;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle("Add teachers");
        setContentView(R.layout.user_add_main);
/*
>>>>>>> origin/userProfile:app/src/main/java/com/themusicians/musiclms/ui/UserAdd.java
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
<<<<<<< HEAD:app/src/main/java/com/themusicians/musiclms/ui/UserAddUsers.java

  }
}
=======
*/
    }
}
>>>>>>> origin/userProfile:app/src/main/java/com/themusicians/musiclms/ui/UserAdd.java

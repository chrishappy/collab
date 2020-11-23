package com.themusicians.musiclms.ui;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.themusicians.musiclms.Accountfragment;

public class PagerAdapter extends FragmentPagerAdapter {

    protected int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs){

        super(fm);
        this.numOfTabs = numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new Accountfragment();
            case 1:
                return new UserFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {

        return numOfTabs;
    }
}

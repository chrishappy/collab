package com.themusicians.musiclms.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.themusicians.musiclms.accountfragment;

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
                return new accountfragment();
            case 1:
                return new teachersFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {

        return numOfTabs;
    }
}

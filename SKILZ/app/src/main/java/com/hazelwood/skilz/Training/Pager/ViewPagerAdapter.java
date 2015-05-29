package com.hazelwood.skilz.Training.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hazelwood.skilz.Training.TrainingSections.Athlete_Fragment;
import com.hazelwood.skilz.Training.TrainingSections.Guide_Fragment;

import java.util.ArrayList;

/**
 * Created by Hazelwood on 4/25/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    String sport_;
    ArrayList<String> skills_;

    public ViewPagerAdapter(FragmentManager fm, String string, ArrayList<String> strings) {
        super(fm);
        sport_ = string;
        skills_ = strings;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new Fragment();
        switch (i){
            case 0:
                fragment = Guide_Fragment.newInstance(sport_, skills_);
                break;
            case 1:
                fragment = Athlete_Fragment.newInstance(sport_, skills_);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "TITLE";
        switch (position){
            case 0:
                title = "FEED";
                break;
            case 1:
                title = "ATHLETES";
                break;
        }
        return title;
    }
}

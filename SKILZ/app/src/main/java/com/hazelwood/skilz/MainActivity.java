package com.hazelwood.skilz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hazelwood.skilz.Setup.SetupActivity;
import com.hazelwood.skilz.Training.TrainingSections.Training_Fragment;
import com.parse.Parse;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        Training_Fragment.Listener, Nutrition_Fragment.Listener, Challenge_Fragment.Listener,TrophyFragment.Listener, Bookmark_Fragment.Listener{

    private static final String PREF_USER_DID_SETUP = "setup_complete";
    SharedPreferences sp;

    private static final String TAG = "MAINACTIVITYTAG";

    private boolean mUserCompletedSetup;
    private boolean mFromSavedInstanceState;

    ArrayList<String> skills_;
    String sport_;

    android.app.FragmentManager fragmentManager;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Parse.initialize(this, "zt1QXsnVxEz5nNaOXkls8jrEgSoVvI3T2LIK17rc", "RYgybo6poSSSTuTn0JL7GFmuRsOp7vz8PHOeCiHH");

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mUserCompletedSetup = sp.getBoolean(PREF_USER_DID_SETUP, false);

        sport_ = "";
        skills_ = new ArrayList<>();

        if (!mUserCompletedSetup){
            Intent intent = new Intent(this, SetupActivity.class);
            startActivity(intent);

        } else {
            Set<String> set = sp.getStringSet("SKILLS", null);
            String sport = sp.getString("SPORT", null);

            if (!sport.equals(null) && !set.equals(null)){
                skills_ = new ArrayList<>(set);
                sport_ = sport;
            }

        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        fragmentManager = getFragmentManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sport_ = "";
        skills_ = new ArrayList<>();

        if (!mUserCompletedSetup){
            Intent intent = new Intent(this, SetupActivity.class);
            startActivity(intent);

        } else {
            Set<String> set = sp.getStringSet("SKILLS", null);
            String sport = sp.getString("SPORT", null);

            if (!sport.equals(null) && !set.equals(null)){
                skills_ = new ArrayList<>(set);
                sport_ = sport;
            }

        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();


    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                fragmentManager.beginTransaction().replace(R.id.container, Training_Fragment.newInstance(sport_, skills_), TAG).commit();
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                fragmentManager.beginTransaction().replace(R.id.container, Nutrition_Fragment.newInstance(sport_, skills_), TAG).commit();
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                fragmentManager.beginTransaction().replace(R.id.container, Challenge_Fragment.newInstance(sport_, skills_), TAG).commit();
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                fragmentManager.beginTransaction().replace(R.id.container, TrophyFragment.newInstance("TROPHY")).commit();
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                fragmentManager.beginTransaction().replace(R.id.container, Bookmark_Fragment.newInstance(mTitle.toString()), TAG).commit();
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                fragmentManager.beginTransaction().replace(R.id.container, new Preference_Fragment()).commit();
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d(TAG, "CLICK");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getURL(String bob) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));

            Bundle argument = getArguments();
            int section = argument.getInt(ARG_SECTION_NUMBER);

            switch (section){
                case 1:
                    Log.d(TAG, "Training");
                    break;
                case 2:
                    Log.d(TAG, "Nutrition");
                    break;
                case 3:
                    Log.d(TAG, "Challenge");
                    break;
                case 4:
                    Log.d(TAG, "Medals");
                    break;
                case 5:
                    Log.d(TAG, "Bookmark");
                    break;
                case 6:
                    Log.d(TAG, "Setting");
                    break;
            }
        }
    }

}

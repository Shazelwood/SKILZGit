package com.hazelwood.skilzadmin;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.hazelwood.skilzadmin.Fragments.Article_Fragment;
import com.hazelwood.skilzadmin.Fragments.Athlete_Fragment;
import com.hazelwood.skilzadmin.Fragments.Challenge_Fragment;
import com.hazelwood.skilzadmin.Fragments.Nutrition_Fragment;


public class FormActivity extends ActionBarActivity implements
        Article_Fragment.OnFragmentInteractionListener,
        Athlete_Fragment.OnFragmentInteractionListener,
        Challenge_Fragment.OnFragmentInteractionListener,
        Nutrition_Fragment.OnFragmentInteractionListener {
    android.app.FragmentManager fragmentManager;

    public static final String TAG = "FORM_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        int typeInt = getIntent().getIntExtra("type", 0);
        fragmentManager = getFragmentManager();

        switch (typeInt){
            //Nutrition
            case 10:
                fragmentManager.beginTransaction().replace(R.id.form_container, Nutrition_Fragment.newInstance("data"), TAG).commit();
                break;
            //Article
            case 20:
                fragmentManager.beginTransaction().replace(R.id.form_container, Article_Fragment.newInstance("data"), TAG).commit();
                break;
            //Challenge
            case 30:
                fragmentManager.beginTransaction().replace(R.id.form_container, Challenge_Fragment.newInstance("data"), TAG).commit();
                break;
            //Athlete
            case 40:
                fragmentManager.beginTransaction().replace(R.id.form_container, Athlete_Fragment.newInstance("data"), TAG).commit();
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}

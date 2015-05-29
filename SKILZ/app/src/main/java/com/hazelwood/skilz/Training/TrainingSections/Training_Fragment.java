package com.hazelwood.skilz.Training.TrainingSections;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hazelwood.skilz.MainActivity;
import com.hazelwood.skilz.R;
import com.hazelwood.skilz.Training.Pager.ViewPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Hazelwood on 4/24/15.
 */
public class Training_Fragment extends Fragment {

    ViewPager pager;
    ViewPagerAdapter adapter;

    MainActivity mContext;

    public static final String TAG = "CHALLENGE";

    public Training_Fragment(){

    }

    public interface Listener{
        public void getURL(String bob);
    }

    public Listener mListener;

    public static Training_Fragment newInstance(String sports_, ArrayList<String> skills_){
        Training_Fragment fragment = new Training_Fragment();
        if (sports_ == null){
            Log.d(TAG, "No data");

        } else {
            Bundle args = new Bundle();
            args.putString("SPORT", sports_);
            args.putStringArrayList("SKILLS", skills_);

            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Listener){
            mListener = (Listener) activity;
        } else {
            throw new IllegalArgumentException("");
        }

        mContext = (MainActivity)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.training_fragment_layout, container, false);
        return fragView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fragmentManager = mContext.getSupportFragmentManager();
        if (getArguments().containsKey("SKILLS")){
            ArrayList<String> skills = getArguments().getStringArrayList("SKILLS");
            String sport = getArguments().getString("SPORT");

            pager = (ViewPager)getActivity().findViewById(R.id.pager);
            adapter = new ViewPagerAdapter(fragmentManager, sport, skills);

            pager.setAdapter(adapter);
        }



    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

}

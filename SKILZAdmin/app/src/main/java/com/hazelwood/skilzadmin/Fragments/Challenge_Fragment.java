package com.hazelwood.skilzadmin.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.hazelwood.skilzadmin.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class Challenge_Fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    public static final String TAG = "CHALLENGE";

    EditText et_challengeTitle, et_challengeLink, et_challengeDescription;
    Spinner spinnerPoints, spinnerLevel;
    Button skills_btn, sports_btn;
    ArrayList<String> skills_selected, sports_selected;

    private String mParam1;

    private OnFragmentInteractionListener mListener;

    public static Challenge_Fragment newInstance(String param1) {
        Challenge_Fragment fragment = new Challenge_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public Challenge_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_form, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                saveAll();

                return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_challenge, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Add new Challenge");
        et_challengeTitle = (EditText) getActivity().findViewById(R.id.challenge_title);
        et_challengeDescription = (EditText) getActivity().findViewById(R.id.challenge_description);
        et_challengeLink = (EditText) getActivity().findViewById(R.id.challenge_link);

        spinnerLevel = (Spinner)getActivity().findViewById(R.id.challenge_spinner_level);
        spinnerPoints = (Spinner) getActivity().findViewById(R.id.challenge_spinner_type);

        skills_btn = (Button)getActivity().findViewById(R.id.challenge_button_skills);
        sports_btn = (Button)getActivity().findViewById(R.id.challenge_button_sports);

        skills_btn.setOnClickListener(showChecklist);
        sports_btn.setOnClickListener(showChecklist);

        skills_selected = new ArrayList<>();
        sports_selected = new ArrayList<>();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.challenge_level, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.challenge_points, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPoints.setAdapter(adapter);
    }

    View.OnClickListener showChecklist = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.challenge_button_skills:
                    showChecklistDialog(R.array.skills_array, 1);
                    break;
                case R.id.challenge_button_sports:
                    showChecklistDialog(R.array.sports_array, 2);
                    break;
            }

        }
    };

    public void showChecklistDialog(final int array, final int arrayID){
        final String[] strings = getResources().getStringArray(array);

        final ArrayList<Integer> selectedItems=new ArrayList<>();

        new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                .setTitle("Make a selection")
                .setMultiChoiceItems(array, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {

                                if (isChecked) {
                                    selectedItems.add(which);
                                } else if (selectedItems.contains(which)) {

                                    selectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        switch (arrayID) {
                            case 1:
                                for (int i = 0; i < selectedItems.size(); i++) {
                                    skills_selected.add(strings[selectedItems.get(i)]);
                                }
                                break;
                            case 2:
                                for (int i = 0; i < selectedItems.size(); i++) {
                                    sports_selected.add(strings[selectedItems.get(i)]);
                                }
                                break;
                        }
                        selectedItems.clear();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .show();
    }

    public void saveAll(){
        ParseObject parseObject = new ParseObject("Challenge");

        String title_ = et_challengeTitle.getText().toString();
        String link_ = et_challengeLink.getText().toString();
        String description_ = et_challengeDescription.getText().toString();
        String level_ = spinnerLevel.getSelectedItem().toString();
        String points_ = spinnerPoints.getSelectedItem().toString();
        ArrayList<String> skills_ = skills_selected;
        ArrayList<String> sports_ = sports_selected;

        Log.d(TAG, points_);

        parseObject.put("title", title_);
        parseObject.put("link", link_);
        parseObject.put("points", points_);
        parseObject.put("level", level_);
        parseObject.put("description", description_);
        parseObject.put("skills", skills_);
        parseObject.put("sports", sports_);

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                getActivity().finish();
            }
        });


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}

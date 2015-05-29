package com.hazelwood.skilz;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Hazelwood on 4/27/15.
 */
public class Challenge_Fragment extends Fragment {

    ListView listView;
    ListAdapter adapter;
    View v;
    TextView pointsTV;

    MainActivity mContext;

    String sport;
    ArrayList<String> skills;
    ArrayList<Challenge> arrayList_challenge;
    Set<String> set;
    ArrayList<String> checkList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences sp;
    int totalPoints;


    public static final String TAG = "Guide_Frag";
    private boolean mUserAgreement;
    public static final String PREF_USER_DID_AGREE = "AGREEMENT_TO_CHALLENGE";

    public Challenge_Fragment (){

    }

    public interface Listener{
        public void getURL(String bob);
    }

    public Listener mListener;

    public static Challenge_Fragment newInstance(String sports_, ArrayList<String> skills_){
        Challenge_Fragment fragment = new Challenge_Fragment();
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
        View fragView = inflater.inflate(R.layout.fragment_challenge, container, false);
        v = fragView;
        return fragView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        totalPoints = sp.getInt("SKILLPOINTS",0);
        pointsTV = (TextView)getActivity().findViewById(R.id.challenge_skill_points);
        pointsTV.setText("Skill Points: " + String.valueOf(totalPoints));

        mUserAgreement = sp.getBoolean(PREF_USER_DID_AGREE, false);

        if (!mUserAgreement){
            showDialog();
        }

        sport = getArguments().getString("SPORT");
        skills = getArguments().getStringArrayList("SKILLS");
        arrayList_challenge = new ArrayList<>();
        checkList = getChallengesArray(getActivity());
        //Check if challenge is added already

        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.challenge_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.grey, R.color.red);
        mSwipeRefreshLayout.setOnRefreshListener(refresh);

        listView = (ListView)v.findViewById(R.id.challenge_list_view);
        adapter = new ListAdapter(getActivity(), arrayList_challenge);
        listView.setAdapter(adapter);
        listView.setDividerHeight(10);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChallengeActivity.class);

                intent.putExtra("TITLE", arrayList_challenge.get(position).getChallenge_title());
                intent.putExtra("DESCRIPTION", arrayList_challenge.get(position).getChallenge_description());
                intent.putExtra("LEVEL", arrayList_challenge.get(position).getChallenge_level());
                intent.putExtra("LINK", arrayList_challenge.get(position).getChallenge_link());
                intent.putExtra("POINTS", arrayList_challenge.get(position).getChallenge_points());

                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(), view, "challengeView");
                // start the new activity
                startActivity(intent, options.toBundle());
            }
        });
        //TODO USE SAVE STRING[] TO PREFERENCES TO CHECK IF ACCEPTED
    }

    SwipeRefreshLayout.OnRefreshListener refresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            arrayList_challenge.clear();
            adapter.notifyDataSetChanged();
            onResume();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (arrayList_challenge.isEmpty()){
            if (SkilzObj.isConnected(getActivity())){
                getChallenges();
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Connection Problem.");
                builder1.setMessage("There is a probblem with your network connection.");
                builder1.setCancelable(true);
                builder1.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }
        checkList = getChallengesArray(getActivity());
        if (arrayList_challenge.size() !=0){
            adapter = new ListAdapter(getActivity(), arrayList_challenge);
            listView.setAdapter(adapter);
            totalPoints = sp.getInt("SKILLPOINTS",0);
            pointsTV.setText("Skill Points: " + String.valueOf(totalPoints));
            adapter.notifyDataSetChanged();
        }

    }

    private class Challenge{

        String challenge_title, challenge_description, challenge_level,challenge_link, challenge_points;

        public Challenge(String _title, String _desc, String _level, String _link, String _points){
            this.challenge_description = _desc;
            this.challenge_level = _level;
            this.challenge_link = _link;
            this.challenge_title = _title;
            this.challenge_points = _points;
        }

        public Challenge(){

        }

        public String getChallenge_points() {
            return challenge_points;
        }

        public String getChallenge_description() {
            return challenge_description;
        }

        public String getChallenge_level() {
            return challenge_level;
        }

        public String getChallenge_link() {
            return challenge_link;
        }

        public String getChallenge_title() {
            return challenge_title;
        }

        public void setChallenge_points(String challenge_points) {
            this.challenge_points = challenge_points;
        }

        public void setChallenge_description(String challenge_description) {
            this.challenge_description = challenge_description;
        }

        public void setChallenge_level(String challenge_level) {
            this.challenge_level = challenge_level;
        }

        public void setChallenge_link(String challenge_link) {
            this.challenge_link = challenge_link;
        }

        public void setChallenge_title(String challenge_title) {
            this.challenge_title = challenge_title;
        }
    }
    public static ArrayList<String> getChallengesArray(Context context) {
        ArrayList<String> arrayList = new ArrayList<String>();

        try {
            File extFolder = context.getExternalFilesDir(null);
            File file = new File(extFolder, "CHALLENGES.dat");

            if (file.exists()) {
                FileInputStream fin = new FileInputStream(file);
                ObjectInputStream oin = new ObjectInputStream(fin);
                arrayList = (ArrayList<String>) oin.readObject();
                oin.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return arrayList;
    }
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog characteristics
        //TODO GET THE USER TO REALLY MAKE A DECISION HERE. NEVER SHOW MESSAGE AGAIN OR SET BOOLEAN TO SHOW EVERY TIME

        builder.setMessage("This is where we test your skills. Only you can make improvements based on the work that you put in, so whether you complete them to the fullest is on you. Be CAREFUL! Don't hurt yourself. KNOW your limits. USE the GUIDES and ATHLETES to improve on your skills. USE the NUTRITION feeds")
                .setTitle("GOT SKILLS?")
                .setNegativeButton("DON'T SHOW ANYMORE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCancelable(false);


        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

//            SharedPreferences.Editor edit = sp.edit();
//            edit.putBoolean(PREF_USER_DID_AGREE, Boolean.TRUE);
//            edit.apply();
    }
    private class ListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<Challenge> arrayList;

        private static final long ID_CONSTANT = 123456789;

        public ListAdapter(Context c, ArrayList<Challenge> array){
            context = c;
            arrayList = array;
        }
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Challenge getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ID_CONSTANT + position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Challenge item = getItem(position);
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_1, parent, false);
                convertView.setBackgroundColor(Color.WHITE);
            }

            String level = item.getChallenge_level();
            String points = item.getChallenge_points();

            TextView points_TV = (TextView) convertView.findViewById(R.id.challenge_points);

            View view = (View)convertView.findViewById(R.id.medalIndicator);

            if(level.equalsIgnoreCase("gold")) {
                view.setBackgroundColor(getResources().getColor(R.color.gold));
                points_TV.setText(points);
            }
            else if (level.equalsIgnoreCase("silver")) {
                view.setBackgroundColor(getResources().getColor(R.color.silver));
                points_TV.setText(points);
            }
            else if (level.equalsIgnoreCase("bronze")) {
                view.setBackgroundColor(getResources().getColor(R.color.bronze));
                points_TV.setText(points);
            }

            if (checkList.contains(item.getChallenge_title())){
                view.setBackgroundColor(getResources().getColor(R.color.green));
                //TODO add all the points
                //totalPoints += item.challenge_points;
            }

            TextView title = (TextView) convertView.findViewById(R.id.challenge_title);
            title.setText(item.getChallenge_title());

            return convertView;
        }
    }
    private void getChallenges(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Challenge");
        query.whereContainedIn("skills", skills);
        query.whereEqualTo("sports", sport);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++){
                        final String challengeTitle = objects.get(i).getString("title");
                        final String challengeLink = objects.get(i).getString("link");
                        final String challengeLevel = objects.get(i).getString("level");
                        final String challengeDesc = objects.get(i).getString("description");
                        final String challengePoints = objects.get(i).getString("points");
                        final String objectId = objects.get(i).getObjectId();

                        Log.d(TAG, challengeTitle);

                        arrayList_challenge.add(new Challenge(challengeTitle, challengeDesc, challengeLevel, challengeLink, challengePoints));
                        adapter.notifyDataSetChanged();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

}

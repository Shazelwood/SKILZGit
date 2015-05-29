package com.hazelwood.skilz.Training.TrainingSections;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hazelwood.skilz.SkilzObj;
import com.hazelwood.skilz.Detail.Detail_Activity;
import com.hazelwood.skilz.R;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hazelwood on 4/25/15.
 */
public class Athlete_Fragment extends Fragment {

    GridView gridView;
    GridAdapter adapter;
    View v;

    String sport;
    ArrayList<String> skills;
    ArrayList<SkilzObj> arrayList_athlete;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public static final String TAG = "Athlete";

    public Athlete_Fragment (){

    }

    public static Athlete_Fragment newInstance(String sports_, ArrayList<String> skills_){
        Athlete_Fragment fragment = new Athlete_Fragment();
        if (sports_ == null){
            Log.d(TAG, "No data");

        } else {
            Bundle args = new Bundle();
            args.putStringArrayList("SKILLS", skills_);
            args.putString("SPORT", sports_);

            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_athlete, container, false);
        v = fragView;
        return fragView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sport = getArguments().getString("SPORT");
        skills = getArguments().getStringArrayList("SKILLS");
        arrayList_athlete = new ArrayList<>();

        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.athlete_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.grey, R.color.red);
        mSwipeRefreshLayout.setOnRefreshListener(refresh);

        gridView = (GridView)v.findViewById(R.id.gridView);
        adapter = new GridAdapter(getActivity(), arrayList_athlete);
        gridView.setAdapter(adapter);
        gridView.setBackgroundResource(R.color.whitish);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Detail_Activity.class);
                String objectID = arrayList_athlete.get(position).getObjectId();
                String className = arrayList_athlete.get(position).getClassName();
                String subClassName = arrayList_athlete.get(position).getSubClassName();

                intent.putExtra("ID", objectID);
                intent.putExtra("Class", className);
                intent.putExtra("SubClass", subClassName);
                intent.putExtra("TYPE", "athlete");

                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(), view, "guideTransition");
                // start the new activity

                getActivity().startActivity(intent, options.toBundle());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (arrayList_athlete.isEmpty()){
            if (SkilzObj.isConnected(getActivity())){
                getAthletes();
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Connection Problem.");
                builder1.setMessage("You are not connected to wifi.");
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    SwipeRefreshLayout.OnRefreshListener refresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            arrayList_athlete.clear();
            adapter.notifyDataSetChanged();
            getAthletes();
        }
    };

    private class GridAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<SkilzObj> arrayList;

        private static final long ID_CONSTANT = 123456789;

        public GridAdapter(Context c, ArrayList<SkilzObj> array){
            context = c;
            arrayList = array;
        }
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public SkilzObj getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ID_CONSTANT + position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
            }

            SkilzObj item = getItem(position);

            ImageView imageView = (ImageView)convertView.findViewById(R.id.grid_image_view);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length));
            TextView textView = (TextView)convertView.findViewById(R.id.grid_text_view);
            textView.setText(item.getPrimaryString());

            return convertView;
        }
    }
    private void getAthletes(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Athlete");
        query.whereContainedIn("skills", skills);
        query.whereEqualTo("sports", sport);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++){
                        final String fName = objects.get(i).getString("firstName");
                        final String lName = objects.get(i).getString("lastName");
                        final String socialName = objects.get(i).getString("socialName");
                        final String bio = objects.get(i).getString("bio");
                        final String objectId = objects.get(i).getObjectId();
                        final boolean video = objects.get(i).getBoolean("hasVideo");

                        Log.d(TAG, fName);

                        ParseFile imageFile = (ParseFile)objects.get(i).get("imageFile");
                        imageFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                arrayList_athlete.add(new SkilzObj(objectId, fName + " " + lName, socialName, bio, bytes, video, "", "Athlete", "AthleteItems"));
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
}

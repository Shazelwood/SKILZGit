package com.hazelwood.skilz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.util.ArrayList;

/**
 * Created by Hazelwood on 5/28/15.
 */
public class TrophyFragment extends Fragment {
    GridView gridView;
    GridAdapter adapter;
    View v;

    String sport;
    ArrayList<String> skills;
    ArrayList<SkilzObj> arrayList_athlete;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences sp;
    int totalPoints;
    MainActivity mContext;

    public interface Listener{
        public void getURL(String bob);
    }

    public Listener mListener;

    public static final String TAG = "Athlete";

    public TrophyFragment (){

    }

    public static TrophyFragment newInstance(String trophy_){
        TrophyFragment fragment = new TrophyFragment();
        if (trophy_ == null){
            Log.d(TAG, "No data");

        } else {
            Bundle args = new Bundle();
            args.putString("SPORT", trophy_);

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
        View fragView = inflater.inflate(R.layout.fragment_athlete, container, false);
        v = fragView;
        return fragView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        totalPoints = sp.getInt("SKILLPOINTS",0);

        final ArrayList<Trophy> trophyArrayList = new ArrayList<Trophy>();
        trophyArrayList.add(new Trophy("Contract", "Description", 1, R.drawable.medal3));
        trophyArrayList.add(new Trophy("First Star", "Description", 10, R.drawable.medal4));
        trophyArrayList.add(new Trophy("Participation Award", "Description", 20, R.drawable.medal6));
        trophyArrayList.add(new Trophy("Gold coin", "Description", 30, R.drawable.medal5));
        trophyArrayList.add(new Trophy("First Medal", "Description", 100, R.drawable.medal2));
        trophyArrayList.add(new Trophy("Gold Medal", "Description", 200, R.drawable.medal7));
        trophyArrayList.add(new Trophy("Olympic Medal", "Description", 500, R.drawable.medal1));
        trophyArrayList.add(new Trophy("Olympic Reef", "Description", 1000, R.drawable.medal9));
        trophyArrayList.add(new Trophy("Golden Reef", "Description", 2000, R.drawable.medal8));
        trophyArrayList.add(new Trophy("Gold Crown", "Description", 5000, R.drawable.medal10));




        arrayList_athlete = new ArrayList<>();

        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.athlete_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.grey, R.color.red);
        mSwipeRefreshLayout.setOnRefreshListener(refresh);

        gridView = (GridView)v.findViewById(R.id.gridView);
        gridView.setBackgroundResource(R.color.whitish);
        adapter = new GridAdapter(getActivity(), trophyArrayList);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(2);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (trophyArrayList.get(position).getTrophy_points() > totalPoints){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setIcon(trophyArrayList.get(position).getTrophy_image());
                    builder1.setTitle(trophyArrayList.get(position).getTrophy_title());
                    builder1.setMessage("You need " + (trophyArrayList.get(position).getTrophy_points() - totalPoints) + " skill points to get this medal.");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else {
                    Uri path = Uri.parse("android.resource://com.hazelwood.skilz/" + trophyArrayList.get(position).getTrophy_image());
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "I just received " + trophyArrayList.get(position).getTrophy_title() + " from SKILZ @OfficialSKILZ #IGOTSKILZ");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, path);
                    shareIntent.setType("image/jpeg");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "Where do you want to share your medal to?"));
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (arrayList_athlete.isEmpty()){
            if (SkilzObj.isConnected(getActivity())){
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
        }
    };

    private class GridAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<Trophy> arrayList;

        private static final long ID_CONSTANT = 123456789;

        public GridAdapter(Context c, ArrayList<Trophy> array){
            context = c;
            arrayList = array;
        }
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Trophy getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ID_CONSTANT + position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Trophy item = getItem(position);
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
            }

            ImageView imageView = (ImageView)convertView.findViewById(R.id.grid_image_view);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageResource(item.getTrophy_image());
            TextView textView = (TextView)convertView.findViewById(R.id.grid_text_view);
            textView.setText(item.getTrophy_title());

            return convertView;
        }
    }
}

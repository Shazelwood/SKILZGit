package com.hazelwood.skilz;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hazelwood.skilz.Detail.Detail_Activity;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hazelwood on 4/27/15.
 */
public class Nutrition_Fragment extends Fragment {

    MainActivity mContext;

    ListView listView;
    ListAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    String sport;
    ArrayList<String> skills;
    ArrayList<SkilzObj> arrayList_nutrition;
    View v;


    public static final String TAG = "Guide_Frag";

    public Nutrition_Fragment (){

    }

    public interface Listener{
        public void getURL(String bob);
    }

    public Listener mListener;

    public static Nutrition_Fragment newInstance(String sports_, ArrayList<String> skills_){
        Nutrition_Fragment fragment = new Nutrition_Fragment();
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
        View fragView = inflater.inflate(R.layout.fragment_guide, container, false);
        v = fragView;
        return fragView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        arrayList_nutrition = new ArrayList<>();

        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.guide_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.grey, R.color.red);
        mSwipeRefreshLayout.setOnRefreshListener(refresh);


        sport = getArguments().getString("SPORT");
        skills = getArguments().getStringArrayList("SKILLS");
        Log.d(TAG, sport + " " + skills.get(0));

        listView = (ListView)v.findViewById(R.id.list_view);
        adapter = new ListAdapter(getActivity(), arrayList_nutrition);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Detail_Activity.class);
                String objectID = arrayList_nutrition.get(position).getObjectId();
                String className = arrayList_nutrition.get(position).getClassName();
                String subClassName = arrayList_nutrition.get(position).getSubClassName();

                intent.putExtra("ID", objectID);
                intent.putExtra("Class", className);
                intent.putExtra("SubClass", subClassName);

                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(getActivity(), view, "guideTransition");

                getActivity().startActivity(intent, options.toBundle());
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        if (arrayList_nutrition.isEmpty()){
            if (SkilzObj.isConnected(getActivity())){
                getNutritionArticles();
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
            arrayList_nutrition.clear();
            adapter.notifyDataSetChanged();
            getNutritionArticles();
        }
    };


    private class ListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<SkilzObj> arrayList;

        private static final long ID_CONSTANT = 123456789;

        public ListAdapter(Context c, ArrayList<SkilzObj> array){
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
            SkilzObj item = getItem(position);
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_3, parent, false);
            }

            ImageView imageView = (ImageView)convertView.findViewById(R.id.item3_image);
            TextView title = (TextView)convertView.findViewById(R.id.item3_title);
            TextView source = (TextView)convertView.findViewById(R.id.item3_source);

            imageView.setImageBitmap(BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length));
            title.setText(item.getPrimaryString());
            source.setText(item.getSecondaryString());

            return convertView;
        }
    }
    private void getNutritionArticles(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Nutrition");
        query.whereContainedIn("skills", skills);
        query.whereEqualTo("sports", sport);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++){
                        final String title = objects.get(i).getString("title");
                        final String summary = objects.get(i).getString("summary");
                        final String source = objects.get(i).getString("source");
                        final String objectId = objects.get(i).getObjectId();
                        final boolean video = objects.get(i).getBoolean("hasVideo");

                        ParseFile imageFile = (ParseFile)objects.get(i).get("imageFile");
                        imageFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                arrayList_nutrition.add(new SkilzObj(objectId, title, source, summary, bytes, video, "", "Nutrition", "NutritionItems"));
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

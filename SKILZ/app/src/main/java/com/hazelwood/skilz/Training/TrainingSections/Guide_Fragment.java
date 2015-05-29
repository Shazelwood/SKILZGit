package com.hazelwood.skilz.Training.TrainingSections;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hazelwood.skilz.Detail.Detail_Activity;
import com.hazelwood.skilz.R;
import com.hazelwood.skilz.SkilzObj;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Hazelwood on 4/21/15.
 */
public class Guide_Fragment extends android.support.v4.app.Fragment {

    ListView listView;
    ListAdapter adapter;

    SwipeRefreshLayout mSwipeRefreshLayout;
    View v;
    String sport;

    ArrayList<String> skills;
    ArrayList<SkilzObj> arrayList_articles;

    public static final String TAG = "Guide_Frag";

    public Guide_Fragment (){

    }

    public static Guide_Fragment newInstance(String sports_, ArrayList<String> skills_){
        Guide_Fragment fragment = new Guide_Fragment();
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
    public void onResume() {
        super.onResume();

        if (arrayList_articles.isEmpty()){
            if (SkilzObj.isConnected(getActivity())){
                getArticles();
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupLanding();

        sport = getArguments().getString("SPORT");
        skills = getArguments().getStringArrayList("SKILLS");
        arrayList_articles = new ArrayList<>();

        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.guide_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.grey, R.color.red);
        mSwipeRefreshLayout.setOnRefreshListener(refresh);

        listView = (ListView)v.findViewById(R.id.list_view);
        adapter = new ListAdapter(getActivity(), arrayList_articles);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Detail_Activity.class);
                String objectID = arrayList_articles.get(position).getObjectId();
                String className = arrayList_articles.get(position).getClassName();
                String subClassName = arrayList_articles.get(position).getSubClassName();
                boolean hasVideo = arrayList_articles.get(position).hasVideo();

                intent.putExtra("ID", objectID);
                intent.putExtra("Class", className);
                intent.putExtra("SubClass", subClassName);

                if (hasVideo){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(arrayList_articles.get(position).getUrlString()));
                                    startActivity(browserIntent);
                } else {
                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(getActivity(), view, "guideTransition");

                    getActivity().startActivity(intent, options.toBundle());
                }

            }
        });
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    SwipeRefreshLayout.OnRefreshListener refresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            arrayList_articles.clear();
            adapter.notifyDataSetChanged();
            getArticles();
        }
    };

    private void setupLanding(){
        LayoutInflater layoutInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.landing_screen, null);
        TextView quoteTV = (TextView) layout.findViewById(R.id.landingText);
        TextView nameTV = (TextView) layout.findViewById(R.id.landingName);
        String[] quotes = getQuote();

        quoteTV.setText(quotes[0]);
        nameTV.setText("— " + quotes[1]);


        final PopupWindow popup = new PopupWindow(getActivity());
        popup.setContentView(layout);
        popup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popup.setFocusable(true);
        popup.showAtLocation(layout, Gravity.CENTER,0,0);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

}
    private String[] getQuote(){
        Random random = new Random();

        int num = random.nextInt(10 - 1);

        ArrayList<String> quotes = new ArrayList<>();
        quotes.add("It’s not whether you get knocked down; it’s whether you get up.");
        quotes.add("I’ve missed more than 9,000 shots in my career. I’ve lost almost 300 games. 26 times, I’ve been trusted to take the game winning shot and missed. I’ve failed over and over and over again in my life. And that is why I succeed.");
        quotes.add("Gold medals aren’t really made of gold. They’re made of sweat, determination, and a hard-to-find alloy called guts.");
        quotes.add("You miss 100 percent of the shots you don’t take.");
        quotes.add("Never give up! Failure and rejection are only the first step to succeeding.");
        quotes.add("You’re never a loser until you quit trying.");
        quotes.add("I hated every minute of training, but I said, ‘Don’t quit. Suffer now and live the rest of your life as a champion.’");
        quotes.add("It is not the size of a man but the size of his heart that matters. ");
        quotes.add("Just keep going. Everybody gets better if they keep at it.");
        quotes.add("If you train hard, you’ll not only be hard, you’ll be hard to beat.");

        ArrayList<String> names = new ArrayList<>();
        names.add("Vince Lombardi");
        names.add("Michael Jordan");
        names.add("Dan Gable");
        names.add("Wayne Gretzky");
        names.add("Jim Valvano");
        names.add("Mike Ditka");
        names.add("Muhammad Ali");
        names.add("Evander Holyfield");
        names.add("Ted Williams");
        names.add("Herschel Walker");

        String[] randomQuote = {quotes.get(num), names.get(num)};

        return randomQuote;

    }
    private class ListAdapter extends BaseAdapter{

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
        public int getViewTypeCount() {
            return 2;
        }

        private static final int REGULAR = 0;
        private static final int HEADER = 1;

        @Override
        public int getItemViewType(int position) {
            SkilzObj item = getItem(position);
            int type;
            if (item.hasVideo()){
                type = 0;
            }else{
                type = 1;
            }
            return type;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                switch (getItemViewType(position)){
                    case 0:
                        convertView = LayoutInflater.from(context).inflate(R.layout.item_2, parent, false);
                        break;
                    case 1:
                        convertView = LayoutInflater.from(context).inflate(R.layout.item_3, parent, false);
                        break;
                }

            }

            SkilzObj item = getItem(position);
            switch (getItemViewType(position)){
                case 0:
                    TextView title1 = (TextView)convertView.findViewById(R.id.item2_title);
                    TextView summary1 = (TextView)convertView.findViewById(R.id.item2_summary);
                    TextView source1 = (TextView)convertView.findViewById(R.id.item2_source);

                    title1.setText(item.getPrimaryString());
                    source1.setText(item.getSecondaryString());
                    summary1.setText(item.getTertiaryString());
                    break;
                case 1:
                    ImageView imageView = (ImageView)convertView.findViewById(R.id.item3_image);
                    TextView title = (TextView)convertView.findViewById(R.id.item3_title);
                    TextView source = (TextView)convertView.findViewById(R.id.item3_source);

                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length));
                    title.setText(item.getPrimaryString());
                    source.setText(item.getSecondaryString());

                    break;
            }



            return convertView;
        }
    }
    private void getArticles(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Article");
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
                        final String video = objects.get(i).getString("video");
                        final boolean hasVideo = objects.get(i).getBoolean("hasVideo");

                        ParseFile imageFile = (ParseFile)objects.get(i).get("imageFile");
                        imageFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                arrayList_articles.add(new SkilzObj(objectId, title, source, summary, bytes, hasVideo, video, "Article", "ArticleItems"));
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

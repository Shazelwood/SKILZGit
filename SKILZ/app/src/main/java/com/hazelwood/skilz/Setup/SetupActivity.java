package com.hazelwood.skilz.Setup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hazelwood.skilz.MainActivity;
import com.hazelwood.skilz.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class SetupActivity extends ActionBarActivity {
    private static final String PREF_USER_DID_SETUP = "setup_complete";
    private boolean mUserCompletedSetup;
    Bundle args;
    View v1, v2, v3;
    Button nextBTN;
    ArrayList skillsArray;
    String[] skillsStrings;
    String sportsString;

    int click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        click = 0;
        setContentView(R.layout.activity_setup);
        v1 = findViewById(R.id.setup_one);
        v2 = findViewById(R.id.setup_two);
        v3 = findViewById(R.id.setup_three);
        nextBTN = (Button)findViewById(R.id.setup_button);

        //Setup View 1, information


        //Setup View 2, Sports grid
        ArrayList<Integer> array_image = new ArrayList<Integer>();
        array_image.add(R.drawable.basketball);
        array_image.add(R.drawable.sprint);
        array_image.add(R.drawable.baseball);
        array_image.add(R.drawable.football);
        array_image.add(R.drawable.soccer);
        array_image.add(R.drawable.wrestling);
        array_image.add(R.drawable.swimming);
        array_image.add(R.drawable.tennis);
        array_image.add(R.drawable.volleyball);
        array_image.add(R.drawable.cheer);

        skillsStrings = getResources().getStringArray(R.array.skills_array);
        skillsArray = new ArrayList();

        GridView sportsGrid = (GridView)v2.findViewById(R.id.sport_grid);
        GridAdapter gridAdapter = new GridAdapter(this,array_image);
        sportsGrid.setAdapter(gridAdapter);

        sportsGrid.setOnItemClickListener(selectSport);

        //Setup View 3, Skills list
        String[] skills = getResources().getStringArray(R.array.skills_array);
        ListView skillsList = (ListView)v3.findViewById(R.id.skill_list);
        ListAdapter adapter = new ListAdapter(this, skills);
        skillsList.setAdapter(adapter);

        final Intent main = new Intent(SetupActivity.this, MainActivity.class);
        args = new Bundle();

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click++;
                switch (click){
                    case 1:
                        v1.setVisibility(View.GONE);
                        v2.setVisibility(View.VISIBLE);
                        v3.setVisibility(View.GONE);
                        nextBTN.setVisibility(View.GONE);
                        break;
                    case 2:
                        Set<String> set = new HashSet<String>();
                        set.addAll(skillsArray);

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SetupActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putStringSet("SKILLS", set);
                        editor.putString("SPORT", sportsString);
                        editor.putBoolean(PREF_USER_DID_SETUP, Boolean.TRUE);
                        editor.commit();

                        finish();

                        startActivity(main);
                        click = 0;
                        break;
                }


            }
        });

    }



    AdapterView.OnItemClickListener selectSport = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            v3.setVisibility(View.VISIBLE);
            nextBTN.setVisibility(View.VISIBLE);


            sportsString = getResources().getStringArray(R.array.sports_array)[position];
        }
    };


    private class GridAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<Integer> arrayList;

        private static final long ID_CONSTANT = 123456789;

        public GridAdapter(Context c, ArrayList<Integer> array){
            context = c;
            arrayList = array;
        }
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Integer getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ID_CONSTANT + position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.setup_sports_items, parent, false);
            }

            ImageView imageView = (ImageView)convertView.findViewById(R.id.grid_sport_image_view);
            imageView.setImageResource(arrayList.get(position));


            return convertView;
        }
    }

    private class ListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<String> arrayList;

        private static final long ID_CONSTANT = 123456789;

        public ListAdapter(Context c, String[] array){
            context = c;
            arrayList =  new ArrayList<>(Arrays.asList(array));
        }
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public String getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ID_CONSTANT + position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            String item = getItem(position);

            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.setup_skills_items, parent, false);
            }

            TextView textView = (TextView)convertView.findViewById(R.id.textView1);
            CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkBox1);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        skillsArray.add(skillsStrings[position]);
                    } else if (skillsArray.contains(position)) {

                        skillsArray.remove(Integer.valueOf(position));
                    }
                }
            });
            textView.setText(item);

            return convertView;
        }
    }
}

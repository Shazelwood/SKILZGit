package com.hazelwood.skilzadmin;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.Parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private FragmentManager mFragManager;
    public static final String TAG = "MainActivityTAG";
    ListAdapter adapter;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = getObj(MainActivity.this);
        ListView listView = (ListView)findViewById(R.id.main_list_view);
        adapter = new ListAdapter(MainActivity.this, arrayList);
        listView.setAdapter(adapter);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "zt1QXsnVxEz5nNaOXkls8jrEgSoVvI3T2LIK17rc", "RYgybo6poSSSTuTn0JL7GFmuRsOp7vz8PHOeCiHH");
        mFragManager = getFragmentManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayList = getObj(MainActivity.this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(MainActivity.this, FormActivity.class);

        switch (id){
            case R.id.action_nutrition:
                intent.putExtra("type", 10);
                startActivity(intent);
                break;
            case R.id.action_training:
                intent.putExtra("type", 20);
                startActivity(intent);
                break;
            case R.id.action_challenge:
                intent.putExtra("type", 30);
                startActivity(intent);
                break;
            case R.id.action_athlete:
                intent.putExtra("type", 40);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private class ListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<String> arrayList;

        private static final long ID_CONSTANT = 123456789;

        public ListAdapter(Context c, ArrayList<String> array){
            context = c;
            arrayList = array;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            String item = getItem(position);

            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_4, parent, false);
            }

            TextView textView = (TextView)convertView.findViewById(R.id.bookmark_title);
            textView.setText(item);

            return convertView;
        }
    }

    public static ArrayList<String> getObj (Context context) {
        ArrayList<String> arrayList = new ArrayList<String>();

        try {
            File extFolder = context.getExternalFilesDir(null);
            File file = new File(extFolder, "SKILZADMIN.dat");

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
}
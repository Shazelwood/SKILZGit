package com.hazelwood.skilz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Hazelwood on 5/22/15.
 */
public class ChallengeActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

//        intent.putExtra("TITLE", arrayList_challenge.get(position).getChallenge_title());
//        intent.putExtra("DESCRIPTION", arrayList_challenge.get(position).getChallenge_description());
//        intent.putExtra("LEVEL", arrayList_challenge.get(position).getChallenge_level());
//        intent.putExtra("LINK", arrayList_challenge.get(position).getChallenge_link());
//        intent.putExtra("POINTS", arrayList_challenge.get(position).getChallenge_points());

        Bundle arg = getIntent().getExtras();
        final String title = arg.getString("TITLE");
        String description = arg.getString("DESCRIPTION");
        String pointString = arg.getString("POINTS");
        final Integer pointsWorth = Integer.parseInt(pointString);
        Log.d("VALUE", String.valueOf(pointsWorth + 50));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ChallengeActivity.this);
        final SharedPreferences.Editor editor = sp.edit();

        final int myIntValue = sp.getInt("SKILLPOINTS",0);

        final TextView challenge_title = (TextView)findViewById(R.id.activity_challenge_title);
        TextView challenge_description = (TextView)findViewById(R.id.activity_challenge_description);
        final Button challenge_accept = (Button)findViewById(R.id.activity_challenge__button);
        challenge_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTitle(ChallengeActivity.this, title);
                editor.putInt("SKILLPOINTS", myIntValue + pointsWorth);
                editor.commit();

            }
        });

        ArrayList<String> checkList = getChallengesArray(ChallengeActivity.this);
        if (checkList.contains(title)){
            challenge_accept.setEnabled(false);
            challenge_accept.setText("CHALLENGE ACCEPTED");
        }

        challenge_title.setText(title);
        challenge_description.setText(description);

    }

    //Saves the title to compare to the challenge list title to see if completed or not
    public void saveTitle(Context context, String string) {

        ArrayList<String> data = null;


        try {
            File extFolder = getExternalFilesDir(null);
            File file = new File(extFolder, "CHALLENGES.dat");

            if (file.exists()) {
                FileInputStream fin = new FileInputStream(file);
                ObjectInputStream oin = new ObjectInputStream(fin);
                data = (ArrayList<String>) oin.readObject();
                oin.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(data == null) {
            data = new ArrayList<String>();
        }

        if(!data.contains(string)) {
            data.add(string);
        }

        try {

            File extFolder = getExternalFilesDir(null);
            File extFile = new File(extFolder, "CHALLENGES.dat");
            FileOutputStream fos = new FileOutputStream(extFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();

        } catch(Exception e) {
            e.printStackTrace();
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
}
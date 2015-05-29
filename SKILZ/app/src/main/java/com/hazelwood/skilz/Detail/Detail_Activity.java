package com.hazelwood.skilz.Detail;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hazelwood.skilz.R;
import com.hazelwood.skilz.SkilzObj;
import com.hazelwood.skilz.SubItem;
import com.hazelwood.skilz.TransitionAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
public class Detail_Activity extends ActionBarActivity implements Detail_List_Frag.Listener, Detail_Text_Frag.Listener {

    public static final String TAG = "DETAIL_ACTIVITY";
    ArrayList<SubItem> arrayList_subItems;

    String id, className, subClassName, title_temp;
    SkilzObj skilzObj;
    ProgressDialog progressDialog;


    TextView title, author, source, summary;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        progressDialog = new ProgressDialog(Detail_Activity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading ..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        arrayList_subItems = new ArrayList<>();

        title = (TextView)findViewById(R.id.detail_title);
        author = (TextView)findViewById(R.id.detail_author);
        source = (TextView)findViewById(R.id.detail_source);
        imageView = (ImageView)findViewById(R.id.detail_image);
        summary = (TextView) findViewById(R.id.detail_summary);

        id = getIntent().getStringExtra("ID");
        className = getIntent().getStringExtra("Class");
        subClassName = getIntent().getStringExtra("SubClass");
        final Button bookmark = (Button)findViewById(R.id.detail_bookmark);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkilzObj.saveObj(Detail_Activity.this, skilzObj);
            }
        });

        getWindow().getEnterTransition().addListener(new TransitionAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                circleAnim(imageView);
                bookmark.setVisibility(View.VISIBLE);
                getWindow().getEnterTransition().removeListener(this);
            }
        });


    }

    public void circleAnim(View view){
        // get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.setDuration(1000);
        anim.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (SkilzObj.isConnected(this)){
            progressDialog.show();
            setPage(id, className, subClassName);
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Connection Problem.");
            builder1.setMessage("There was a problem with connection.");
            builder1.setCancelable(true);
            builder1.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            progressDialog.dismiss();
            alert11.show();
        }
    }

    private void setPage(final String _id, final String _class, final String _subClass){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(_class);
        query.getInBackground(_id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    final String detail_title, detail_author, detail_source, detail_summary;


                    if (_class.equals("Athlete")){
                        detail_title = object.getString("firstName");
                        detail_author = object.getString("lastName");
                        detail_source = object.getString("socialName");
                        detail_summary = object.getString("bio");
                    } else {
                        detail_title = object.getString("title");
                        detail_author = object.getString("author");
                        detail_source = object.getString("source");
                        detail_summary = object.getString("summary");
                    }


                    title_temp = detail_title;

                    title.setText(detail_title);
                    author.setText(detail_author);
                    summary.setText(detail_summary);
                    source.setText(detail_source);
                    source.setAllCaps(true);

                    ParseFile imageFile = (ParseFile)object.get("imageFile");
                    imageFile.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        }
                    });

                    ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery(_subClass);
                    innerQuery.whereEqualTo("parent",object);
                    innerQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> innerObjects, ParseException e) {
                            for (int j = 0; j < innerObjects.size(); j++) {
                                final String innerTitle = innerObjects.get(j).getString("title");
                                final String innerExp = innerObjects.get(j).getString("explanation");

                                ParseFile imageFile = (ParseFile)innerObjects.get(j).get("imageFile");
                                imageFile.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] bytes, ParseException e) {
                                        progressDialog.dismiss();
                                        arrayList_subItems.add(new SubItem(bytes, innerExp, innerTitle));
                                        //no video so false and "none" for videos
                                        skilzObj = new SkilzObj(_id,detail_title, detail_author, detail_summary,bytes,false,"none",_class,_subClass);

                                        if (arrayList_subItems.size() == 1){
                                            Log.d(TAG, String.valueOf(arrayList_subItems.size()));
                                            getFragmentManager().beginTransaction().replace(R.id.detail_container, Detail_Text_Frag.newInstance(arrayList_subItems), TAG).commit();
                                        } else {
                                            Log.d(TAG, String.valueOf(arrayList_subItems.size()));
                                            getFragmentManager().beginTransaction().replace(R.id.detail_container, Detail_List_Frag.newInstance(arrayList_subItems), TAG).commit();
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    // something went wrong
                }
            }
        });

    }

    @Override
    public void getURL(String bob) {

    }
}

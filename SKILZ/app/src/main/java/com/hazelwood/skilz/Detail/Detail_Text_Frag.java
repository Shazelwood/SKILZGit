package com.hazelwood.skilz.Detail;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hazelwood.skilz.R;
import com.hazelwood.skilz.SubItem;

import java.util.ArrayList;

/**
 * Created by Hazelwood on 4/27/15.
 */
public class Detail_Text_Frag extends Fragment {
    View v;

    public static final String TAG = "TEXT FRAG";

    public Detail_Text_Frag (){

    }

    public interface Listener{
        public void getURL(String bob);
    }

    public Listener mListener;

    public static Detail_Text_Frag newInstance(ArrayList<SubItem> data){
        Detail_Text_Frag fragment = new Detail_Text_Frag();
        if (data == null){
            Log.d(TAG, "No data");

        } else {
            Bundle args = new Bundle();
            args.putSerializable("data", data);

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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.detail_text_fragment, container, false);
        v = fragView;
        return fragView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<SubItem> arrayList = (ArrayList<SubItem>) getArguments().getSerializable("data");
        TextView textView = (TextView)v.findViewById(R.id.detail_text);
        textView.setText(arrayList.get(0).getExplanation());


    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
}

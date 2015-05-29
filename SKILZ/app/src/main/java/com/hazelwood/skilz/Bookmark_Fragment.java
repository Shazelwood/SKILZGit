package com.hazelwood.skilz;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hazelwood.skilz.Detail.Detail_Activity;

import java.util.ArrayList;

/**
 * Created by Hazelwood on 4/27/15.
 */
public class Bookmark_Fragment extends Fragment {

    MainActivity mContext;

    ListView listView;
    ListAdapter adapter;
    ArrayList<SkilzObj> arrayList;
    View v;
    SwipeRefreshLayout mSwipeRefreshLayout;


    public static final String TAG = "Guide_Frag";

    public Bookmark_Fragment (){

    }

    public interface Listener{
        public void getURL(String bob);
    }

    public Listener mListener;

    public static Bookmark_Fragment newInstance(String data){
        Bookmark_Fragment fragment = new Bookmark_Fragment();
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

        arrayList = SkilzObj.getObj(getActivity());

        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.guide_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.grey, R.color.red);
        mSwipeRefreshLayout.setOnRefreshListener(refresh);

        listView = (ListView)v.findViewById(R.id.list_view);
        adapter = new ListAdapter(getActivity(), arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Detail_Activity.class);
//                intent.putExtra("DATA", arrayList.get(position));
                getActivity().startActivity(intent);
            }
        });
    }

    SwipeRefreshLayout.OnRefreshListener refresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            arrayList = SkilzObj.getObj(getActivity());
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
    }


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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_4, parent, false);
            }

            TextView textView = (TextView)convertView.findViewById(R.id.bookmark_title);
            textView.setText(item.getPrimaryString());

            return convertView;
        }
    }
}

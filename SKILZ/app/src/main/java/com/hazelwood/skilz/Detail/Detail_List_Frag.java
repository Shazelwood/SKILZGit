package com.hazelwood.skilz.Detail;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hazelwood.skilz.R;
import com.hazelwood.skilz.SubItem;

import java.util.ArrayList;

/**
 * Created by Hazelwood on 4/27/15.
 */
public class Detail_List_Frag extends Fragment{
    View v;
    public static final String TAG = "LIST FRAG";

    public Detail_List_Frag (){

    }

    public interface Listener{
        public void getURL(String bob);
    }

    public Listener mListener;

    public static Detail_List_Frag newInstance(ArrayList<SubItem> data){
        Detail_List_Frag fragment = new Detail_List_Frag();
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
        View fragView = inflater.inflate(R.layout.detail_list_fragment, container, false);
        v = fragView;
        return fragView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ArrayList<SubItem> arrayList = (ArrayList<SubItem>) getArguments().getSerializable("data");

        Log.d(TAG, String.valueOf(arrayList.size()));
        ListView listView = (ListView)v.findViewById(R.id.detail_list);

        DetailListAdapter adapter = new DetailListAdapter(getActivity(), arrayList);

        listView.setAdapter(adapter);
        Utility.setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title_dialog = arrayList.get(position).getTitle();
                String descr_dialog = arrayList.get(position).getExplanation();
                byte[] image_dialog= arrayList.get(position).getImage();

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.popup_layout);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setTitle(title_dialog);

                TextView descr_tv = (TextView) dialog.findViewById(R.id.dialog_text);
                descr_tv.setText(descr_dialog);
                ImageView imageView = (ImageView) dialog.findViewById(R.id.dialog_image);
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(image_dialog, 0, image_dialog.length));

                dialog.show();
            }
        });


    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class DetailListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<SubItem> arrayList;

        private static final long ID_CONSTANT = 123456789;

        public DetailListAdapter(Context c, ArrayList<SubItem> array){
            context = c;
            arrayList = array;
        }
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public SubItem getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ID_CONSTANT + position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.detail_list_item, parent, false);

            }

            SubItem item = getItem(position);
            TextView textView = (TextView)convertView.findViewById(R.id.detail_list_title);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.detail_list_image);

            textView.setText(item.getTitle());
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length));


            return convertView;
        }
    }

    public static class Utility {
        public static void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
        }
    }



}

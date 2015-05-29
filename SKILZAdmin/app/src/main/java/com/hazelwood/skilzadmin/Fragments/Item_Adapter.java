package com.hazelwood.skilzadmin.Fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hazelwood.skilzadmin.R;

import java.util.ArrayList;

/**
 * Created by Hazelwood on 4/22/15.
 */
public class Item_Adapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Item> mObjects;

    private static final long ID_CONSTANT = 123456789;

    public Item_Adapter(Context c, ArrayList<Item> objects){
        mContext = c;
        mObjects = objects;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Item getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }

        Item item = getItem(position);

        TextView item_text = (TextView)convertView.findViewById(R.id.item_text);
        TextView item_title = (TextView)convertView.findViewById(R.id.item_title);
        ImageView item_image = (ImageView)convertView.findViewById(R.id.item_image);

        item_text.setText(item.getExplanation());
        item_title.setText(item.getTitle());
        item_image.setImageBitmap(BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length));

        return convertView;
    }
}

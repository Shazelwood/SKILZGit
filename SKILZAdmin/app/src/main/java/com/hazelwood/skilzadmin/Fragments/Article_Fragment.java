package com.hazelwood.skilzadmin.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;

import com.hazelwood.skilzadmin.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class Article_Fragment extends Fragment {
    public static final String TAG = "ARTICLE";
    private static final String ARG_PARAM1 = "param1";

    Switch switch_type;
    EditText title_et, source_et, author_et, summary_et ,dialog_text, dialog_title, video_et;
    Button skills_btn, add_btn, sports_btn;
    ImageView main_img, dialog_img;
    ListView listView;
    ArrayList<String> skills_selected, sports_selected;
    ArrayList<Item> items;
    static final int REQUEST_GALLERY_MAIN = 0x01002;
    static final int REQUEST_GALLERY_ITEM = 0x01003;
    byte[] bytes;
    String imageName;
    ParseObject articleItemObj;
    ParseObject articleParseObj;

    Item_Adapter adapter;

    private String mParam1;

    private OnFragmentInteractionListener mListener;

    public static Article_Fragment newInstance(String param1) {
        Article_Fragment fragment = new Article_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public Article_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_form, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                saveArticle();
//                ParseObject foo = new ParseObject("Author");
//                foo.put("first name", "shae");
//                foo.put("last name", "hazelwood");
//                foo.put("social name", "boom");
//                foo.put("bio", "boom");
//                foo.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        getActivity().finish();
//                    }
//                });

                return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Add new Article");
        articleParseObj = new ParseObject("Article");


        switch_type = (Switch)getActivity().findViewById(R.id.article_switcher_type);
        switch_type.setOnCheckedChangeListener(layoutSwitch);

        video_et = (EditText)getActivity().findViewById(R.id.article_edit_videoLink);
        title_et= (EditText)getActivity().findViewById(R.id.article_edit_title);
        source_et = (EditText)getActivity().findViewById(R.id.article_edit_source);
        author_et = (EditText)getActivity().findViewById(R.id.article_edit_author);
        summary_et = (EditText)getActivity().findViewById(R.id.article_summary_edit);

        skills_btn = (Button)getActivity().findViewById(R.id.article_button_skills);
        sports_btn = (Button)getActivity().findViewById(R.id.article_button_sports);
        add_btn = (Button)getActivity().findViewById(R.id.article_button_add);

        main_img = (ImageView)getActivity().findViewById(R.id.article_image_view);
        main_img.setOnClickListener(clickImage);

        skills_btn.setOnClickListener(showChecklist);
        sports_btn.setOnClickListener(showChecklist);
        add_btn.setOnClickListener(addRow);

        items = new ArrayList<>();
        skills_selected = new ArrayList<>();
        sports_selected = new ArrayList<>();

        listView = (ListView)getActivity().findViewById(R.id.article_list_view);
        adapter = new Item_Adapter(getActivity(), items);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(deleteRow);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY_MAIN && resultCode != Activity.RESULT_CANCELED){
            setImage(main_img, data);
        } else if (requestCode == REQUEST_GALLERY_ITEM && resultCode != Activity.RESULT_CANCELED){
            setImage(dialog_img, data);
        }
    }

    CompoundButton.OnCheckedChangeListener layoutSwitch = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                video_et.setVisibility(View.VISIBLE);
            } else {
                video_et.setVisibility(View.GONE);
            }

        }
    };

    AdapterView.OnItemLongClickListener deleteRow = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete

                            items.remove(position);
                            adapter.notifyDataSetChanged();

                            Utility.setListViewHeightBasedOnChildren(listView);

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_delete)
                    .show();
            return false;
        }
    };

    View.OnClickListener showChecklist = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.article_button_skills:
                    showChecklistDialog(R.array.skills_array, 1);
                    break;
                case R.id.article_button_sports:
                    showChecklistDialog(R.array.sports_array, 2);
                    break;
            }

        }
    };

    View.OnClickListener addRow = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater inflater = getActivity().getLayoutInflater();

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setView(inflater.inflate(R.layout.dialog_add, null))
                    .setTitle("Create Row")

                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            byte[] byteArray = getByteImage(dialog_img);

                            String text = dialog_text.getText().toString();
                            String title = dialog_title.getText().toString();
                            items.add(new Item(byteArray, text, title));
                            adapter.notifyDataSetChanged();

                            articleItemObj = new ParseObject("ArticleItems");
                            articleItemObj.put("title", title);
                            articleItemObj.put("explanation", text);
                            articleItemObj.put("parent", articleParseObj);
                            ParseFile subPhoto = new ParseFile("image.png", byteArray);
                            subPhoto.saveInBackground();

                            articleItemObj.put("imageFile", subPhoto);
                            articleItemObj.saveInBackground();

                            Utility.setListViewHeightBasedOnChildren(listView);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .show();

            dialog_text = (EditText)alertDialog.findViewById(R.id.dialog_subtext);
            dialog_title = (EditText)alertDialog.findViewById(R.id.dialog_title);
            dialog_img = (ImageView)alertDialog.findViewById(R.id.dialog_image);

            dialog_img.setOnClickListener(clickImage);

        }
    };

    View.OnClickListener clickImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.article_image_view:
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, REQUEST_GALLERY_MAIN);
                    break;
                case R.id.dialog_image:
                    i = new Intent(Intent.ACTION_PICK, android.provider.
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, REQUEST_GALLERY_ITEM);
                    break;
            }
        }
    };

    public void showChecklistDialog(final int array, final int arrayID){
        final String[] strings = getResources().getStringArray(array);

        final ArrayList<Integer> selectedItems=new ArrayList<>();

        new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                .setTitle("Make a selection")
                .setMultiChoiceItems(array, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {

                                if (isChecked) {
                                    selectedItems.add(which);
                                } else if (selectedItems.contains(which)) {

                                    selectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        switch (arrayID) {
                            case 1:
                                for (int i = 0; i < selectedItems.size(); i++) {
                                    skills_selected.add(strings[selectedItems.get(i)]);
                                }
                                break;
                            case 2:
                                for (int i = 0; i < selectedItems.size(); i++) {
                                    sports_selected.add(strings[selectedItems.get(i)]);
                                }
                                break;
                        }
                        selectedItems.clear();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .show();
    }

    public void setImage(ImageView image, Intent i){
        Uri selectedImage = i.getData();
        bytes = null;
        try {
            ContentResolver cr = getActivity().getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(selectedImage);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bytes = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        imageName = picturePath;
    }

    public byte[] getByteImage(ImageView image){

        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bm = image.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
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

    public void saveArticle(){
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Submitting Article ..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        final String title_ = title_et.getText().toString();
        String author_ = author_et.getText().toString();
        String summary_ = summary_et.getText().toString();
        String source_ = source_et.getText().toString();
        String video_ = video_et.getText().toString();
        byte[] image_ = getByteImage(main_img);
        ArrayList<String> skills_ = skills_selected;
        ArrayList<String> sports_ = sports_selected;

        ParseFile photoFile = new ParseFile("image.png", image_);
        photoFile.saveInBackground();

        articleParseObj.put("title", title_);
        articleParseObj.put("author", author_);
        articleParseObj.put("summary", summary_);
        articleParseObj.put("source", source_);
        articleParseObj.put("skills", skills_);
        articleParseObj.put("sports", sports_);
        articleParseObj.put("imageFile", photoFile);
        if (switch_type.isChecked()){
            articleParseObj.put("hasVideo", true);
            articleParseObj.put("video", video_);
        } else {
            articleParseObj.put("video", "no video");
            articleParseObj.put("hasVideo", false);
        }




        articleParseObj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                progress.dismiss();
                saveObj(getActivity(), title_);
                getActivity().finish();
            }
        });



    }


    public static void saveObj(Context context, String obj) {

        ArrayList<String> data = null;


        try {
            File extFolder = context.getExternalFilesDir(null);
            File file = new File(extFolder, "SKILZADMIN.dat");

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

        if(!data.contains(obj)) {
            data.add(obj);
        }

        try {

            File extFolder = context.getExternalFilesDir(null);
            File extFile = new File(extFolder, "SKILZADMIN.dat");
            FileOutputStream fos = new FileOutputStream(extFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}

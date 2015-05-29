package com.hazelwood.skilz;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hazelwood on 5/19/15.
 */
public class SkilzObj implements Serializable {

    public static final long serialVersionUID = 12345L;

    String primaryString, secondaryString, tertiaryString, objectId, className, subClassName, urlString;
    byte[] image;
    boolean hasVideo;

    public SkilzObj(String _objId, String _primary, String _secondary, String _tertiary, byte[] _image, boolean _hasVideo,String url, String _class, String _subClass){
        this.primaryString = _primary;
        this.secondaryString = _secondary;
        this.tertiaryString = _tertiary;
        this.image = _image;
        this.hasVideo = _hasVideo;
        this.objectId = _objId;
        this.className = _class;
        this.subClassName = _subClass;
        this.urlString = url;
    }

    public static boolean isConnected(Context context){
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService((Context.CONNECTIVITY_SERVICE));
        if (mgr != null){
            NetworkInfo info = mgr.getActiveNetworkInfo();
            if (info != null && info.isConnected()){
                return true;
            }
        }
        return false;
    }

    public static void saveObj(Context context, SkilzObj obj) {

        ArrayList<SkilzObj> data = null;


        try {
            File extFolder = context.getExternalFilesDir(null);
            File file = new File(extFolder, "BOOKMARK.dat");

            if (file.exists()) {
                FileInputStream fin = new FileInputStream(file);
                ObjectInputStream oin = new ObjectInputStream(fin);
                data = (ArrayList<SkilzObj>) oin.readObject();
                oin.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(data == null) {
            data = new ArrayList<SkilzObj>();
        }

        if(!data.contains(obj)) {
            data.add(obj);
        }

        try {

            File extFolder = context.getExternalFilesDir(null);
            File extFile = new File(extFolder, "BOOKMARK.dat");
            FileOutputStream fos = new FileOutputStream(extFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<SkilzObj> getObj (Context context) {
        ArrayList<SkilzObj> arrayList = new ArrayList<SkilzObj>();

        try {
            File extFolder = context.getExternalFilesDir(null);
            File file = new File(extFolder, "BOOKMARK.dat");

            if (file.exists()) {
                FileInputStream fin = new FileInputStream(file);
                ObjectInputStream oin = new ObjectInputStream(fin);
                arrayList = (ArrayList<SkilzObj>) oin.readObject();
                oin.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return arrayList;
    }

    private SkilzObj(){}

    public String getUrlString() {
        return urlString;
    }

    public String getClassName() {
        return className;
    }

    public String getSubClassName() {
        return subClassName;
    }

    public String getObjectId() {
        return objectId;
    }

    public boolean hasVideo() {
        return hasVideo;
    }

    public byte[] getImage() {
        return image;
    }

    public String getPrimaryString() {
        return primaryString;
    }

    public String getSecondaryString() {
        return secondaryString;
    }

    public String getTertiaryString() {
        return tertiaryString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setSubClassName(String subClassName) {
        this.subClassName = subClassName;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setPrimaryString(String primaryString) {
        this.primaryString = primaryString;
    }

    public void setSecondaryString(String secondaryString) {
        this.secondaryString = secondaryString;
    }

    public void setTertiaryString(String tertiaryString) {
        this.tertiaryString = tertiaryString;
    }
}

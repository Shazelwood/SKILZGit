package com.hazelwood.skilzadmin;

import com.hazelwood.skilzadmin.Fragments.Item;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hazelwood on 5/4/15.
 */
public class Thing implements Serializable {

    int displayType, viewType, categoryType;
    byte[] image;
    String title, author, source, summary;
    ArrayList<Integer> skills, sports;
    ArrayList<Item> items;

    public static final long serialVersionUID = 1234567890L;

    public void Thing(){

    }

    public void Thing(byte[] _image, String _title, String _author, String _source, int _displayType, int _viewType, int _categoryType,
          String _summary, ArrayList<Integer> _skills, ArrayList<Integer> _sports, ArrayList<Item> _items){

        title = _title;
        author = _author;
        source = _source;
        summary = _summary;
        displayType = _displayType;
        viewType = _viewType;
        categoryType = _categoryType;
        skills = _skills;
        sports = _sports;
        items = _items;
        image = _image;
    }


    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setSkills(ArrayList<Integer> skills) {
        this.skills = skills;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setSports(ArrayList<Integer> sports) {
        this.sports = sports;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public int getDisplayType() {
        return displayType;
    }

    public int getViewType() {
        return viewType;
    }

    public byte[] getImage() {
        return image;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getSource() {
        return source;
    }

    public String getSummary() {
        return summary;
    }

    public ArrayList<Integer> getSkills() {
        return skills;
    }

    public ArrayList<Integer> getSports() {
        return sports;
    }
}

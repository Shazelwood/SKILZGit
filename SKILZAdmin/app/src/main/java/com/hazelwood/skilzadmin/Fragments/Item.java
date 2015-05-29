package com.hazelwood.skilzadmin.Fragments;

import java.io.Serializable;

/**
 * Created by Hazelwood on 4/22/15.
 */
public class Item implements Serializable {

    public static final long serialVersionUID = 1234567890L;

    byte[] image;
    String explanation, title;

    public Item(byte[] img, String exp, String title_){
        image = img;
        explanation = exp;
        title = title_;
    }

    public Item(){}

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public byte[] getImage() {
        return image;
    }

    public String getExplanation() {
        return explanation;
    }
}

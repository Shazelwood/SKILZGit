package com.hazelwood.skilz;

import java.io.Serializable;

/**
 * Created by Hazelwood on 4/22/15.
 */
public class SubItem implements Serializable {

    public static final long serialVersionUID = 567890L;

    byte[] image;
    String explanation, title;

    public SubItem(byte[] img, String exp, String title_){
        image = img;
        explanation = exp;
        title = title_;
    }

    public SubItem(){}

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

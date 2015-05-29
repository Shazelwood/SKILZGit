package com.hazelwood.skilz;

import java.io.Serializable;

/**
 * Created by Hazelwood on 5/28/15.
 */
public class Trophy implements Serializable {

    public static final long serialVersionUID = 5678901234L;

    String trophy_title, trophy_description;
    int trophy_points, trophy_image;

    public Trophy(String _title, String _description, int _points, int _trophy){
        this.trophy_description = _description;
        this.trophy_title = _title;
        this.trophy_points = _points;
        this.trophy_image = _trophy;
    }

    public int getTrophy_image() {
        return trophy_image;
    }

    public int getTrophy_points() {
        return trophy_points;
    }

    public String getTrophy_description() {
        return trophy_description;
    }

    public String getTrophy_title() {
        return trophy_title;
    }

    public void setTrophy_image(int trophy_image) {
        this.trophy_image = trophy_image;
    }

    public void setTrophy_description(String trophy_description) {
        this.trophy_description = trophy_description;
    }

    public void setTrophy_points(int trophy_points) {
        this.trophy_points = trophy_points;
    }

    public void setTrophy_title(String trophy_title) {
        this.trophy_title = trophy_title;
    }
}

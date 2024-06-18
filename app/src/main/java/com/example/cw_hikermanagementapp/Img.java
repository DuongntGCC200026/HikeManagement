package com.example.cw_hikermanagementapp;

import java.io.Serializable;

public class Img implements Serializable {
    int id;
    int ob_id;
    String title;
    String date;
    int isDeleted;
    byte[] image;

    public Img(int id, int ob_id, String title, String date, int isDeleted, byte[] image) {
        this.id = id;
        this.ob_id = ob_id;
        this.title = title;
        this.date = date;
        this.isDeleted = isDeleted;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOb_id() {
        return ob_id;
    }

    public void setOb_id(int ob_id) {
        this.ob_id = ob_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

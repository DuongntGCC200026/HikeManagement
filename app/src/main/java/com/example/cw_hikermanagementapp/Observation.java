package com.example.cw_hikermanagementapp;

import java.io.Serializable;

public class Observation implements Serializable {
    public int id;
    public String nameOfObservation;
    public String dateAndTime;
    public String additionalComments;
    public int isDeleted;
    public int hikeId;

    public Observation(int id, String nameOfObservation, String dateAndTime, String additionalComments, int isDeleted, int hikeId) {
        this.id = id;
        this.nameOfObservation = nameOfObservation;
        this.dateAndTime = dateAndTime;
        this.additionalComments = additionalComments;
        this.isDeleted = isDeleted;
        this.hikeId = hikeId;
    }

    public int getId() {
        return id;
    }

    public String getNameOfObservation() {
        return nameOfObservation;
    }

    public void setNameOfObservation(String nameOfObservation) {
        this.nameOfObservation = nameOfObservation;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getHikeId() {
        return hikeId;
    }

    public void setHikeId(int hikeId) {
        this.hikeId = hikeId;
    }
}

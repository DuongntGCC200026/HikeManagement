package com.example.cw_hikermanagementapp;

import java.io.Serializable;

public class Hike implements Serializable {
    private int id;
    private String name;
    private String location;
    private String dateOfTheHike;
    private int parking;
    private double length;
    private int level;
    private String description;
    private String timeStart;
    private String timeStop;
    private int isDeleted;

    public Hike(int id, String name, String location, String dateOfTheHike, int parking, double length, int level, String description, String timeStart, String timeStop, int isDeleted) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.dateOfTheHike = dateOfTheHike;
        this.parking = parking;
        this.length = length;
        this.level = level;
        this.description = description;
        this.timeStart = timeStart;
        this.timeStop = timeStop;
        this.isDeleted = isDeleted;
    }

    public Hike(int id, String name, String location, String dateOfTheHike, Double length, int isDeleted) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.dateOfTheHike = dateOfTheHike;
        this.length = length;
        this.isDeleted = isDeleted;
    }

    public Hike(String name, String location, String dateOfTheHike, int parking, Double length, int level, String description, String timeStart, String timeStop) {
        this.name = name;
        this.location = location;
        this.dateOfTheHike = dateOfTheHike;
        this.parking = parking;
        this.length = length;
        this.level = level;
        this.description = description;
        this.timeStart = timeStart;
        this.timeStop = timeStop;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateOfTheHike() {
        return dateOfTheHike;
    }

    public void setDateOfTheHike(String dateOfTheHike) {
        this.dateOfTheHike = dateOfTheHike;
    }

    public int isParking() {
        return parking;
    }

    public void setParking(int parking) {
        this.parking = parking;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeStop() {
        return timeStop;
    }

    public void setTimeStop(String timeStop) {
        this.timeStop = timeStop;
    }

    public int isDeleted() {
        return isDeleted;
    }

    public void setDeleted(int deleted) {
        isDeleted = deleted;
    }
}

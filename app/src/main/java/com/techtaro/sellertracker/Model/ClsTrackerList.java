package com.techtaro.sellertracker.Model;

public class ClsTrackerList {
    public static final String TABLE = "TrackerList";

    public static final String KEY_ID = "id";
    public static final String KEY_CODE = "code";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_COUNTER = "counter";
    public static final String KEY_STIME = "stime";

    private String id;
    private String code;
    private String description;
    private String counter;
    private String stime;

    public ClsTrackerList() {
    }

    public ClsTrackerList(String id,String code,String description,String counter,String stime) {

        this.id = id;
        this.code = code;
        this.description = description;
        this.counter = counter;
        this.stime = stime;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }


    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

}

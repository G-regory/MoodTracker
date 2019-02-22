package com.carriel.gregory.moodtracker.model;

import java.util.Date;

public class StoreMood {
    //******Attribute*******
    private String mMood;
    private String mComment;
    private Date date;

    //******constructor********
    public StoreMood(String pMood, String pComment, Date pDate) {
        mMood =pMood;
        mComment =pComment;
        date=pDate;
    }

    public String getMood()  {
        return mMood;
    }

    public String getComment() {
        return mComment;
    }

    public Date getDate() {
        return date;
    }
}

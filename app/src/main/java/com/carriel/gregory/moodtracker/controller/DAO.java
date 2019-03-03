package com.carriel.gregory.moodtracker.controller;

import android.content.Context;

import com.carriel.gregory.moodtracker.controller.baseSQL.MySQLiteOpenHelper;
import com.carriel.gregory.moodtracker.controller.utils.MyToolsDate;
import com.carriel.gregory.moodtracker.model.StoreMood;

import java.util.Date;
import java.util.List;

public class DAO {

    private final String TAG= "MoodMessage: SaveMood";

    //********Model Class***********
    private StoreMood mNewStoreMood;
    private  StoreMood mLastStoreMood;
    private  StoreMood mEmptyMood;


    private static MySQLiteOpenHelper mMySQLiteOpenHelper;

    private static DAO ourInstance;

    // --- SINGLETON ---
    private DAO() {}

    public static DAO getInstance(Context context) {
        if(ourInstance==null){
            ourInstance= new DAO();
        }
        mMySQLiteOpenHelper=new MySQLiteOpenHelper(context);
        return ourInstance;
    }

    /**
     * recovers last Mood, check Number ID already present in the table
     * if Number ID is empty stock last Mood in the table
     * else compare  difference of day between last date recorded and new date
     * if some day without Mood, record Moods by fault without comment
     * if last date and current date is the same, update last ID in the table.
     * @param mood
     * @param comment
     * @param date
     */
    public void recordMood(String mood, String comment, Date date) {

        mNewStoreMood =new StoreMood(mood,comment,date);

        if (returnNumberId()>0) {
            compareLastDateAndMood(getDifferenceDays(date));//compare difference day and insert or update in the table
        }else{
            mMySQLiteOpenHelper.recordDate(mNewStoreMood); //insert in the table
        }
    }

    /**
     * recovers difference day
     * @param date current day
     * @return  difference last date and current date
     */
    private int getDifferenceDays(Date date) {
        int differenceDay;
        mLastStoreMood = mMySQLiteOpenHelper.restoreLastData();
        differenceDay= (int) MyToolsDate.compareDate(date, mLastStoreMood.getDate());

        return differenceDay;
    }


    /**
     * Method that compare difference by date between last date and current date
     * if last date is the same, update the table
     * if last date to different, do record of the table
     * if more than 1 records default Moods without how in the table, with the latest new Mood
     * @param differenceDays
     */
    private void compareLastDateAndMood(long differenceDays) {
        int difDay = (int) differenceDays; //difference day

        if(difDay == 0 && mNewStoreMood != null){  //if 0 day difference and mNewStoreMood is not null
            mMySQLiteOpenHelper.updateData(mNewStoreMood); //update mNewStoreMood in last ID
        }

        if(difDay == 1 && mNewStoreMood != null){  //if 1 day difference and mNewStoreMood is not null
            mMySQLiteOpenHelper.recordDate(mNewStoreMood); ///insert mNewStoreMood in the table
        }

        if(difDay > 1 ){ // if more than one day difference
            addEmptyMood(difDay); //add empty mood
            if (mNewStoreMood != null){
                mMySQLiteOpenHelper.recordDate(mNewStoreMood);
            }
        }
    }

    /**
     * loop add empty Moods and record in the table depending on the number of days difference
     * @param pNbrDay day difference
     */
    public void addEmptyMood(int pNbrDay){
        for(int i=pNbrDay-1; i>0;i--){
            mEmptyMood = new StoreMood("", "", MyToolsDate.substractDay(i));
            mMySQLiteOpenHelper.recordDate(mEmptyMood);
        }
    }

    /**
     * return ID numbers present in the table
     * @return
     */
    public  int returnNumberId(){
        int numId=0;
        numId=mMySQLiteOpenHelper.returnNbreIDInTable();
        return numId;
    }

    /**
     * recover last date record in the table
     * @return last date record
     */
    public Date getLastDate(){
        Date lastDate= null;
        if(mMySQLiteOpenHelper.restoreLastData()!=null){ //if the last date record isn't null.
            lastDate=mMySQLiteOpenHelper.restoreLastData().getDate();  //recover last date recorded
        }
        return lastDate;
    }

    /**
     * created some mood empty according to the day difference between last date record and current date
     * recover all data of the table and stock in a list
     * @return
     */
    public List<StoreMood> restaureListMood(){
        List<StoreMood> storeMoods;

        storeMoods =mMySQLiteOpenHelper.restoreAllData();  //restore data from table

        return storeMoods;
    }

    /**
     * check if the last comment is to current date
     * if yes, show the last comment
     * else show empty comment
     */
    public String recoverLastComment() {
        String lastComment = "";
        if (returnNumberId() > 0) {
            lastComment = mMySQLiteOpenHelper.restoreLastData().getComment(); //restore last comment from the table

            if (!lastComment.isEmpty() && MyToolsDate.compareDate(new Date(), getLastDate()) == 0) {
            }
        }
        return lastComment;
    }
}

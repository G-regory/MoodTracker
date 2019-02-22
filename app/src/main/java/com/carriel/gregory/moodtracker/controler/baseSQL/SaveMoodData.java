package com.carriel.gregory.moodtracker.controler.baseSQL;

import android.content.Context;
import android.util.Log;

import com.carriel.gregory.moodtracker.controler.utils.MyToolsDate;
import com.carriel.gregory.moodtracker.model.StoreMood;

import java.util.Date;
import java.util.List;

public class SaveMoodData {

    private final String TAG= "MoodMessage: SaveMood";

    //********Model Class***********
    private StoreMood mNewStoreMood;
    private  StoreMood mLastStoreMood;
    private  StoreMood mEmptyMood;


    private static MySQLiteOpenHelper mMySQLiteOpenHelper;

    private static SaveMoodData ourInstance;

    public static SaveMoodData getInstance(Context context) {
        if(ourInstance==null){
            ourInstance= new SaveMoodData();
        }
        mMySQLiteOpenHelper=new MySQLiteOpenHelper(context);
        return ourInstance;
    }

    // --- SINGLETON ---
    private SaveMoodData() {}

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
            checkLastDateAndReturnEmptyMood(getDifferenceDays(date));
        }else{
            mMySQLiteOpenHelper.recordDate(mNewStoreMood);
        }
    }

    /**
     * recovers difference day
     * @param date current day
     * @return  difference last date and current date
     */
    private int getDifferenceDays(Date date) {
        int differenceDay;
        mLastStoreMood = mMySQLiteOpenHelper.restaureLastData();
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
    private void checkLastDateAndReturnEmptyMood(long differenceDays) {
        int difDay = (int) differenceDays;


        //if difday equal to 0  and newMood isn't null update newMood table
        if(difDay == 0 && mNewStoreMood != null){
            mMySQLiteOpenHelper.updateData(mNewStoreMood);
            Log.d(TAG, "checkLastDateAndReturnEmptyMood: juste update");
        }

        //if difday equal to 1  and newMood isn't null record newMood table
        if(difDay == 1 && mNewStoreMood != null){
            mMySQLiteOpenHelper.recordDate(mNewStoreMood);
            Log.d(TAG, "checkLastDateAndReturnEmptyMood: juste record");

        }

        //if difday is taller than 1 add emptyMood and else newMood isn't null record newMood table
        if(difDay > 1 ){
            addEmptyMood(difDay);
            Log.d(TAG, "checkLastDateAndReturnEmptyMood: add empty mood "+difDay);
            if (mNewStoreMood != null){
                mMySQLiteOpenHelper.recordDate(mNewStoreMood);
                Log.d(TAG, "checkLastDateAndReturnEmptyMood: juste record after add empty");
            }
        }


//        switch (difDay) {
//            case 0:
//                if (mNewStoreMood != null) {
//                    mMySQLiteOpenHelper.updateData(mNewStoreMood);
//                }
//                Log.d(TAG, "checkLastDateAndReturnEmptyMood: switch, here we are" + difDay);
//                break;
//            case 1:
//                if (mNewStoreMood != null) {
//                    mMySQLiteOpenHelper.recordDate(mNewStoreMood);
//                }
//                Log.d(TAG, "checkLastDateAndReturnEmptyMood: switch, here we are" + difDay);
//
//                break;
//            default:
//                addEmptyMood(difDay);
//                if (mNewStoreMood != null) {
//                    mMySQLiteOpenHelper.recordDate(mNewStoreMood);
//                }
//                Log.d(TAG, "checkLastDateAndReturnEmptyMood: switch, here we are" + difDay);
//                break;
//            case 3:
//                addEmptyMood(difDay);
//                if (mNewStoreMood != null) {
//                    mMySQLiteOpenHelper.recordDate(mNewStoreMood);
//                }
//                Log.d(TAG, "checkLastDateAndReturnEmptyMood: switch, here we are" + difDay);
//
//                break;
//            case 4:
//                addEmptyMood(difDay);
//                if (mNewStoreMood != null) {
//                    mMySQLiteOpenHelper.recordDate(mNewStoreMood);
//                }
//                Log.d(TAG, "checkLastDateAndReturnEmptyMood: switch, here we are" + difDay);
//
//                break;
//            case 5:
//                addEmptyMood(difDay);
//                if (mNewStoreMood != null) {
//                    mMySQLiteOpenHelper.recordDate(mNewStoreMood);
//                }
//                Log.d(TAG, "checkLastDateAndReturnEmptyMood: switch, here we are" + difDay);
//
//                break;
//            case 6:
//                addEmptyMood(difDay);
//                if (mNewStoreMood != null) {
//                    mMySQLiteOpenHelper.recordDate(mNewStoreMood);
//                }
//                Log.d(TAG, "checkLastDateAndReturnEmptyMood: switch, here we are" + difDay);
//
//                break;
//            case 7:
//                addEmptyMood(difDay);
//                if (mNewStoreMood != null) {
//                    mMySQLiteOpenHelper.recordDate(mNewStoreMood);
//                }
//                Log.d(TAG, "checkLastDateAndReturnEmptyMood: switch, here we are" + difDay);
//
//                break;
//        }

    }

    /**
     * loop add empty Moods and record in the table depending on the number of days difference
     * @param pNbrDay day difference
     */
    private void addEmptyMood(int pNbrDay){
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
        Date lastDate=mMySQLiteOpenHelper.restaureLastData().getDate();

        return lastDate;
    }

    /**
     * created some mood empty according to the day difference between last date record and current date
     * recover all data of the table and stock in a list
     * @param answerDifDay
     * @return
     */
    public List<StoreMood> restaureListMood(int answerDifDay){
        List<StoreMood> storeMoods;

        checkLastDateAndReturnEmptyMood(answerDifDay);

        storeMoods =mMySQLiteOpenHelper.restaureAllData();

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
            lastComment = mMySQLiteOpenHelper.restaureLastData().getComment();

            if (!lastComment.isEmpty() && MyToolsDate.compareDate(new Date(), getLastDate()) == 0) {
            }
        }
        return lastComment;
    }

}

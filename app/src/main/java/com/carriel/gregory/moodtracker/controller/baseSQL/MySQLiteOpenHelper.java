package com.carriel.gregory.moodtracker.controller.baseSQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.carriel.gregory.moodtracker.controller.utils.MyToolsDate;
import com.carriel.gregory.moodtracker.model.StoreMood;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private final String TAG = "MoodMessage :MySql";
    private SQLiteDatabase mSQLiteDatabase;
    private static final int VERSION_DB = 1;
    private static final String DB_NAME = "HistoryMood.db";
    private final String TABLE_NAME = "Mood";
    private final String COLUMN_ID = "_id";
    private final String COLUMN_DAY = "_date";
    private final String COLUMN_MOOD = "smiley";
    private final String COLUMN_COMMENT = "comment";

    private final String CREATE_TABLE = "create table  "
            + TABLE_NAME + " (" + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_DAY + " text,"+ COLUMN_MOOD + " text,"
            + COLUMN_COMMENT + " text) ";

    /**
     * Constructor
     *
     * @param context
     */
    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION_DB);
    }

    /**
     * create table
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    /**
     * update version table
     * drop current table
     * and create an new table
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String strSQL = "drop table if exists " + TABLE_NAME;
        db.execSQL(strSQL);
        onCreate(db);
    }

    /**
     * downgrade version table
     * drop current table
     * and create an new table
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String strSQL = "drop table if exists " + TABLE_NAME;
        db.execSQL(strSQL);
        onCreate(db);
    }

    /**
     * recover mood and the comment recorded in StoreMood
     * and insert in the table
     * @param pStoreMood
     */
    public void recordDate(StoreMood pStoreMood){
        String mDate2 = MyToolsDate.convertDatetoString(pStoreMood.getDate());
        String mMoodEmpty="";

        mMoodEmpty = controlMood(pStoreMood);
        try { mSQLiteDatabase=getWritableDatabase();
            ContentValues mContentValues= new ContentValues();
            mContentValues.put(COLUMN_DAY,mDate2);
            mContentValues.put(COLUMN_MOOD, mMoodEmpty);
            mContentValues.put(COLUMN_COMMENT, pStoreMood.getComment());
            mSQLiteDatabase.insert(TABLE_NAME, null,mContentValues);

        }catch (SQLException e){
            Log.d(TAG, "recordDate: failure");
        }



    }

    /**
     * check if current mood is empty
     * if isn't empty, input current Mood
     * else input "Bonne humeur"
     *
     * @param pStoreMood
     * @return mood
     */
    private String controlMood(StoreMood pStoreMood) {
        String mMoodEmpty;
        if (!pStoreMood.getMood().isEmpty()) {
            mMoodEmpty = pStoreMood.getMood();
        } else {
            mMoodEmpty = "Bonne humeur";
        }
        return mMoodEmpty;
    }

    /**
     * recover the last Data in this table
     * @return objet StoreMood
     */
    public StoreMood restoreLastData(){
        StoreMood dataLastStore=null;

        try{
            mSQLiteDatabase=getReadableDatabase();
            String StrSQL="Select * from "+TABLE_NAME+" ORDER BY "+COLUMN_ID+" DESC LIMIT 1" ;
            Cursor mCursor= mSQLiteDatabase.rawQuery(StrSQL,null);

            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){   //course table
                StoreMood dataStore=new StoreMood(mCursor.getString(mCursor.getColumnIndex(COLUMN_MOOD)),mCursor.getString(mCursor.getColumnIndex(COLUMN_COMMENT)), MyToolsDate.convertStringtoDate(mCursor.getString(mCursor.getColumnIndex(COLUMN_DAY))));  //recover data from table and store in dataStore object
                dataLastStore=dataStore;    //store last object
            }
            mCursor.close();
        }catch (SQLException e){
            Log.d(TAG, "restoreLastData: failure");
        }

        return dataLastStore;
    }


    /**
     * recover allData of the table by order descending and the store in a list of StoreMood,end return
     * @return list of StoreMood recover in the table
     */
    public List<StoreMood> restoreAllData(){
        List<StoreMood> storeDatas=new ArrayList<>();
        String todayString= MyToolsDate.convertDatetoString(new Date());

        try {
            mSQLiteDatabase=getReadableDatabase();
            String StrSQL="Select * from "+TABLE_NAME+" ORDER BY "+COLUMN_ID+" DESC limit 7" ;
            Cursor mCursor= mSQLiteDatabase.rawQuery(StrSQL,null);

            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){   //course table from beginning to the end
                StoreMood dataStore=new StoreMood(mCursor.getString(mCursor.getColumnIndex(COLUMN_MOOD)),mCursor.getString(mCursor.getColumnIndex(COLUMN_COMMENT)), MyToolsDate.convertStringtoDate(mCursor.getString(mCursor.getColumnIndex(COLUMN_DAY))));  //recover data of the table and store in dataStore object
                if(!dataStore.getDate().equals(MyToolsDate.convertStringtoDate(todayString))){
                        storeDatas.add(dataStore);//store each object in the list storeMood except current day

                }
            }
            mCursor.close();
        }catch (SQLException e){
            Log.d(TAG, "restoreAllData: failure");
        }

        return storeDatas;
    }

    /**
     * update the last ID, in the table if there is more 1 ID, else insert data normaly in this table
     *
     * @param mStoreMood
     */
    public void updateData(StoreMood mStoreMood) {
        mSQLiteDatabase = this.getWritableDatabase();
        String where = COLUMN_ID + "=" + returnNbreIDInTable();
        String mMood=controlMood(mStoreMood);

        String currentDate= MyToolsDate.convertDatetoString(mStoreMood.getDate());
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(COLUMN_DAY,currentDate );
        mContentValues.put(COLUMN_MOOD,mMood);
        if(!mStoreMood.getComment().isEmpty()){  //if current comment isn't empty, store in mContentValues
            mContentValues.put(COLUMN_COMMENT, mStoreMood.getComment());
        }
        try{
            if(returnNbreIDInTable()==0){  // if table is empty, insert data in table
                mSQLiteDatabase.insert(TABLE_NAME, null, mContentValues);
            }else{  //else update data in table
                mSQLiteDatabase.update(TABLE_NAME, mContentValues, where, null);
            }
        }catch (SQLException e){
            Log.d(TAG, "updateData: failure");
        }
    }

    /**
     * check contents of this table and return nomber
     * @return the id numbers recorded in this table
     */
    public int returnNbreIDInTable(){
        mSQLiteDatabase=getReadableDatabase();
        String StrSQL="select * from "+TABLE_NAME+" where exists (select * from "+TABLE_NAME+")";

        Cursor mCursor=mSQLiteDatabase.rawQuery(StrSQL,null );
        int id=mCursor.getCount();
        return id;
    }

}

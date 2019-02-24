package com.carriel.gregory.moodtracker.controler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.carriel.gregory.moodtracker.R;
import com.carriel.gregory.moodtracker.controler.utils.CustumDialog;
import com.carriel.gregory.moodtracker.controler.utils.MyToolsDate;

import java.util.Date;

import com.carriel.gregory.moodtracker.controler.baseSQL.SaveMoodData;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    //*******LOG********************
    private final String TAG= "MessageMood:Main";

    private static String mMood ="";
    private static String mComment ="";
    private SharedPreferences mSharedPreferences;
    private GestureDetector mGestureDetector;

    //*****counteur mMood*******
    private int mCountMood =0;

    //***********Constant String Mood*************************
    private final String SUPER_BONNE_HUMEUR = "Super bonne humeur";
    private final String BONNE_HUMEUR = "Bonne humeur";
    private final String HUMEUR_NORMALE = "Humeur normale";
    private final String MAUVAISE_HUMEUR = "Mauvaise humeur";
    private  final String TRES_MAUVAISE_HUMEUR = "Très mauvaise humeur";
    private final String SHARED_COUNTMOOD="mCountMood";
    private final String SHARED_COMMENT="mComment";

    //*********Views**************
    private ImageView mImageViewSmiley;
    private ConstraintLayout mConstraintLayout;
    private CustumDialog mCustumDialog;

    //*******access point to save & restore data to the DB
    private SaveMoodData mSaveMoodData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mImageViewSmiley=findViewById(R.id.activity_main_smiley_img);
        mConstraintLayout=findViewById(R.id.activity_main_layout);
        mGestureDetector= new GestureDetector(this,this);
        mCustumDialog= new CustumDialog(this);
        mSaveMoodData=SaveMoodData.getInstance(this);
        mSharedPreferences=getPreferences(MODE_PRIVATE);
    }

    /**
     * show popup dialog on clic buttonNote
     */
    public void buttonNote(View view) {
        mCustumDialog.setTitle("Commentaire"); //title of popup

        /**
         * events validate button
         * record mComment, mMood and date, show msg toast and hide popup
         */
        mCustumDialog.getOkButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //*****keep mComment***********
                mComment = mCustumDialog.getEditTextSubTitle().getText().toString();

                //*****record data******
                mSaveMoodData.recordMood(mMood, mComment, new Date());

                //******hide popup*************
                mCustumDialog.hide();

                //***affiche msg Toat*********
                Toast.makeText(getApplicationContext(),"Commentaire enregistré",Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * event clic button cancel
         * show msg toast and close popup
         */
        mCustumDialog.getCancelButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Commentaire annulé",Toast.LENGTH_SHORT).show();
                //***close popup*******
                mCustumDialog.cancel();
            }
        });
        //start screen popup
        mCustumDialog.build();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {}

    /**
     * informed event when the finger stay on the screen with this position of beginning motion, of end motion and speed movement
     * @param event1 position x and Y of start of touch
     * @param event2 position x and Y of end dof touch
     * @param velocityX  speed x
     * @param velocityY  speed y
     * @return
     */
    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        float startEventVertical=event1.getY(); // first event movement detect of screen
        float endEventVertical=event2.getY(); //event movement launch onFling

        float flingToUpOrDown = (startEventVertical - endEventVertical);
        checkMotion(flingToUpOrDown); //increments or decrements mCountMood
        switchMood();// change mMood depending mCountMood
        return true;
    }

    /**
     * check mMood of smiley from 1 to -3
     * (1 Super Bonne Humeur) - (-3 Très mauvaise mMood)
     * count increments if up and decrements if down
     * @param verticalFlingEvent result calcul fling allowing if define whe ou vers le basther up or down movement
     */
    private void checkMotion(float verticalFlingEvent) {
        if(verticalFlingEvent > 0){
            if(mCountMood == 1) {
            }else{
                mCountMood++;
            }
        }else {
            if(mCountMood == -3) {
            }else {
                mCountMood--;
            }
        }
    }

    /**
     * if mCountMood change, adapte image, backgroundcolor and initializes mMood variable
     *
     */
    private void switchMood() {
        switch (mCountMood){
            case 1:
                mMood = SUPER_BONNE_HUMEUR;
                mImageViewSmiley.setImageResource(R.drawable.smiley_super_happy);
                mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                break;
            case 0:
                mMood = BONNE_HUMEUR;
                mImageViewSmiley.setImageResource(R.drawable.smiley_happy);
                mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.light_sage));
                break;
            case -1:
                mMood = HUMEUR_NORMALE;
                mImageViewSmiley.setImageResource(R.drawable.smiley_normal);
                mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                break;
            case -2:
                mMood = MAUVAISE_HUMEUR;
                mImageViewSmiley.setImageResource(R.drawable.smiley_disappointed);
                mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.warm_grey));
                break;
            case -3:
                mMood = TRES_MAUVAISE_HUMEUR;
                mImageViewSmiley.setImageResource(R.drawable.smiley_sad);
                mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.faded_red));
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * button history, start activity_history
     * @param view button history
     */
    public void buttonHistory(View view) {
        Intent intentHistory = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intentHistory);
    }

    @Override
    protected void onResume() {
        controlMood(); //check if current mMood
        super.onResume();
    }

    @Override
    protected void onPause() {
        mSharedPreferences.edit().putInt(SHARED_COUNTMOOD, mCountMood).apply(); //save number countMood
        if(!mMood.equals(BONNE_HUMEUR)){  //if current mood  isn't default mood
            mSaveMoodData.recordMood(mMood, mComment, new Date());  //record current data in DB
        }
        super.onPause();
    }

    /**
     * check current mMood and current mComment
     */
    private void controlMood() {
        int numberID=mSaveMoodData.returnNumberId();  //recover number ID from table
        int countMood = mSharedPreferences.getInt(SHARED_COUNTMOOD,0);  //recover last Mood save and if don't find recover 0

        if(numberID >0 || countMood != 0){  //check if data stored on BD and if mood has changed
            Log.d(TAG, "controlMood: BD is greater than 0 ou l'humeur n'est plus l'humeur par défaut");
            if(MyToolsDate.compareDate(new Date(),mSaveMoodData.getLastDate()) ==0){ //if last date recorded is current day
                mCustumDialog.setEditTextSubTitle(mSaveMoodData.recoverLastComment()); //recover last comment from table
                mCountMood=countMood; //recover Mood from SharedPreferences
            }else{  // restore mMood and mComment by default
                mCustumDialog.setEditTextSubTitle("");
                mCountMood=0;
            }
        }
        switchMood();
    }

    /**
     * button share mood of day with other friend
     * @param view
     */
    public void shareButton(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String mood=mMood;
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sentence_share_mood)+mood);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.title_share_mood)));
    }
}

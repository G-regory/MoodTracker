package com.carriel.gregory.moodtracker.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.carriel.gregory.moodtracker.R;
import com.carriel.gregory.moodtracker.controller.utils.CustumDialog;
import com.carriel.gregory.moodtracker.controller.utils.MyToolsDate;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    //*******LOG********************
    private final String TAG= "MoodMessage:Main";

    private static String mMood ="";
    private static String mComment ="";
    private SharedPreferences mSharedPreferences;
    private GestureDetector mGestureDetector;
    private boolean isClick=false;

    //*****counteur mMood*******
    private int mCountMood =1;

    //***********Constant String Mood*************************
    private final String SUPER_BONNE_HUMEUR = "Super bonne humeur";
    private final String BONNE_HUMEUR = "Bonne humeur";
    private final String HUMEUR_NORMALE = "Humeur normale";
    private final String MAUVAISE_HUMEUR = "Mauvaise humeur";
    private  final String TRES_MAUVAISE_HUMEUR = "Très mauvaise humeur";
    private final String SHARED_COUNTMOOD="mCountMood";

    //*********Views**************
    private ImageView mImageViewSmiley;
    private ConstraintLayout mConstraintLayout;
    private CustumDialog mCustomDialog;

    //*******access point to save & restore data to the DB
    private DAO mDAO;

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
        mCustomDialog = new CustumDialog(this);
        mDAO = DAO.getInstance(this);
        mSharedPreferences=getPreferences(MODE_PRIVATE);
    }

    /**
     * show popup dialog on clic buttonNote
     */
    public void buttonNote(View view) {
        mCustomDialog.setTitle("Commentaire"); //title of popup

        /**
         * events validate button
         * record mComment, mMood and date, show msg toast and hide popup
         */
        mCustomDialog.getOkButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //*****keep mComment***********
                mComment = mCustomDialog.getEditTextSubTitle().getText().toString();

                //*****record data******
                mDAO.recordMood(mMood, mComment, new Date());

                //******hide popup*************
                mCustomDialog.hide();

                //***display msg Toast*********
                Toast.makeText(getApplicationContext(),"Commentaire enregistré",Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * event clic button cancel
         * show msg toast and close popup
         */
        mCustomDialog.getCancelButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Commentaire annulé",Toast.LENGTH_SHORT).show();
                //***close popup*******
                mCustomDialog.cancel();
            }
        });
        //start screen popup
        mCustomDialog.build();
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
     * @param event2 position x and Y of end of touch
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
            if(mCountMood == 0) {
            }else{
                mCountMood--;
            }
        }else {
            if(mCountMood == 4) {
            }else {
                mCountMood++;
            }
        }
    }

    /**
     * if mCountMood change, adapte image, backgroundcolor and initializes mMood variable
     *
     */
    private void switchMood() {
        int [] arrayDrawable={R.drawable.smiley_super_happy, R.drawable.smiley_happy, R.drawable.smiley_normal, R.drawable.smiley_disappointed, R.drawable.smiley_sad};

        int [] arrayColor={R.color.banana_yellow, R.color.light_sage, R.color.cornflower_blue_65, R.color.warm_grey, R.color.faded_red};

        String [] sentenceMood= {SUPER_BONNE_HUMEUR, BONNE_HUMEUR, HUMEUR_NORMALE, MAUVAISE_HUMEUR, TRES_MAUVAISE_HUMEUR};

        for(int i=0;i<sentenceMood.length;i++){
            if(mCountMood == i){
                mMood=sentenceMood[i];
                mImageViewSmiley.setImageResource(arrayDrawable[i]);
                mConstraintLayout.setBackgroundResource(arrayColor[i]);
            }
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
        isClick=true;
        Intent intentHistory = new Intent(MainActivity.this, HistoryDynamic.class);

        startActivity(intentHistory);
    }

    @Override
    protected void onResume() {
        isClick=false;
        controlMood(); //check if current mMood
        super.onResume();
    }

    /**
     * Save last mood before close application
     */
    @Override
    protected void onPause() {
        mSharedPreferences.edit().putString("date",MyToolsDate.convertDatetoString(new Date()));
        mSharedPreferences.edit().putInt(SHARED_COUNTMOOD, mCountMood).apply(); //save number countMood
        if(!mMood.equals(BONNE_HUMEUR) && !isClick){  //if current mood  isn't default mood
            mDAO.recordMood(mMood, mComment, new Date());  //record current data in DB
        }
        super.onPause();
    }

    /**
     * check current mMood and current mComment
     */
    private void controlMood() {
        int numberID= mDAO.returnNumberId();  //recover number ID from table
        int countMood = mSharedPreferences.getInt(SHARED_COUNTMOOD,1);  //recover last countMood saved

        if(numberID >0 && countMood != 1){  //check if data stored on BD and if mood has changed
            if(MyToolsDate.compareDate(new Date(), mDAO.getLastDate()) ==0){ //if last date recorded and current day are equal
                mCustomDialog.setEditTextSubTitle(mDAO.recoverLastComment()); //recover last comment from table
                mCountMood=countMood; //recover Mood from SharedPreferences
            }else{  // restore mMood and mComment by default
                mCustomDialog.setEditTextSubTitle("");
                mCountMood=1;
                if(mCustomDialog.isShowing()){
                    mCustomDialog.cancel();
                }
            }
        }
        switchMood();
    }

    /**
     * button share mood of day with other person
     * @param view button share
     */
    public void shareButton(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String mood=mMood;  //recovers mood and inserts into the variable mood
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sentence_share_mood)+mood);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.title_share_mood)));
    }
}

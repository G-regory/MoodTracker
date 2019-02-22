package com.carriel.gregory.moodtracker.controler;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.carriel.gregory.moodtracker.R;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private final String TAG= "MessageMood:Main";
    private static String mood ="";
    private static String comment ="";

    //*****counteur mood*******
    public int countMood =0;

    //***********Constant String Mood*************************
    private final String SUPER_BONNE_HUMEUR = "Super bonne humeur";
    private final String BONNE_HUMEUR = "Bonne humeur";
    private final String HUMEUR_NORMALE = "Humeur normale";
    private final String MAUVAISE_HUMEUR = "Mauvaise humeur";
    private  final String TRES_MAUVAISE_HUMEUR = "Très mauvaise humeur";

    //*********Views**************
    private ImageView mImageViewSmiley;
    private ConstraintLayout mConstraintLayout;
    private GestureDetector mGestureDetector;
    private CustumDialog mCustumDialog;


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
    }

    /**
     * show popup dialog on clic buttonNote
     */
    public void buttonNote(View view) {
        mCustumDialog.setTitle("Commentaire"); //title of popup

        /**
         * events validate button
         * record comment, mood and date, show msg toast and hide popup
         */
        mCustumDialog.getOkButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //*****keep comment***********
                comment = mCustumDialog.getEditTextSubTitle().getText().toString();
                //******hide popup*************
                mCustumDialog.hide();


                //***affiche msg Toat*********
                Toast.makeText(getApplicationContext(),"Commentaire enregistré",Toast.LENGTH_LONG).show();
            }
        });

        /**
         * event clic button cancel
         * show msg toast and close popup
         */
        mCustumDialog.getCancelButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Commentaire annulé",Toast.LENGTH_LONG).show();
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
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

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
        checkMotion(flingToUpOrDown); //increments or decrements countMood
        switchMood();// change mood depending countMood
        return true;
    }

    /**
     * check mood of smiley from 1 to -3
     * (1 Super Bonne Humeur) - (-3 Très mauvaise mood)
     * count increments if up and decrements if down
     * @param verticalFlingEvent result calcul fling allowing if define whe ou vers le basther up or down movement
     */
    private void checkMotion(float verticalFlingEvent) {
        if(verticalFlingEvent > 0){
            if(countMood == 1) {
            }else{
                countMood++;
            }
        }else {
            if(countMood == -3) {
            }else {
                countMood--;
            }
        }
    }

    /**
     * if countMood change, adapte image, backgroundcolor and initializes mood variable
     *
     */
    private void switchMood() {
        switch (countMood){
            case 1:
                mood = SUPER_BONNE_HUMEUR;
                mImageViewSmiley.setImageResource(R.drawable.smiley_super_happy);
                mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                break;
            case 0:
                mood = BONNE_HUMEUR;
                mImageViewSmiley.setImageResource(R.drawable.smiley_happy);
                mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.light_sage));
                break;
            case -1:
                mood = HUMEUR_NORMALE;
                mImageViewSmiley.setImageResource(R.drawable.smiley_normal);
                mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                break;
            case -2:
                mood = MAUVAISE_HUMEUR;
                mImageViewSmiley.setImageResource(R.drawable.smiley_disappointed);
                mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.warm_grey));
                break;
            case -3:
                mood = TRES_MAUVAISE_HUMEUR;
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
}

package com.carriel.gregory.moodtracker.controler;

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

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}

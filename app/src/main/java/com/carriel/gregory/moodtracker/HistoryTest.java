package com.carriel.gregory.moodtracker;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import static android.support.v4.view.ViewCompat.generateViewId;

public class HistoryTest extends AppCompatActivity {

    private final String TAG = "MoodMessage:History";


    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamiquelayout);

        mLinearLayout=findViewById(R.id.my_dynamique_layout);

//        FrameLayout mFrameLayout=new FrameLayout(this);
//        ViewGroup.LayoutParams layoutParams = new ActionBar.LayoutParams(288, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        LinearLayout.LayoutParams paramsTest = new LinearLayout.LayoutParams(
//        288, ViewGroup.LayoutParams.MATCH_PARENT);
//        paramsTest.weight = 1.0f;
//        mFrameLayout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
//        mLinearLayout.setWeightSum(1f);
//
//        mFrameLayout.setLayoutParams(paramsTest);
//
//        LinearLayout.LayoutParams paramsTest2 = new LinearLayout.LayoutParams(
//                288, 0);
//        paramsTest.weight = 1.0f;
//        mFrameLayout.setBackgroundColor(getResources().getColor(R.color.Black));
//
//        mFrameLayout.setLayoutParams(paramsTest2);
//
//        ImageButton imageButton = new ImageButton(this);
//        ViewGroup.LayoutParams imageParams= new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        imageButton.setLayoutParams(imageParams);
//        imageButton.setBackgroundResource(R.drawable.ic_comment_black_48px);
//
//        mLinearLayout.addView(mFrameLayout);
////        mLinearLayout.addView(imageButton);
//
//
//        int realSizeWidth = this.getResources().getDisplayMetrics().widthPixels;
//        int realSizeHeight = this.getResources().getDisplayMetrics().heightPixels;
//        Log.d(TAG, "onCreate: realSizeWidth "+realSizeWidth/2.4);
//        Log.d(TAG, "onCreate: realSizeHeight "+realSizeHeight);




    }
}

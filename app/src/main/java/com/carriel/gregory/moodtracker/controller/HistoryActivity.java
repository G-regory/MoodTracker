package com.carriel.gregory.moodtracker.controller;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carriel.gregory.moodtracker.R;
import com.carriel.gregory.moodtracker.controller.utils.MyToolsDate;
import com.carriel.gregory.moodtracker.model.StoreMood;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    public final String SUPER_BONNE_HUMEUR = "Super bonne humeur";
    public final String BONNE_HUMEUR = "Bonne humeur";
    public final String HUMEUR_NORMALE = "Humeur normale";
    public final String MAUVAISE_HUMEUR = "Mauvaise humeur";
    public final String TRES_MAUVAISE_HUMEUR = "Très mauvaise humeur";

    private final String TAG = "MoodMessage:History";
    private List<StoreMood> mStoreMoods;
    private DAO mDAO;

    private ConstraintLayout mConstraints;
    private ConstraintSet mConstraintSet;
    private RelativeLayout mRelativeLayoutDay1, mRelativeLayoutDay2,mRelativeLayoutDay3, mRelativeLayoutDay4,mRelativeLayoutDay5,mRelativeLayoutDay6,mRelativeLayoutDay7;
    private ImageButton mButtonComment1, mButtonComment2, mButtonComment3,mButtonComment4,mButtonComment5,  mButtonComment6, mButtonComment7;
    private TextView mTextViewConsole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();
        recoverMoods();
    }

    /**
     * initializes view RelativeLayout, ImageButton, Group, Constraintlayout
     * initializes class ConstraintSet and SaveMoodDataeur
     */
    private void init() {
        mConstraints=findViewById(R.id.activity_history_constraint_layout);
        mTextViewConsole =findViewById(R.id.activity_history_empty_txt);
        mConstraintSet= new ConstraintSet();
        mDAO = DAO.getInstance(this);
    }

    /**
     * Count ID number in the table
     * if NumberID is greater than 0, compare current date with last date recorded and return difference date
     * recover list StoreMood of all data
     * loop on each ID in this table, show a different color for each Mood and positions by date
     */
    public void recoverMoods(){
        int NumberID= mDAO.returnNumberId(); //recover number ID in table

        if (NumberID>0){ //if the number ID is greater than 0
            int answerDifDay =(int) MyToolsDate.compareDate(new Date(), mDAO.getLastDate()); //compare current date and last date recorded, and return the difference day

            if(NumberID ==1 && answerDifDay ==0){ //if current date
                mTextViewConsole.setVisibility(View.VISIBLE);  //show msg empty history
            }else{ //hide msg and show result
                mTextViewConsole.setVisibility(View.INVISIBLE);  //hide msg empty history

                mDAO.addEmptyMood(answerDifDay);
                mStoreMoods = mDAO.restaureListMood(); //recover list data
                int maxIndexMood = countMoodList(); //count number of items in the list

                //get the list of all layouts
                List<RelativeLayout> listRelativeLayout = returnListRelativeLayout();

                //get the list of all ImageButton
                List<ImageButton> listPopupDay = returnListButtonComment();

                //get the list of all layouts by id in the class R
                List<Integer> listIdLayout = returnListIdLayout();

                //loop on all view and the position by mood corresponding
                for (int i = 0; i < maxIndexMood; i++) {
                    showMood(listRelativeLayout.get(i), listPopupDay.get(i), mStoreMoods.get(i), listIdLayout.get(i));
                }
            }
        }else{
            mTextViewConsole.setVisibility(View.VISIBLE); //display msg history empty
        }
    }

    /**
     * check and count mood number
     * if less 7 Moods, return number Id, else return 7
     * @return
     */
    private int countMoodList(){
        int nbreId=0;
        int resultNumberListMood=0;

        if(mStoreMoods==null){} // if mood list and empty
        else{//else count numbre list
            resultNumberListMood=mStoreMoods.size();
            if(resultNumberListMood <7){
                nbreId=mStoreMoods.size();
            }else {
                nbreId=7;
            }
        }
        return nbreId;
    }

    /**
     * recover all the view line by line, and defines a color, padding and if ImageButton will be visible or not
     * by to the mood of the list and its its place
     * @param pLayout
     * @param pImageButton
     * @param pStoreMood
     * @param pIdLayout
     */
    private void showMood(RelativeLayout pLayout, ImageButton pImageButton, StoreMood pStoreMood, Integer pIdLayout) {
        mConstraintSet.clone(mConstraints);
        String Mood=pStoreMood.getMood();
        String comment=pStoreMood.getComment();

        List<Integer> listIdGuideLine= returnListIdGuideLine();

        switch (Mood){
            case (SUPER_BONNE_HUMEUR):
                pLayout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                mConstraintSet.connect(pIdLayout,ConstraintSet.END,listIdGuideLine.get(0),ConstraintSet.END,0);
                break;
            case (BONNE_HUMEUR):
                pLayout.setBackgroundColor(getResources().getColor(R.color.light_sage));
                mConstraintSet.connect(pIdLayout,ConstraintSet.END,listIdGuideLine.get(1),ConstraintSet.END,0);
                break;
            case (HUMEUR_NORMALE):
                pLayout.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                mConstraintSet.connect(pIdLayout,ConstraintSet.END,listIdGuideLine.get(2),ConstraintSet.END,0);
                break;
            case (MAUVAISE_HUMEUR):
                pLayout.setBackgroundColor(getResources().getColor(R.color.warm_grey));
                mConstraintSet.connect(pIdLayout,ConstraintSet.END,listIdGuideLine.get(3),ConstraintSet.END,0);
                break;
            case (TRES_MAUVAISE_HUMEUR):
                pLayout.setBackgroundColor(getResources().getColor(R.color.faded_red));
                mConstraintSet.connect(pIdLayout,ConstraintSet.END,listIdGuideLine.get(4),ConstraintSet.END,0);
                break;
        }
        mConstraintSet.applyTo(mConstraints);
        checkComment(comment, pImageButton); //check if a comment is present
    }

    /**
     * check if comment isn't empty for make visible ImageButton
     * show comment by a Toast.
     * else will be invisible
     * @param pcomment
     * @param pImageButton
     */
    private void checkComment(final String pcomment, ImageButton pImageButton) {
        if(pcomment.isEmpty()){
            pImageButton.setVisibility(View.INVISIBLE);
        }else {
            pImageButton.setVisibility(View.VISIBLE);
            pImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(HistoryActivity.this, pcomment, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * recover view relativeLayout and store in the list
     * @return ListRelativeLayout
     */
    private List<RelativeLayout> returnListRelativeLayout(){
        List<RelativeLayout> listRelativeLayout= new ArrayList<>(Arrays.asList(mRelativeLayoutDay1,mRelativeLayoutDay2,mRelativeLayoutDay3,mRelativeLayoutDay4,mRelativeLayoutDay5, mRelativeLayoutDay6, mRelativeLayoutDay7));

        for(int i=0;i<listRelativeLayout.size();i++){
            listRelativeLayout.set(i,(RelativeLayout) findViewById(returnListIdLayout().get(i)));
        }

        return listRelativeLayout;
    }

    /**
     * recover variable of class R id  to relativeLayout and store in the list
     * @return listIdLayout
     */
    private List<Integer> returnListIdLayout(){
        List<Integer> listIdLayout= Arrays.asList(R.id.activity_history_relative_layout_day_1,R.id.activity_history_relative_layout_day_2,R.id.activity_history_relative_layout_day_3,R.id.activity_history_relative_layout_day_4,R.id.activity_history_relative_layout_day_5,R.id.activity_history_relative_layout_day_6, R.id.activity_history_relative_layout_day_7);

        return listIdLayout;
    }

    /**
     * recover variable of class R id  des GuideLines and store in list
     * @return listIdGuideLine
     */
    private List<Integer> returnListIdGuideLine(){
        List<Integer> listIdGuideLine= Arrays.asList(R.id.activity_history_guidelinev1,R.id.activity_history_guidelinev2,R.id.activity_history_guidelinev3,R.id.activity_history_guidelinev4,R.id.activity_history_guidelinev5);

        return listIdGuideLine;
    }

    /**
     * recover views ImageButton which will contain the comments then the stocks in list
     * @return ListGroup
     */
    private List<ImageButton> returnListButtonComment(){
        List<ImageButton> listButtonComment= Arrays.asList(mButtonComment1,mButtonComment2,mButtonComment3,mButtonComment4,mButtonComment5,mButtonComment6,mButtonComment7);

        for(int i=0;i<listButtonComment.size();i++){
            listButtonComment.set(i,(ImageButton) findViewById(returnListIdButton().get(i)));
        }

        return listButtonComment;
    }

    /**
     * recover variable of class R id  to relativeLayout and store in the list
     * @return listIdLayout
     */
    private List<Integer> returnListIdButton(){
        List<Integer> listIdLayout= Arrays.asList(R.id.activity_history_btn_day_1,R.id.activity_history_btn_day_2,R.id.activity_history_btn_day_3,R.id.activity_history_btn_day_4,R.id.activity_history_btn_day_5,R.id.activity_history_btn_day_6, R.id.activity_history_btn_day_7);

        return listIdLayout;
    }

    @Override
    protected void onResume() {

        recoverMoods();
        super.onResume();
    }
}



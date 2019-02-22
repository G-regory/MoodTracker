package com.carriel.gregory.moodtracker.controler;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Group;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carriel.gregory.moodtracker.R;
import com.carriel.gregory.moodtracker.controler.baseSQL.SaveMoodData;
import com.carriel.gregory.moodtracker.controler.utils.MyToolsDate;
import com.carriel.gregory.moodtracker.model.StoreMood;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    public final String SUPER_BONNE_HUMEUR = "Super bonne humeur";
    public final String BONNE_HUMEUR = "Bonne humeur";
    public final String HUMEUR_NORMALE = "Humeur normale";
    public final String MAUVAISE_HUMEUR = "Mauvaise humeur";
    public final String TRÈS_MAUVAISE_HUMEUR = "Très mauvaise humeur";

    private final String TAG = "MoodMessage:History";
    private List<StoreMood> mStoreMoods;
    private SaveMoodData mSaveMoodData;

    private ConstraintLayout mConstraints;
    private ConstraintSet mConstraintSet;
    private RelativeLayout mRelativeLayoutDay1, mRelativeLayoutDay2,mRelativeLayoutDay3, mRelativeLayoutDay4,mRelativeLayoutDay5,mRelativeLayoutDay6,mRelativeLayoutDay7;
    private ImageButton mButtonComment1, mButtonComment2, mButtonComment3,mButtonComment4,mButtonComment5,mButtonComment6, mButtonComment7;
    private Group mGroupDay1, mGroupDay2, mGroupDay3, mGroupDay4, mGroupDay5, mGroupDay6, mGroupDay7;
    private TextView mTextViewConsol;

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
        mRelativeLayoutDay1=findViewById(R.id.activity_history_relative_layout_day_1);
        mRelativeLayoutDay2=findViewById(R.id.activity_history_relative_layout_day_2);
        mRelativeLayoutDay3=findViewById(R.id.activity_history_relative_layout_day_3);
        mRelativeLayoutDay4=findViewById(R.id.activity_history_relative_layout_day_4);
        mRelativeLayoutDay5=findViewById(R.id.activity_history_relative_layout_day_5);
        mRelativeLayoutDay6=findViewById(R.id.activity_history_relative_layout_day_6);
        mRelativeLayoutDay7=findViewById(R.id.activity_history_relative_layout_day_7);
        mButtonComment1=findViewById(R.id.activity_history_btn_day_1);
        mButtonComment2=findViewById(R.id.activity_history_btn_day_2);
        mButtonComment3=findViewById(R.id.activity_history_btn_day_3);
        mButtonComment4=findViewById(R.id.activity_history_btn_day_4);
        mButtonComment5=findViewById(R.id.activity_history_btn_day_5);
        mButtonComment6=findViewById(R.id.activity_history_btn_day_6);
        mButtonComment7=findViewById(R.id.activity_history_btn_day_7);
        mGroupDay1=findViewById(R.id.activity_history_group_day_1);
        mGroupDay2=findViewById(R.id.activity_history_group_day_2);
        mGroupDay3=findViewById(R.id.activity_history_group_day_3);
        mGroupDay4=findViewById(R.id.activity_history_group_day_4);
        mGroupDay5=findViewById(R.id.activity_history_group_day_5);
        mGroupDay6=findViewById(R.id.activity_history_group_day_6);
        mGroupDay7=findViewById(R.id.activity_history_group_day_7);
        mTextViewConsol=findViewById(R.id.activity_history_empty_txt);
        mConstraintSet= new ConstraintSet();
        mSaveMoodData=SaveMoodData.getInstance(this);
    }


    /**
     * Count ID number in the table
     * if NumberID is biggest 0, compare current date with last date recorded and return difference date
     * recover list StoreMood of alldata
     * loop on each ID in this table, show a different color for each Mood and positions by date
     */
    public void recoverMoods(){
        int NumberID=mSaveMoodData.returnNumberId();
        int answerDifDay;

        if (NumberID>0){ //if the ID number of the tables biggest 0
            answerDifDay =(int) MyToolsDate.compareDate(new Date(),mSaveMoodData.getLastDate()); //compare current date and last date recorded, and return the difference

            if(NumberID ==1 && answerDifDay ==0){ //if current date
                mTextViewConsol.setVisibility(View.VISIBLE);  //show msg empty history
            }else{ //hide msg and show result
                mTextViewConsol.setVisibility(View.INVISIBLE);  //hide msg empty history

                mStoreMoods = mSaveMoodData.restaureListMood(answerDifDay); //recover list data
                int maxIndexMood = countMoodList(); //count number of items in the list

                //get the list of all layouts
                List<RelativeLayout> listRelativeLayout = returnListRelativeLayout();

                //get the list of all groups(Text and Layout)
                List<Group> listGroupDay = returnListGroup();

                //get the list of all ImageButton
                List<ImageButton> listPopupDay = returnListButtonComment();

                //get the list of all layouts by id in the class R
                List<Integer> listIdLayout = returListIdLayout();

                //loop on all view and the position by mood corresponding
                for (int i = 0; i < maxIndexMood; i++) {
                    showMood(listRelativeLayout.get(i), listGroupDay.get(i), listPopupDay.get(i), mStoreMoods.get(i), listIdLayout.get(i));
                }
            }

        }else{
            mTextViewConsol.setVisibility(View.VISIBLE); //affiche msg historique vide
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
     * @param pGroup
     * @param pImageButton
     * @param pStoreMood
     * @param pIdLayout
     */
    private void showMood(RelativeLayout pLayout, Group pGroup, ImageButton pImageButton, StoreMood pStoreMood, Integer pIdLayout) {
        mConstraintSet.clone(mConstraints);
        String Mood="";
        String comment="";
        Mood=pStoreMood.getMood();

        comment=pStoreMood.getComment();
        List<Guideline> listGuideLine=returnListGuideLine();
        List<Integer> listIdGuideLine=returListIdGuideLine();
        switch (Mood){
            case (SUPER_BONNE_HUMEUR):
                pLayout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                listGuideLine.get(0).setVisibility(View.VISIBLE);   //Layout padding to 100%
                mConstraintSet.connect(pIdLayout,ConstraintSet.END,listIdGuideLine.get(0),ConstraintSet.END,0);
                mConstraintSet.applyTo(mConstraints);
                pGroup.setVisibility(View.VISIBLE);     //show text define the day and the layout
                checkComment(comment, pImageButton);    //check if a comment is present
                break;
            case (BONNE_HUMEUR):
                pLayout.setBackgroundColor(getResources().getColor(R.color.light_sage));
                listGuideLine.get(1).setVisibility(View.VISIBLE);   //Layout padding to 85%
                mConstraintSet.connect(pIdLayout,ConstraintSet.END,listIdGuideLine.get(1),ConstraintSet.END,0);
                mConstraintSet.applyTo(mConstraints);
                pGroup.setVisibility(View.VISIBLE);
                checkComment(comment, pImageButton);
                break;
            case (HUMEUR_NORMALE):
                pLayout.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                listGuideLine.get(2).setVisibility(View.VISIBLE);   //Layout padding to 65%
                mConstraintSet.connect(pIdLayout,ConstraintSet.END,listIdGuideLine.get(2),ConstraintSet.END,0);
                mConstraintSet.applyTo(mConstraints);
                pGroup.setVisibility(View.VISIBLE);
                checkComment(comment, pImageButton);
                break;
            case (MAUVAISE_HUMEUR):
                pLayout.setBackgroundColor(getResources().getColor(R.color.warm_grey));
                listGuideLine.get(3).setVisibility(View.VISIBLE);   //Layout padding to 45%
                mConstraintSet.connect(pIdLayout,ConstraintSet.END,listIdGuideLine.get(3),ConstraintSet.END,0);
                mConstraintSet.applyTo(mConstraints);
                pGroup.setVisibility(View.VISIBLE);
                checkComment(comment, pImageButton);
                break;
            case (TRÈS_MAUVAISE_HUMEUR):
                pLayout.setBackgroundColor(getResources().getColor(R.color.faded_red));
                listGuideLine.get(4).setVisibility(View.VISIBLE);   //Layout padding to 25%
                mConstraintSet.connect(pIdLayout,ConstraintSet.END,listIdGuideLine.get(4),ConstraintSet.END,0);
                mConstraintSet.applyTo(mConstraints);
                pGroup.setVisibility(View.VISIBLE);
                checkComment(comment, pImageButton);
                break;
        }
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
     * recover view Guideline and store in the list
     * Guideline allows to fill according to Mood the bar of layout
     * @return listGuideLine
     */
    private List<Guideline> returnListGuideLine() {
        List<Guideline> listGuideLine= new ArrayList<>();

        listGuideLine.add((Guideline) findViewById(R.id.activity_history_guidelinev1));
        listGuideLine.add((Guideline) findViewById(R.id.activity_history_guidelinev2));
        listGuideLine.add((Guideline) findViewById(R.id.activity_history_guidelinev3));
        listGuideLine.add((Guideline) findViewById(R.id.activity_history_guidelinev4));
        listGuideLine.add((Guideline) findViewById(R.id.activity_history_guidelinev5));

        return listGuideLine;
    }

    /**
     * recover view relativeLayout and store in the list
     * @return ListRelativeLayout
     */
    private List<RelativeLayout> returnListRelativeLayout(){
        List<RelativeLayout> listRelativeLayout= new ArrayList<>();
        listRelativeLayout.add(mRelativeLayoutDay1);
        listRelativeLayout.add(mRelativeLayoutDay2);
        listRelativeLayout.add(mRelativeLayoutDay3);
        listRelativeLayout.add(mRelativeLayoutDay4);
        listRelativeLayout.add(mRelativeLayoutDay5);
        listRelativeLayout.add(mRelativeLayoutDay6);
        listRelativeLayout.add(mRelativeLayoutDay7);

        return listRelativeLayout;
    }

    /**
     * recover variable of class R id  to relativeLayout and store in the list
     * @return listIdLayout
     */
    private List<Integer> returListIdLayout(){
        List<Integer> listIdLayout= new ArrayList<>();
        listIdLayout.add(R.id.activity_history_relative_layout_day_1);
        listIdLayout.add(R.id.activity_history_relative_layout_day_2);
        listIdLayout.add(R.id.activity_history_relative_layout_day_3);
        listIdLayout.add(R.id.activity_history_relative_layout_day_4);
        listIdLayout.add(R.id.activity_history_relative_layout_day_5);
        listIdLayout.add(R.id.activity_history_relative_layout_day_6);
        listIdLayout.add(R.id.activity_history_relative_layout_day_7);

        return listIdLayout;
    }

    /**
     * recover variable of class R id  des GuideLines and store in list
     * @return listIdGuideLine
     */
    private List<Integer> returListIdGuideLine(){
        List<Integer> listIdGuideLine= new ArrayList<>();
        listIdGuideLine.add(R.id.activity_history_guidelinev1);
        listIdGuideLine.add(R.id.activity_history_guidelinev2);
        listIdGuideLine.add(R.id.activity_history_guidelinev3);
        listIdGuideLine.add(R.id.activity_history_guidelinev4);
        listIdGuideLine.add(R.id.activity_history_guidelinev5);

        return listIdGuideLine;
    }

    /**
     * recover views Groups include TextViews and RelativeLayout, and store in list
     * @return ListGroup
     */
    private List<Group> returnListGroup(){
        List<Group> listGroup= new ArrayList<>();
        listGroup.add(mGroupDay1);
        listGroup.add(mGroupDay2);
        listGroup.add(mGroupDay3);
        listGroup.add(mGroupDay4);
        listGroup.add(mGroupDay5);
        listGroup.add(mGroupDay6);
        listGroup.add(mGroupDay7);

        return listGroup;
    }

    /**
     * recover views ImageButton which will contain the comments then the stocks in list
     * @return ListGroup
     */
    private List<ImageButton> returnListButtonComment(){
        List<ImageButton> listButtonComment= new ArrayList<>();
        listButtonComment.add(mButtonComment1);
        listButtonComment.add(mButtonComment2);
        listButtonComment.add(mButtonComment3);
        listButtonComment.add(mButtonComment4);
        listButtonComment.add(mButtonComment5);
        listButtonComment.add(mButtonComment6);
        listButtonComment.add(mButtonComment7);

        return listButtonComment;
    }

}



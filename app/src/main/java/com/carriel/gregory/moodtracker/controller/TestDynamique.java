package com.carriel.gregory.moodtracker.controller;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.carriel.gregory.moodtracker.R;
import com.carriel.gregory.moodtracker.controller.utils.MyToolsDate;
import com.carriel.gregory.moodtracker.model.StoreMood;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDynamique extends AppCompatActivity {

    public final String SUPER_BONNE_HUMEUR = "Super bonne humeur";
    public final String BONNE_HUMEUR = "Bonne humeur";
    public final String HUMEUR_NORMALE = "Humeur normale";
    public final String MAUVAISE_HUMEUR = "Mauvaise humeur";
    public final String TRES_MAUVAISE_HUMEUR = "Très mauvaise humeur";

    private final String TAG = "MoodMessage:History";
    public int widthScreen;
    public int heightScreen;
    int emptySpace;
    private List<StoreMood> mStoreMoods;
    private LinearLayout mLinearLayout;
    private ImageButton mButtonComment1, mButtonComment2, mButtonComment3,mButtonComment4,mButtonComment5,  mButtonComment6, mButtonComment7;

    private TextView mTextViewConsole;

    private DAO mDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dynamique);
        init();

        catchSizeScreen();
    }

    /**
     *measure the size of the layout and insert
     * the width screen in widthScreen
     * and
     * the height screen in heightScreen
     */
    private void catchSizeScreen() {
        mLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                heightScreen= mLinearLayout.getHeight(); //height is ready
                widthScreen=mLinearLayout.getWidth();
                recoverMoods();
            }
        });
    }

    private void init() {
        mLinearLayout=findViewById(R.id.linearlayout);
        mDAO = DAO.getInstance(this);
    }

    /**
     * display history in the view
     */
    private void displayHistory() {

        Map<String, Integer> moodMap = new HashMap<>();
        int[] color={R.color.banana_yellow, R.color.light_sage, R.color.cornflower_blue_65, R.color.warm_grey, R.color.faded_red};

        int [] sentence={R.string.hier, R.string.avant_hier, R.string.trois_jour, R.string.quatre_jour, R.string.cinq_jour,R.string.six_jour,R.string.une_semaine};

        moodMap.put(SUPER_BONNE_HUMEUR, 0);
        moodMap.put(BONNE_HUMEUR, 1);
        moodMap.put(HUMEUR_NORMALE, 2);
        moodMap.put(MAUVAISE_HUMEUR, 3);
        moodMap.put(TRES_MAUVAISE_HUMEUR, 4);


        int j=0;
        for(int i=0; i<7;i++){
            if(i>=7-emptySpace) {
                createEmptyLayout(i);
            }else {

                ViewGroup.LayoutParams layoutParams = new ActionBar.LayoutParams(countMoodForWidthLayout(4), countDayForHeightLayout());
                ViewGroup.LayoutParams textParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ViewGroup.LayoutParams buttonParams=new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                RelativeLayout mRelativeLayout=new RelativeLayout(TestDynamique.this);
                mRelativeLayout.setLayoutParams(layoutParams);
                TextView mTextView=new TextView(TestDynamique.this);
                mTextView.setTextAppearance(this, R.style.TextHistoryStyle);
                mTextView.setLayoutParams(textParams);
                mTextView.setText(sentence[i]);
                ImageButton mButton = new ImageButton(this);
                mButton.setLayoutParams(buttonParams);

                    mRelativeLayout.setBackgroundResource(color[moodMap.get(TRES_MAUVAISE_HUMEUR)]);

                mLinearLayout.addView(mRelativeLayout,mLinearLayout.getChildCount() - i);
                mRelativeLayout.addView(mTextView);
                mRelativeLayout.addView(mButton);
            }
        }
    }

    /**
     * create empty RelativeLayout for hide empty space
     * @param i counter from loop
     */
    private void createEmptyLayout(int i) {
        ViewGroup.LayoutParams layoutEmptyParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, countDayForHeightLayout());
        RelativeLayout mRelativeLayoutEmpty=new RelativeLayout(TestDynamique.this);

        mRelativeLayoutEmpty.setLayoutParams(layoutEmptyParams);
        mRelativeLayoutEmpty.setAlpha( 0.9f );
        mLinearLayout.addView(mRelativeLayoutEmpty,mLinearLayout.getChildCount() - i);
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
//                mTextViewConsole.setVisibility(View.VISIBLE);  //show msg empty history
                createEmptyLayout(1);
            }else{ //hide msg and show result
//                mTextViewConsole.setVisibility(View.INVISIBLE);  //hide msg empty history

                mDAO.addEmptyMood(answerDifDay);
                mStoreMoods = mDAO.restaureListMood(); //recover list data
                final int maxIndexMood = countMoodList(); //count number of items in the list

                displayHistory();

            }
        }else{
//            mTextViewConsole.setVisibility(View.VISIBLE); //display msg history empty
        }
    }

    /**
     * check and count mood number
     * if less 7 Moods, return number Id,
     * and subtract number Id by 7
     * else return 7
     * @return return number Id
     */
    private int countMoodList(){
        int nbreId=0;
        int resultNumberListMood;

        if(mStoreMoods==null){} // if mood list and empty
        else{//else count numbre list
            resultNumberListMood=mStoreMoods.size();
            if(resultNumberListMood <7){
                nbreId=mStoreMoods.size();
                emptySpace=7-nbreId;

            }else {
                nbreId=7;
            }
        }
        return nbreId;
    }

    /**
     * count the number of moods available to divide the width
     * @param pMoodRecover number of moods available
     * @return the width distributed according to the number of moods
     */
    private int countMoodForWidthLayout(int pMoodRecover){
        int TOTAL_NUMBER_MOODS = 5;
        int sizeforWidthLayout=(this.widthScreen/ TOTAL_NUMBER_MOODS)*pMoodRecover;
        return sizeforWidthLayout;
    }

    /**
     * count the number of days to divide the height
     * @return the height distributed according to the number of days
     */
    private int countDayForHeightLayout(){
        int TOTAL_NUMBER_DAY = 7;
        int sizeforHeightLayout=(this.heightScreen/ TOTAL_NUMBER_DAY);
        return sizeforHeightLayout;
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
                    Toast.makeText(TestDynamique.this, pcomment, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}

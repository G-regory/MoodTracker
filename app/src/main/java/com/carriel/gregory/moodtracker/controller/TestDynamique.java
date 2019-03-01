package com.carriel.gregory.moodtracker.controller;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    private final String SUPER_BONNE_HUMEUR = "Super bonne humeur";
    private final String BONNE_HUMEUR = "Bonne humeur";
    private final String HUMEUR_NORMALE = "Humeur normale";
    private final String MAUVAISE_HUMEUR = "Mauvaise humeur";
    private final String TRES_MAUVAISE_HUMEUR = "Très mauvaise humeur";
    private final int TOTAL_NUMBER_DAY = 7;

    private final String TAG = "MoodMessage:History";
    private int widthScreen;
    private int heightScreen;
    private int emptySpace;
    private List<StoreMood> mStoreMoods;
    private LinearLayout mLinearLayout;

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
                widthScreen=mLinearLayout.getWidth();   //width is ready
                recoverMoods();
            }
        });
    }

    private void init() {
        mLinearLayout=findViewById(R.id.linearlayout);
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

                Toast.makeText(this, "History vide", Toast.LENGTH_SHORT).show();//

            }else{

                mDAO.addEmptyMood(answerDifDay);
                mStoreMoods = mDAO.restaureListMood(); //recover list data
                final int maxIndexMood = countMoodList(); //count number of items in the list

                displayHistory(maxIndexMood);
            }
        }else{
            Toast.makeText(this, "History vide", Toast.LENGTH_SHORT).show(); //display msg history empty
        }
    }

    /**
     * display history in the view
     * @param maxIndexMood
     */
    private void displayHistory(int maxIndexMood) {

        Map<String, Integer> moodMap = new HashMap<>();
        int[] color={R.color.faded_red, R.color.warm_grey, R.color.cornflower_blue_65, R.color.light_sage, R.color.banana_yellow};

        int [] sentence={R.string.hier, R.string.avant_hier, R.string.trois_jour, R.string.quatre_jour, R.string.cinq_jour,R.string.six_jour,R.string.une_semaine};

        moodMap.put(SUPER_BONNE_HUMEUR, 4);
        moodMap.put(BONNE_HUMEUR, 3);
        moodMap.put(HUMEUR_NORMALE, 2);
        moodMap.put(MAUVAISE_HUMEUR, 1);
        moodMap.put(TRES_MAUVAISE_HUMEUR, 0);

        int j=0;
        for(int i=0; i<TOTAL_NUMBER_DAY;i++){
            if(i>=TOTAL_NUMBER_DAY-emptySpace) {
                createEmptyLayout(i);
            }else {
                if(i<maxIndexMood){
                    String mood=mStoreMoods.get(i).getMood();
                    int countMood= moodMap.get(mood);
                    RelativeLayout mRelativeLayout = getRelativeLayout(color[countMood],moodMap.get(mood));

                    TextView mTextView = getTextView(sentence[i]);

                    ImageButton mButton = getImageButton(mStoreMoods.get(i).getComment());
                    checkComment(mStoreMoods.get(i).getComment(),mButton, mRelativeLayout);


                    mLinearLayout.addView(mRelativeLayout,mLinearLayout.getChildCount() - i);
                    mRelativeLayout.addView(mTextView);
                }
            }
        }
    }
    /**
     * create a widget relative layout with params
     * @param resid number of mood
     * @param integer
     * @return
     */
    private RelativeLayout getRelativeLayout(int resid, Integer integer) {
        ViewGroup.LayoutParams layoutParams = new ActionBar.LayoutParams(countMoodForWidthLayout(integer), countDayForHeightLayout());
        RelativeLayout mRelativeLayout=new RelativeLayout(TestDynamique.this);
        mRelativeLayout.setLayoutParams(layoutParams);
        mRelativeLayout.setBackgroundResource(resid);
        return mRelativeLayout;
    }

    /**
     * create a widget textview with params
     * @param resid number day to display
     * @return
     */
    private TextView getTextView(int resid) {
        ViewGroup.LayoutParams textParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView mTextView=new TextView(TestDynamique.this);
        mTextView.setTextAppearance(this, R.style.TextHistoryStyle);
        mTextView.setId(R.id.text);
        ((ActionBar.LayoutParams) textParams).setMarginStart(15);
        mTextView.setLayoutParams(textParams);
        mTextView.setText(resid);
        return mTextView;
    }

    /**
     * create a widget imagebutton with params
     * @return
     */
    private ImageButton getImageButton(final String pComment) {
        RelativeLayout.LayoutParams ButtonParams= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageButton mButton = new ImageButton(this);
        ButtonParams.addRule(RelativeLayout.BELOW,R.id.text);
        ButtonParams.setMarginEnd(20);
        ButtonParams.topMargin=50;
        ButtonParams.addRule(RelativeLayout.ALIGN_PARENT_END);

        mButton.setLayoutParams(ButtonParams);
        mButton.setBackgroundResource(R.drawable.ic_comment_black_48px);

        return mButton;
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
            if(resultNumberListMood <TOTAL_NUMBER_DAY){
                nbreId=mStoreMoods.size();
                emptySpace=TOTAL_NUMBER_DAY-nbreId;

            }else {
                nbreId=TOTAL_NUMBER_DAY;
            }
        }
        return nbreId;
    }

    /**
     * count the number of moods available to divide the width
     * @param pMoodRecover number of moods available
     * @return the width distributed according to the number of moods
     */
    private int countMoodForWidthLayout(int  pMoodRecover){
        int TOTAL_NUMBER_MOODS = 5;
        int sizeforWidthLayout=(this.widthScreen/ TOTAL_NUMBER_MOODS)*(pMoodRecover+1);
        return sizeforWidthLayout;
    }

    /**
     * count the number of days to divide the height
     * @return the height distributed according to the number of days
     */
    private int countDayForHeightLayout(){

        int sizeforHeightLayout=(this.heightScreen/ TOTAL_NUMBER_DAY);
        return sizeforHeightLayout;
    }

    /**
     * check if comment isn't empty for add view ImageButton in relative layout
     * and display comment in a Toast.
     * @param pComment comment of
     * @param pImageButton
     * @param mRelativeLayout
     */
    private void checkComment(final String pComment, ImageButton pImageButton, RelativeLayout mRelativeLayout) {
        if(!pComment.isEmpty()){
            mRelativeLayout.addView(pImageButton);
            pImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(TestDynamique.this, pComment, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}



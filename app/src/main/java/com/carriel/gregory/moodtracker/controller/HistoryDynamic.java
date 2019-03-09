package com.carriel.gregory.moodtracker.controller;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class HistoryDynamic extends AppCompatActivity {

    private final String SUPER_BONNE_HUMEUR = "Super bonne humeur";
    private final String BONNE_HUMEUR = "Bonne humeur";
    private final String HUMEUR_NORMALE = "Humeur normale";
    private final String MAUVAISE_HUMEUR = "Mauvaise humeur";
    private final String TRES_MAUVAISE_HUMEUR = "Très mauvaise humeur";
    public final int TOTAL_NUMBER_DAY = 7;

    private final String TAG = "MoodMessage:History";
    public int widthScreen;
    public int heightScreen;
    private   int centerPositionButton;
    public LinearLayout mLinearLayout;
    private List<StoreMood> mStoreMoods;
    public int emptySpace; //

    private DAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_dynamique);
        mLinearLayout=findViewById(R.id.linearLayout);
        mDAO = DAO.getInstance(this);
        catchSizeScreen(mLinearLayout);  //measure screen size
    }

    /**
     *measure the size of the layout and insert
     * the width screen in widthScreen
     * and
     * the height screen in heightScreen
     * @param viewGroup
     */
    private void catchSizeScreen(final ViewGroup viewGroup) {
        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                heightScreen= viewGroup.getHeight(); //height is ready
                widthScreen=viewGroup.getWidth();   //width is ready
                centerPositionButton =(heightScreen/TOTAL_NUMBER_DAY)/3;
                recoverMoods();
            }
        });
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

            if(NumberID ==1 && answerDifDay ==0){ //if there is 1 ID in table & the last is current date
                Toast.makeText(this, "History vide", Toast.LENGTH_SHORT).show(); //Toast msg
            }else{ // more 1 ID & the last day isn't
                mDAO.addEmptyMood(answerDifDay);
                mStoreMoods = mDAO.restaureListMood(); //recover list data
                final int elementMoodList = countMoodList(); //count number of items in the list

                displayHistory(elementMoodList);
            }
        }else{
            Toast.makeText(this, "History vide", Toast.LENGTH_SHORT).show(); //display msg history empty
        }
    }

    /**
     * display history in the view
     * @param itemsListMood
     */
    private void displayHistory(int itemsListMood) {
        Map<String, Integer> moodMap = new HashMap<>();
        String [] sentenceMood= {TRES_MAUVAISE_HUMEUR, MAUVAISE_HUMEUR, HUMEUR_NORMALE, BONNE_HUMEUR, SUPER_BONNE_HUMEUR};
        int[] arrayColor={R.color.faded_red, R.color.warm_grey, R.color.cornflower_blue_65, R.color.light_sage, R.color.banana_yellow};

        for(int i=0; i<sentenceMood.length;i++){ //loop for input sentenceMood in Map moodMap
            moodMap.put(sentenceMood[i], i);// key is an item of sentenceMood and value is an number of arrayColor
        }

        int [] sentenceDayOfWeek={R.string.hier, R.string.avant_hier, R.string.trois_jour, R.string.quatre_jour, R.string.cinq_jour,R.string.six_jour,R.string.une_semaine};

        for(int i=0; i<TOTAL_NUMBER_DAY;i++){ //loop 7 times
            if(i<itemsListMood){
                String mood=mStoreMoods.get(i).getMood();
                int countMood= moodMap.get(mood);
                RelativeLayout mRelativeLayout = getRelativeLayout(arrayColor[countMood],moodMap.get(mood));

                TextView mTextView = getTextView(sentenceDayOfWeek[i]);

                ImageButton mButton = getImageButton();

                mLinearLayout.addView(mRelativeLayout,mLinearLayout.getChildCount() - i);
                mRelativeLayout.addView(mTextView);
                checkComment(mStoreMoods.get(i).getComment(),mButton, mRelativeLayout);
            }else{
                createEmptyLayout(i);
            }
        }
    }

    /**
     * create a widget relative layout with params
     * @param colorLayout number of mood
     * @param positionMood
     * @return
     */
    private RelativeLayout getRelativeLayout(int colorLayout, Integer positionMood) {
        ViewGroup.LayoutParams layoutParams = new ActionBar.LayoutParams(countMoodForWidthLayout(positionMood), countDayForHeightLayout());
        RelativeLayout mRelativeLayout=new RelativeLayout(HistoryDynamic.this);
        mRelativeLayout.setLayoutParams(layoutParams);
        mRelativeLayout.setBackgroundResource(colorLayout);

        return mRelativeLayout;
    }

    /**
     * create a widget textview with params
     * @param idSentenceDayOfWeek number day to display
     * @return
     */
    private TextView getTextView(int idSentenceDayOfWeek) {
        ViewGroup.LayoutParams textParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView mTextView=new TextView(HistoryDynamic.this);
        mTextView.setTextAppearance(this, R.style.TextHistoryStyle);
        ((ActionBar.LayoutParams) textParams).setMarginStart(10);
        mTextView.setId(R.id.text);
        mTextView.setLayoutParams(textParams);
        mTextView.setText(idSentenceDayOfWeek);

        return mTextView;
    }

    /**
     * create a widget imagebutton with params
     * @return
     */
    private ImageButton getImageButton() {
        RelativeLayout.LayoutParams ButtonParams= new RelativeLayout.LayoutParams((int)getResources().getDimension(R.dimen.size_width),(int)getResources().getDimension(R.dimen.size_height));
        ImageButton mButton = new ImageButton(this);
        ButtonParams.setMarginEnd((int)getResources().getDimension(R.dimen.margin_end_btn_history_activity));
        ButtonParams.topMargin= centerPositionButton;
        ButtonParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        mButton.setLayoutParams(ButtonParams);
        mButton.setBackgroundResource(R.drawable.ic_comment_black_48px);

        return mButton;
    }

    /**
     * create empty RelativeLayout for hide empty space
     * @param dayNotRecord counter from loop
     */
    private void createEmptyLayout(int dayNotRecord) {
        ViewGroup.LayoutParams layoutEmptyParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, countDayForHeightLayout());
        RelativeLayout mRelativeLayoutEmpty=new RelativeLayout(HistoryDynamic.this);
        mRelativeLayoutEmpty.setLayoutParams(layoutEmptyParams);
        mRelativeLayoutEmpty.setAlpha( 0.9f ); //transparent color
        mLinearLayout.addView(mRelativeLayoutEmpty,mLinearLayout.getChildCount() - dayNotRecord);
    }

    /**
     * check and count mood number
     * if less 7 Moods, return number Id,
     * and subtract number Id by 7
     * else return 7
     * @return return number Id
     */
    private int countMoodList(){
        int numberId=0;
        int totalNumberMoodList;

        if(mStoreMoods!=null){
            //else count number list
            totalNumberMoodList=mStoreMoods.size();
            if(totalNumberMoodList <TOTAL_NUMBER_DAY){
                numberId=mStoreMoods.size();
                emptySpace=TOTAL_NUMBER_DAY-numberId;
            }else {
                numberId=TOTAL_NUMBER_DAY;
            }
        }

        return numberId;
    }

    /**
     * count the number of moods available to divide the width
     * @param pMoodRecover number of moods available
     * @return the width distributed according to the number of moods
     */
    private int countMoodForWidthLayout(int  pMoodRecover){
        int TOTAL_NUMBER_MOODS = 5;
        int sizeForWidthLayout=(this.widthScreen/ TOTAL_NUMBER_MOODS)*(pMoodRecover+1);
        return sizeForWidthLayout;
    }

    /**
     * count the number of days to divide the height
     * @return the height distributed according to the number of days
     */
    private int countDayForHeightLayout(){
        int sizeForHeightLayout=(this.heightScreen/ TOTAL_NUMBER_DAY);
        return sizeForHeightLayout;
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
                    Toast.makeText(HistoryDynamic.this, pComment, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    /**
     *
     */
    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}



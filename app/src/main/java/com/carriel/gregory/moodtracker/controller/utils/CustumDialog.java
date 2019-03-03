package com.carriel.gregory.moodtracker.controller.utils;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.carriel.gregory.moodtracker.R;

public class CustumDialog extends Dialog {
    //field
    private String title;
    private String subTitle;
    private Button mOkButton;
    private Button mCancelButton;
    private TextView mTextViewTitle;
    private EditText mEditTextSubTitle;

    public CustumDialog(Activity activity){
        super(activity, R.style.Theme_AppCompat_Dialog_Alert);
        setContentView(R.layout.my_note_layout);
        mOkButton= findViewById(R.id.mynote_ok_btn);
        mCancelButton= findViewById(R.id.mynote_cancel_btn);
        mTextViewTitle= findViewById(R.id.mynote_title_txt);
        mEditTextSubTitle = findViewById(R.id.mynote_subtitle_txt);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setEditTextSubTitle(String textSubTitle) {
        mEditTextSubTitle.setText(textSubTitle);
    }

    public EditText getEditTextSubTitle() {
        return mEditTextSubTitle;
    }

    public Button getOkButton() {
        return mOkButton;
    }

    public Button getCancelButton() {
        return mCancelButton;
    }

    public void build(){
        show();
        mTextViewTitle.setText("Commentaire");
    }
}

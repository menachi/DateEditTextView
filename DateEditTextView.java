package com.example.eliavmenachi.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by menachi on 02/05/2018.
 */

interface DateEditTextViewDialogListener{
    void onDateSet(int y, int m, int d);
}

public class DateEditTextView extends EditText implements DateEditTextViewDialogListener {
    int year = 0;
    int day;
    int month;

    public DateEditTextView(Context context) {
        super(context);
        init();
    }

    public DateEditTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DateEditTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        setText("" + day + "/" + (month) + "/" + year);
    }

    @Override
    public void createContextMenu(ContextMenu menu) {
        super.createContextMenu(menu);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        String txt = getText().toString();
        String[] strs = txt.split("/");
        if (strs.length == 3) {
            year = Integer.valueOf(strs[2]);
            day = Integer.valueOf(strs[0]);
            month = Integer.valueOf(strs[1]);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            Log.d("TAG" , "ACTION_DOWN");
            TestDialog d = new TestDialog();
            d.viewId = getId();
            d.set(year,month -1,day);
            d.show(((Activity)getContext()).getFragmentManager(),"");
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onDateSet(int y, int m, int d) {
        year = y;
        day = d;
        month = m + 1;
        setText("" + day + "/" + (month) + "/" + year);
    }

    @SuppressLint("ValidFragment")
    static class TestDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        int year;
        int day;
        int month;
        int viewId =0 ;

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt("viewId",viewId);
        }

        @Override
        public void dismissAllowingStateLoss() {
            super.dismissAllowingStateLoss();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        void set(int y, int m, int d){
            year = y;
            day = d;
            month = m;
        }
        DateEditTextViewDialogListener listener;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                int temp = savedInstanceState.getInt("viewId");
                if (viewId == 0 && temp > 0){
                    viewId = savedInstanceState.getInt("viewId");
                }
            }
            if (listener == null && viewId != 0){
                View v = getActivity().findViewById(viewId);
                if (v instanceof DateEditTextViewDialogListener){
                    listener = (DateEditTextViewDialogListener)v;
                }
            }

            Dialog dialog = new DatePickerDialog(getActivity(),this,year,month,day);
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            if (listener!= null){
                listener.onDateSet(i,i1,i2);
            }
        }
    }
}

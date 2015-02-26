package com.example.tcs.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by TCS on 1/29/15.
 */
public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "com.example.tcs.criminalintent.date";

    private Date mDate;

    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_DATE, mDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);

        //Create a calendar to get the year, month, and day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int am_pm = calendar.get(Calendar.AM_PM);
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
        final TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_date_timePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int hourOfDay = timePicker.getCurrentHour();
                int minutesOfHour = timePicker.getCurrentMinute();
                //Translate year, month, day into a Date object using a calendar
                mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth, hourOfDay, minutesOfHour).getTime();
                //Update argument to preserve selected value on rotation
                getArguments().putSerializable(EXTRA_DATE, mDate);
            }
        });
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minutes);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){
            @Override
            public void onTimeChanged(TimePicker view, int hour, int minutes) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                mDate = new GregorianCalendar(year, month, day, hour, minutes).getTime();
                getArguments().putSerializable(EXTRA_DATE, mDate);
            }
        });

        return new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.date_picker_title).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sendResult(Activity.RESULT_OK);
            }
        }).create();
    }
}

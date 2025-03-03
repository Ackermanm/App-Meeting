package com.example.voteapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    /**
     * This is learned from Android Developer Website.
     * https://developer.android.com/guide/topics/ui/controls/pickers
     */
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        // Use the current time as the default value for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        TimeActivity activity = (TimeActivity) getActivity();
        if (activity != null) {
            activity.showTimePickerResult(hourOfDay, minute);
        }
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        TimeActivity activity = (TimeActivity) getActivity();
        if (activity != null) {
            activity.clickCancelTimePicker();
        }
    }
}

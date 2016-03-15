package shestak.maksym.schedule.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import shestak.maksym.schedule.R;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        int month = monthOfYear + 1;
        String formattedMonth = "" + month;
        String formattedDayOfMonth = "" + dayOfMonth;

        if(month < 10)
            formattedMonth = "0" + month;
        if(dayOfMonth < 10)
            formattedDayOfMonth = "0" + dayOfMonth;

        //Log.d("max", String.valueOf(getArguments().getInt("text")));
        String res = formattedDayOfMonth + "." + formattedMonth + "." + year;
        ((TextView) getActivity().findViewById(getArguments().getInt("text"))).setText(res);
    }
}

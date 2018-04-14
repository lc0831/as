package com.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DateDialog extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {
    private DateListener dateListener;

    public DateDialog newInstance(DateListener dateListener) {

    }

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

    public void onDateSet(DatePicker view, int year, int month, int day) {

        String strDate = "" + year + "-" + month + "-" + day;
        dateListener.getDate(strDate);
    }

    public interface DateListener {
        void getDate(String strDate);
    }

    /**
     * 提供公共的方法,并且初始化接口类型的数据
     */
    public void setDateListener(DateListener dateListener) {
        this.dateListener = dateListener;
    }

}

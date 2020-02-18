package com.fitareq.travel_mate;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class setDate implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener
{
    private EditText editText;
    private Calendar calendar;
    private String MyFormat;
    private Context ctx;

    public setDate(EditText editText, Context context, String myFormat)
    {
        this.editText = editText;
        MyFormat = myFormat;
        ctx = context;
        calendar = Calendar.getInstance();

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (hasFocus)
        {
            new DatePickerDialog(ctx,
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                    ).show();
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(MyFormat, Locale.getDefault());
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);

        editText.setText(sdf.format(calendar.getTime()));
    }
}

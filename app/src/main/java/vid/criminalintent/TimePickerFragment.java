package vid.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by SSubra27 on 10/13/15.
 */
public class TimePickerFragment extends DialogFragment {
    private static final String ARG_TIME = "time";
    public static final String EXTRA_TIME="vid.bignerdranch.criminalintent.time";
    private TimePicker mTimePicker;

    protected Calendar mCalendar;
    private Date mTime;

    public static TimePickerFragment newInstance(Date date)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreateDialog(savedInstanceState);

        mTime = (Date) getArguments().getSerializable(ARG_TIME);
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(mTime);


        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int min = mCalendar.get(Calendar.MINUTE);

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v = layoutInflater.inflate(R.layout.crime_time_picker, null);



        mTimePicker = (TimePicker) v.findViewById(R.id.crime_time_time_picker);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(min);


        return new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.time_picker_title).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int y = mCalendar.get(Calendar.YEAR);
                int m = mCalendar.get(Calendar.MONTH);
                int d = mCalendar.get(Calendar.DAY_OF_MONTH);

                int ho = mTimePicker.getCurrentHour();
                int min= mTimePicker.getCurrentMinute();

                mTime = new GregorianCalendar(y,m,d,ho,min).getTime();
                sendResult(Activity.RESULT_OK, mTime);

            }
        }).create();
    }


    private void sendResult (int resultCode, Date date)
    {
        if(getTargetFragment()==null)
        {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}

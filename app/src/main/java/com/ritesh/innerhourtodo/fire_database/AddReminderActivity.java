package com.ritesh.innerhourtodo.fire_database;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.ritesh.innerhourtodo.R;
import com.ritesh.innerhourtodo.todo_main.AddToDo.AddToDoActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText edt_todo_title,
            edt_todo_description,
            edt_todo_date,
            edt_todo_time;

    SwitchCompat sc_todo_date;

    TextView tv_todo_finaldateTime,
            tv_todo_remindMe;

    Button btn_set_reminder;

    LinearLayout ll_container_dateTime;

    private boolean mUserHasReminder;
    private Date mUserReminderDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addreminder);
        initLayout();
        inputReminderData();

    }


    private void initLayout() {
        edt_todo_title = (EditText) findViewById(R.id.edt_todo_title);
        edt_todo_description = (EditText) findViewById(R.id.edt_todo_description);
        edt_todo_date = (EditText) findViewById(R.id.edt_todo_date);
        edt_todo_time = (EditText) findViewById(R.id.edt_todo_time);
        sc_todo_date = (SwitchCompat) findViewById(R.id.sc_todo_date);
        tv_todo_finaldateTime = (TextView) findViewById(R.id.tv_todo_finaldateTime);
        tv_todo_remindMe = (TextView) findViewById(R.id.tv_todo_remindMe);
        btn_set_reminder = (Button) findViewById(R.id.btn_set_reminder);
        ll_container_dateTime = (LinearLayout) findViewById(R.id.ll_container_dateTime);
        sc_todo_date.setChecked(false);
        tv_todo_remindMe.setText("Remind me Off");
        tv_todo_finaldateTime.setVisibility(View.INVISIBLE);
        edt_todo_time.setText("");
        edt_todo_date.setText("");
    }


    private void inputReminderData() {
        setEnterDateVisible(sc_todo_date.isChecked());
        sc_todo_date.setChecked(mUserHasReminder && (mUserReminderDate != null));

        sc_todo_date.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                mUserReminderDate = null;
            }
            mUserHasReminder = isChecked;
            setDateAndTimeEditText();
            setEnterDate_VisibleWithAnimations(isChecked);
            hideKeyboard(edt_todo_title);
            hideKeyboard(edt_todo_description);
        });


        edt_todo_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date;
                hideKeyboard(edt_todo_title);
                date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(AddReminderActivity.this, year, month, day);
                datePickerDialog.setThemeDark(true);
                datePickerDialog.show(getFragmentManager(), "DateFragment");

            }
        });


        edt_todo_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date;
                hideKeyboard(edt_todo_title);

                date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(AddReminderActivity.this, hour, minute, DateFormat.is24HourFormat(AddReminderActivity.this));
                timePickerDialog.setThemeDark(true);
                timePickerDialog.show(getFragmentManager(), "TimeFragment");
            }
        });


        btn_set_reminder.setOnClickListener((View view) -> {
            if (edt_todo_title.length() <= 0) {
                edt_todo_title.setError(getString(R.string.todo_error_title));
                return;
            }

            if (edt_todo_description.length() <= 0) {
                edt_todo_description.setError(getString(R.string.todo_error_desc));
                return;
            }


            if (edt_todo_date.length() <= 0) {
                Toast.makeText(this, "Remind me Off!! please change to On", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                makeResult(RESULT_OK);
            }
        });

    }

    private void setEnterDateVisible(boolean checked) {
        if (checked) {
            ll_container_dateTime.setVisibility(View.VISIBLE);
        } else {
            ll_container_dateTime.setVisibility(View.INVISIBLE);
        }
    }

    private void setEnterDate_VisibleWithAnimations(boolean checked) {
        if (checked) {
            tv_todo_remindMe.setText("Remind me On");
            setReminderTextView();
            ll_container_dateTime.animate().alpha(1.0f).setDuration(500).setListener(
                    new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            ll_container_dateTime.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    }
            );
        } else {
            tv_todo_remindMe.setText("Remind me Off");
            edt_todo_time.setText("");
            edt_todo_date.setText("");
            ll_container_dateTime.animate().alpha(0.0f).setDuration(500).setListener(
                    new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            ll_container_dateTime.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }
            );
        }

    }

    private void setReminderTextView() {
        if (mUserReminderDate != null) {
            tv_todo_finaldateTime.setVisibility(View.VISIBLE);
            if (mUserReminderDate.before(new Date())) {
                Log.e("ToDoReminder", "DATE is " + mUserReminderDate);
                tv_todo_finaldateTime.setText(getString(R.string.date_error_check_again));
                tv_todo_finaldateTime.setTextColor(Color.RED);
                return;
            }
            Date date = mUserReminderDate;
            String dateString = formatDate("d MMM, yyyy", date);
            String timeString;
            String amPmString = "";

            if (DateFormat.is24HourFormat(this)) {
                timeString = formatDate("k:mm", date);
            } else {
                timeString = formatDate("h:mm", date);
                amPmString = formatDate("a", date);
            }
            String finalString = String.format(getResources().getString(R.string.remind_date_and_time), dateString, timeString, amPmString);
            tv_todo_finaldateTime.setTextColor(getResources().getColor(R.color.secondary_text));
            tv_todo_finaldateTime.setText(finalString);
        } else {
            tv_todo_finaldateTime.setVisibility(View.INVISIBLE);

        }
    }

    private static String formatDate(String formatString, Date dateToFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        return simpleDateFormat.format(dateToFormat);
    }

    private void setDateAndTimeEditText() {

        if (mUserReminderDate != null) {
            String userDate = formatDate("d MMM, yyyy", mUserReminderDate);
            String formatToUse;
            if (DateFormat.is24HourFormat(this)) {
                formatToUse = "k:mm";
            } else {
                formatToUse = "h:mm a";

            }
            String userTime = formatDate(formatToUse, mUserReminderDate);
            edt_todo_time.setText(userTime);
            edt_todo_date.setText(userDate);

        } else {
            edt_todo_date.setText(getString(R.string.date_reminder_default));
//            mUserReminderDate = new Date();
            boolean time24 = DateFormat.is24HourFormat(this);
            Calendar cal = Calendar.getInstance();
            if (time24) {
                cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
            } else {
                cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + 1);
            }
            cal.set(Calendar.MINUTE, 0);
            mUserReminderDate = cal.getTime();
            Log.d("OskarSchindler", "Imagined Date: " + mUserReminderDate);
            String timeString;
            if (time24) {
                timeString = formatDate("k:mm", mUserReminderDate);
            } else {
                timeString = formatDate("h:mm a", mUserReminderDate);
            }
            edt_todo_time.setText(timeString);
//            int hour = calendar.get(Calendar.HOUR_OF_DAY);
//            if(hour<9){
//                timeOption = time24?mDefaultTimeOptions24H[0]:mDefaultTimeOptions12H[0];
//            }
//            else if(hour < 12){
//                timeOption = time24?mDefaultTimeOptions24H[1]:mDefaultTimeOptions12H[1];
//            }
//            else if(hour < 15){
//                timeOption = time24?mDefaultTimeOptions24H[2]:mDefaultTimeOptions12H[2];
//            }
//            else if(hour < 18){
//                timeOption = time24?mDefaultTimeOptions24H[3]:mDefaultTimeOptions12H[3];
//            }
//            else if(hour < 21){
//                timeOption = time24?mDefaultTimeOptions24H[4]:mDefaultTimeOptions12H[4];
//            }
//            else{
//                timeOption = time24?mDefaultTimeOptions24H[5]:mDefaultTimeOptions12H[5];
//            }
//            mTimeEditText.setText(timeOption);
        }
    }

    private void hideKeyboard(EditText et) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        setDate(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        setTime(hourOfDay, minute);
    }


    private void setDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int hour, minute;
//        int currentYear = calendar.get(Calendar.YEAR);
//        int currentMonth = calendar.get(Calendar.MONTH);
//        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(year, month, day);

        if (reminderCalendar.before(calendar)) {
            //    Toast.makeText(this, "My time-machine is a bit rusty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mUserReminderDate != null) {
            calendar.setTime(mUserReminderDate);
        }

        if (DateFormat.is24HourFormat(this)) {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        } else {

            hour = calendar.get(Calendar.HOUR);
        }
        minute = calendar.get(Calendar.MINUTE);

        calendar.set(year, month, day, hour, minute);
        mUserReminderDate = calendar.getTime();
        setReminderTextView();
//        setDateAndTimeEditText();
        setDateEditText();
    }

    private void setTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        if (mUserReminderDate != null) {
            calendar.setTime(mUserReminderDate);
        }

//        if(DateFormat.is24HourFormat(this) && hour == 0){
//            //done for 24h time
//                hour = 24;
//        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("OskarSchindler", "Time set: " + hour);
        calendar.set(year, month, day, hour, minute, 0);
        mUserReminderDate = calendar.getTime();

        setReminderTextView();
//        setDateAndTimeEditText();
        setTimeEditText();
    }

    private void setDateEditText() {
        String dateFormat = "d MMM, yyyy";
        edt_todo_date.setText(formatDate(dateFormat, mUserReminderDate));
    }

    private void setTimeEditText() {
        String dateFormat;
        if (DateFormat.is24HourFormat(this)) {
            dateFormat = "k:mm";
        } else {
            dateFormat = "h:mm a";

        }
        edt_todo_time.setText(formatDate(dateFormat, mUserReminderDate));
    }


    private void makeResult(int result) {
        Log.e("AddToDoActivity", "makeResult - ok : in");

        Bundle bReminder = new Bundle();
        if (edt_todo_title.getText().toString().length() > 0) {

            String capitalizedString = Character.toUpperCase(edt_todo_title.getText().toString().charAt(0)) + edt_todo_title.getText().toString().substring(1);
            bReminder.putString("R_IMG", capitalizedString);
            bReminder.putString("R_DESCRIPTION", edt_todo_description.getText().toString());
            bReminder.putString("R_TITLE", edt_todo_title.getText().toString());

            Log.e("TODO", "Img-> " + capitalizedString);
            Log.e("TODO", "Description-> " + edt_todo_description.getText().toString());
            Log.e("TODO", "Title_Again-> " + edt_todo_title.getText().toString());

        }

        if (mUserReminderDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mUserReminderDate);
            calendar.set(Calendar.SECOND, 0);
            mUserReminderDate = calendar.getTime();
            bReminder.putLong("R_DATE", mUserReminderDate.getTime());
        }
        Intent iReminder = new Intent();
        iReminder.putExtras(bReminder);
//        setResult(result, iReminder);
        setResult(Activity.RESULT_OK, iReminder);
        finish();
    }



}

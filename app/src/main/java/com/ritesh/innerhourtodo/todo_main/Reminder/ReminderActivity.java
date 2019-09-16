package com.ritesh.innerhourtodo.todo_main.Reminder;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.ritesh.innerhourtodo.R;
import com.ritesh.innerhourtodo.todo_main.AppDefault.AppDefaultActivity;

public class ReminderActivity extends AppDefaultActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int contentViewLayoutRes() {
        return R.layout.reminder_layout;
    }

    @NonNull
    @Override
    protected ReminderFragment createInitialFragment() {
        return ReminderFragment.newInstance();
    }


}

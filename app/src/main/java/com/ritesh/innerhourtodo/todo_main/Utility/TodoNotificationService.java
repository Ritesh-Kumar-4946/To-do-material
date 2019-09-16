package com.ritesh.innerhourtodo.todo_main.Utility;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ritesh.innerhourtodo.R;
import com.ritesh.innerhourtodo.todo_main.Reminder.AlarmReceiver;
import com.ritesh.innerhourtodo.todo_main.Reminder.ReminderActivity;

import java.util.Calendar;
import java.util.UUID;

public class  TodoNotificationService extends IntentService {
    public static final String TODOTEXT = "com.avjindersekhon.todonotificationservicetext";
    public static final String TODOUUID = "com.avjindersekhon.todonotificationserviceuuid";
    private String mTodoText;
    private UUID mTodoUUID;
    private Context mContext;

    public TodoNotificationService() {
        super("TodoNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mTodoText = intent.getStringExtra(TODOTEXT);
        mTodoUUID = (UUID) intent.getSerializableExtra(TODOUUID);

        Log.e("OskarSchindler", "onHandleIntent called");


        createAlarm();

//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Intent i = new Intent(this, ReminderActivity.class);
//        i.putExtra(TodoNotificationService.TODOUUID, mTodoUUID);
//        Intent deleteIntent = new Intent(this, DeleteNotificationService.class);
//        deleteIntent.putExtra(TODOUUID, mTodoUUID);
//        Notification notification = new Notification.Builder(this)
//                .setContentTitle(mTodoText)
//                .setSmallIcon(R.drawable.ic_done_white_24dp)
//                .setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_SOUND)
//                .setDeleteIntent(PendingIntent.getService(this, mTodoUUID.hashCode(), deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT))
//                .setContentIntent(PendingIntent.getActivity(this, mTodoUUID.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT))
//                .build();
//
//        manager.notify(100, notification);


    }

    private void createAlarm() {
        Log.e("createAlarm", "createAlarm_called");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }
}

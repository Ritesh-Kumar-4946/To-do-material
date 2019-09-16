package com.ritesh.innerhourtodo.fire_database;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ritesh.innerhourtodo.LoginActivity;
import com.ritesh.innerhourtodo.R;
import com.ritesh.innerhourtodo.alarmServices.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListItemsActivity extends AppCompatActivity {

    private final String TAG = "ListItemsActivity";

    DatabaseReference mDB;
    DatabaseReference mListItemRef;
    private FirebaseDatabase mFirebaseInstance;

    private RecyclerView rv_Reminder;
    private ListItemsAdapter rv_ReminderAdapter;
    private ArrayList<ReminderItems_Model> reminderList;
    Date mUserReminderDate;
    private String reminderId;
    private PendingIntent pendingIntent;
    //Alarm Request Code
    private static final int ALARM_REQUEST_CODE = 133;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.e("ListItemsActivity", "OK");

//        mDB = FirebaseDatabase.getInstance().getReference();
        mFirebaseInstance = FirebaseDatabase.getInstance();
//        mListItemRef = mDB.child("listItem");
//        myListItems = new ArrayList<>();


//        mListItemRef = mDB.child("remiderList");

        mListItemRef = mFirebaseInstance.getReference("remiderList");
        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");
        reminderList = new ArrayList<>();


        rv_Reminder = (RecyclerView) findViewById(R.id.listItem_recycler_view);
        rv_Reminder.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        rv_Reminder.setHasFixedSize(true);
        rv_Reminder.setItemAnimator(new DefaultItemAnimator());
        rv_Reminder.setLayoutManager(new LinearLayoutManager(this));
        updateUI();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab_addItem = (FloatingActionButton) findViewById(R.id.fab_addItem);
//        fab_addItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createNewListItem();
//            }
//        });

        fab_addItem.setOnClickListener((View view) -> {
//            createNewListItem();
            Intent intent = new Intent(ListItemsActivity.this, AddReminderActivity.class);
//            startActivity(intent);
            startActivityForResult(intent, 100);
        });

        mListItemRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String string) {
//                Log.d(TAG + "Added", dataSnapshot.getValue(ListItem.class).toString());
//                Log.e(TAG, "Added-> " + dataSnapshot.getKey().equalsIgnoreCase("reminder_Title"));
                Log.e(TAG, "onChildAdded-> ");
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Log.d(TAG + "Changed", dataSnapshot.getValue(ListItem.class).toString());
//                Log.e(TAG + "Changed", dataSnapshot.getValue(ReminderItems_Model.class).toString());
                Log.e(TAG, "Changed");
//                rv_ReminderAdapter.notifyDataSetChanged();
                recreate();


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Log.d(TAG + "Removed", dataSnapshot.getValue(ListItem.class).toString());
//                Log.e(TAG + "Removed", dataSnapshot.getValue(ReminderItems_Model.class).toString());
                Log.e(TAG, "Removed");
//                taskDeletion(dataSnapshot);
                recreate();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                Log.e(TAG + "Moved", dataSnapshot.getValue(ListItem.class).toString());
//                Log.e(TAG + "Moved", dataSnapshot.getValue(ReminderItems_Model.class).toString());
                Log.e(TAG, "Moved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Log.e(TAG + "Cancelled", databaseError.toString());
                Log.e(TAG, "Cancelled");
            }
        });


    }

    private void taskDeletion(DataSnapshot dataSnapshot) {
        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            String taskTitle = singleSnapshot.getValue(String.class);
            for (int i = 0; i < reminderList.size(); i++) {
                if (reminderList.get(i).getReminder_Title().equals(taskTitle)) {
                    reminderList.remove(i);
                }
            }
//            Log.d(TAG, "Task tile " + taskTitle);
            rv_ReminderAdapter.notifyDataSetChanged();
            rv_ReminderAdapter = new ListItemsAdapter(reminderList);
            rv_Reminder.setAdapter(rv_ReminderAdapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_all:
                deleteAllListItems();
                break;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void deleteAllListItems() {
        FirebaseDatabase.getInstance().getReference().child("remiderList").removeValue();
//        myListItems.clear();
        reminderList.clear();
        rv_ReminderAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Items Deleted Successfully", Toast.LENGTH_SHORT).show();

    }


    private void fetchData(DataSnapshot dataSnapshot) {
        ReminderItems_Model listItem = dataSnapshot.getValue(ReminderItems_Model.class);
        reminderList.add(listItem);
        updateUI();
    }


    private void updateUI() {
        rv_ReminderAdapter = new ListItemsAdapter(reminderList);
        rv_Reminder.setAdapter(rv_ReminderAdapter);

//        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm");
//        simpleDateFormat1.format(new Date());

//        if (reminderList.size() > 0) {
//            for (int i = 0; i < reminderList.size(); i++) {
//                Log.e("simpleDateFormat1", "-> " + stringToDate(reminderList.get(i).getReminder_Date()));
//
////                NotificationHelper.scheduleRepeatingRTCNotification(this, hours.getText().toString(), minutes.getText().toString());
////                NotificationHelper.enableBootReceiver(this);
//            }
//
//        }

//        if (reminderList.size() > 0) {
//            for (ReminderItems_Model reminderItems_model: reminderList){
//                Log.e("simpleDateFormat1", "Date-> " + stringToDate(reminderItems_model.getReminder_Date()));
//            }
//        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            Bundle bGetData = null;
            if (data != null) {
                bGetData = data.getExtras();
                if (bGetData != null) {
//                    Log.e("onActivityResult", "Data-> R_IMG-> " + bGetData.getString("R_IMG", "")
//                            + ",       R_DESCRIPTION-> " + bGetData.getString("R_DESCRIPTION", "")
//                            + ",      R_TITLE-> " + bGetData.getString("R_TITLE", "")
//                            + ",        Date-> " + String.valueOf(mUserReminderDate = new Date(bGetData.getLong("R_DATE", 0))));

                    storeTo_FireBase(bGetData);
                }
            } else {
                Log.e("Result", "Null");
            }

        }
    }


    private void storeTo_FireBase(Bundle rawBundle) {
        reminderId = FirebaseDatabase.getInstance().getReference().child("remiderList").push().getKey();
//        reminderId = mListItemRef.push().getKey();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        simpleDateFormat.format(new Date());

//        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm");
//        simpleDateFormat1.format(new Date());

//        Log.e("simpleDateFormat1", "-> " + simpleDateFormat1.format(mUserReminderDate = new Date(rawBundle.getLong("R_DATE", 0))));


//        Log.e("storeTo_FireBase_Pre", "Data-> R_IMG-> " + rawBundle.getString("R_IMG", "")
//                + ",       R_DESCRIPTION-> " + rawBundle.getString("R_DESCRIPTION", "")
//                + ",      R_TITLE-> " + rawBundle.getString("R_TITLE", "")
//                + ",        Date-> " + String.valueOf(mUserReminderDate = new Date(rawBundle.getLong("R_DATE", 0)))
//                + ",        DateFormated-> " + simpleDateFormat.format(mUserReminderDate = new Date(rawBundle.getLong("R_DATE", 0))));


        String strSet_title = rawBundle.getString("R_TITLE", "");
        String strSet_description = rawBundle.getString("R_DESCRIPTION", "");
//        String strSet_time = String.valueOf(mUserReminderDate = new Date(rawBundle.getLong("R_DATE", 0)));
        String strSet_time = simpleDateFormat.format(mUserReminderDate = new Date(rawBundle.getLong("R_DATE", 0)));

//        Log.e("storeTo_FireBase_Post", "Data-> R_IMG-> " + rawBundle.getString("R_IMG", "")
//                + ",      R_TITLE-> " + strSet_title
//                + ",       R_DESCRIPTION-> " + strSet_description
//                + ",        Date-> " + String.valueOf(mUserReminderDate = new Date(rawBundle.getLong("R_DATE", 0)))
//                + ",        DateFormated-> " + strSet_time);

//        String reminder_ItemId = mListItemRef.push().getKey();
        int color = ColorGenerator.MATERIAL.getRandomColor();
        ReminderItems_Model reminderItems_model = new ReminderItems_Model();
        reminderItems_model.setReminder_Title(strSet_title);
        reminderItems_model.setReminder_Description(strSet_description);
        reminderItems_model.setReminder_Date(strSet_time);
        reminderItems_model.setReminder_Coments("");
        reminderItems_model.setReminder_ID(reminderId);
        reminderItems_model.setIntColor(color);
//        Map<String, Object> mapFinalValue = reminderItems_model.toMap();
//        Map<String, Object> mapChildUpdate = new HashMap<>();
//        mapChildUpdate.put("/remiderList/" + reminderId, mapFinalValue);
//        FirebaseDatabase.getInstance().getReference().updateChildren(mapChildUpdate);

        mListItemRef.child(reminderId).setValue(reminderItems_model);
//        printDifference(new Date(), mUserReminderDate, strSet_title, strSet_description);
        triggerAlarmManager(differTime(mUserReminderDate), strSet_title, strSet_description);
//        differTime(mUserReminderDate);

    }


    private class ListItemsHolder extends RecyclerView.ViewHolder {

        public TextView toDoListItemTextview,
                todoListItemTimeTextView,
                todoListItemCommentTextView;
        ImageView toDoListItemColorImageView;

        LinearLayout listItemLinearLayout;

        public ListItemsHolder(View itemView) {
            super(itemView);

            listItemLinearLayout = (LinearLayout) itemView.findViewById(R.id.listItemLinearLayout);
            toDoListItemColorImageView = (ImageView) itemView.findViewById(R.id.toDoListItemColorImageView);
            toDoListItemTextview = (TextView) itemView.findViewById(R.id.toDoListItemTextview);
            todoListItemTimeTextView = (TextView) itemView.findViewById(R.id.todoListItemTimeTextView);
            todoListItemCommentTextView = (TextView) itemView.findViewById(R.id.todoListItemCommentTextView);

        }

        //        public void bindData(ListItem s) {
        public void bindData(int pos, ReminderItems_Model itemsModel) {
            toDoListItemTextview.setText(itemsModel.getReminder_Title());
            todoListItemTimeTextView.setText(itemsModel.getReminder_Date());

            if (itemsModel.getReminder_Coments() == null ||
                    itemsModel.getReminder_Coments().equalsIgnoreCase("")) {
                todoListItemCommentTextView.setVisibility(View.GONE);
            } else {
                todoListItemCommentTextView.setVisibility(View.VISIBLE);
                todoListItemCommentTextView.setText(itemsModel.getReminder_Coments());
            }

            TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(itemsModel.getReminder_Title().substring(0, 1), itemsModel.getIntColor());
            toDoListItemColorImageView.setImageDrawable(myDrawable);

            listItemLinearLayout.setOnClickListener((View view) -> {
                createNewListItem(itemsModel);
//                rv_ReminderAdapter.notifyItemChanged(pos);
            });


        }
    }

    private class ListItemsAdapter extends RecyclerView.Adapter<ListItemsHolder> {
        //        private ArrayList<ListItem> mListItems;
        private ArrayList<ReminderItems_Model> mListItems;

        //        public ListItemsAdapter(ArrayList<ListItem> ListItems) {
        public ListItemsAdapter(ArrayList<ReminderItems_Model> list) {
            mListItems = list;

        }


        @Override
        public ListItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ListItemsActivity.this);
//            View view = layoutInflater.inflate(R.layout.category_list_item_1, parent, false);
            View view = layoutInflater.inflate(R.layout.list_circle_try, parent, false);
            return new ListItemsHolder(view);

        }

        @Override
        public void onBindViewHolder(ListItemsHolder holder, int position) {

//            ListItem s = mListItems.get(position);
            ReminderItems_Model reminderItems_model = mListItems.get(position);
            holder.bindData(position, reminderItems_model);

        }

        @Override
        public int getItemCount() {
            return mListItems.size();

        }
    }


    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
//    public void printDifference(Date startDate, Date endDate, String strTitle, String strDesc) {
//        //milliseconds
//        long different = endDate.getTime() - startDate.getTime();
//
////        Log.e("startDate", "-> " + startDate);
////        Log.e("endDate", "->" + endDate);
////        Log.e("different", "->" + different);
//
//        long secondsInMilli = 1000;
//        long minutesInMilli = secondsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;
//
//        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;
//
//        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;
//
//        long elapsedMinutes = different / minutesInMilli;
//        different = different % minutesInMilli;
//
//        long elapsedSeconds = different / secondsInMilli;
//        Log.e("FinalDiff", "-> "
//                + elapsedDays + "days  " + elapsedHours + "hours  " + elapsedMinutes + "minutes  " + elapsedSeconds + "seconds");
//
//        NotificationHelper.scheduleRepeatingRTCNotification(this, String.valueOf(elapsedHours), String.valueOf(elapsedMinutes), strTitle, strDesc);
//        NotificationHelper.enableBootReceiver(this);
//
//
//    }


    public void createNewListItem(ReminderItems_Model itemsModel) {
        // Create new List Item  at /listItem
        LayoutInflater li = LayoutInflater.from(this);
        View getListItemView = li.inflate(R.layout.dialog_get_list_item, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(getListItemView);

        EditText userInput = (EditText) getListItemView.findViewById(R.id.editTextDialogUserInput);

//        reminderId = mListItemRef.push().getKey();
//        reminderId = mListItemRef.getDatabase().getReference().getKey();


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result
                        // edit text
                        if (userInput.getText().toString().length() > 0) {
                            ReminderItems_Model reminderItems_model = new ReminderItems_Model();
                            DatabaseReference UpdateReference = FirebaseDatabase.getInstance()
                                    .getReference("remiderList")
                                    .child(itemsModel.getReminder_ID());
                            reminderItems_model.setReminder_Title(itemsModel.getReminder_Title());
                            reminderItems_model.setReminder_Description(itemsModel.getReminder_Description());
                            reminderItems_model.setReminder_Date(itemsModel.getReminder_Date());
                            reminderItems_model.setIntColor(itemsModel.getIntColor());
                            reminderItems_model.setReminder_ID(itemsModel.getReminder_ID());
                            String strComments = userInput.getText().toString();
                            if (!TextUtils.isEmpty(strComments)) {
                                reminderItems_model.setReminder_Coments(strComments);
                            } else {
                                strComments = "Complete";
                                reminderItems_model.setReminder_Coments(strComments);
                            }
                            //update  Vasu_User  to firebase
                            UpdateReference.setValue(reminderItems_model);
                        } else {
                            alertDialogBuilder.setCancelable(true);
                        }
                    }
                }).create()
                .show();

    }

    //Trigger alarm manager with entered time interval
    public void triggerAlarmManager(int alarmTriggerTime, String strTitle, String strDesc) {
        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(ListItemsActivity.this, AlarmReceiver.class);
        alarmIntent.putExtra("TITLE", strTitle);
        alarmIntent.putExtra("DESC", strDesc);
        pendingIntent = PendingIntent.getBroadcast(ListItemsActivity.this, ALARM_REQUEST_CODE, alarmIntent, 0);
        // get a Calendar object with current time
        Calendar cal = Calendar.getInstance();
        // add alarmTriggerTime seconds to the calendar object
        cal.add(Calendar.SECOND, alarmTriggerTime);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds
//        Toast.makeText(this, "Alarm Set for " + alarmTriggerTime + " seconds.", Toast.LENGTH_SHORT).show();
    }

    private int differTime(Date futureDate) {
//        https://stackoverflow.com/questions/10690370/how-do-i-get-difference-between-two-dates-in-android-tried-every-thing-and-pos/24279153
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.format(new Date());
        String strfuture_time = simpleDateFormat.format(futureDate);
        Log.e("strDate_time", "-> " + strfuture_time);

        Date currentDate = new Date();
        long diffn = futureDate.getTime() - currentDate.getTime();
        long seconds = diffn / 1000;
        Log.e("Difference: ", " seconds: " + seconds);
//                    + " hours: " + hours + " days: " + days);
//        long minutes = seconds / 60;
//        long hours = minutes / 60;
//        long days = hours / 24;

//        if (currentDate.before(futureDate)) {
//            Log.e("oldDate", "is previous date");
//            Log.e("Difference: ", " seconds: " + seconds + " minutes: " + minutes
//                    + " hours: " + hours + " days: " + days);
//        }
        return (int) seconds;
    }


}

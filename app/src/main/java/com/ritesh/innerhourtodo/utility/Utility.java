package com.ritesh.innerhourtodo.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.ritesh.innerhourtodo.BuildConfig;
import com.ritesh.innerhourtodo.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utility {

    /* this function show log if isLOG is true (Start)*/
    public static void showLog(String strLog_Tag, String strLog_Msg) {
        if (BuildConfig.DEBUG) {
            Log.e(strLog_Tag, strLog_Msg);
        }
    }
    /* this function show log if isLOG is true (End)*/

    /* this function is showing the toast (Start)*/
    public static final boolean isToast = true;
    public static Toast toast;

    public static void showToast_Short(Context mContext, String strToast) {
        if (toast != null) {
            toast.cancel();     // cancel previous toast first if visible
        }
        toast = Toast.makeText(mContext, strToast, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showToast_Long(Context mContext, String strToast) {
        if (toast != null) {
            toast.cancel();     // cancel previous toast first if visible
        }
        toast = Toast.makeText(mContext, strToast, Toast.LENGTH_LONG);
        toast.show();

        /* this method close the long toast after .5 sec.*/
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                toast.cancel();
//            }
//        }, 500);
        /* this method close the long toast after .5 sec.*/
    }
    /* this function is showing the toast (End)*/


    /* this function is used for check is internet connectivity is available or not (Start)*/
    public static boolean onlyNetCheck(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    /* this function is used for check is internet connectivity is available or not (End)*/

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static String getCurrent_Time() {
        Date d = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String dateStr = simpleDateFormat.format(d);
        return dateStr;
    }

    public static String getCurrentDate_Time() {
        Date d = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String dateStr = simpleDateFormat.format(d);
        return dateStr;
    }

    private static int getHoursDiff(Calendar c1, Calendar c2) {
        Date d1 = c1.getTime();
        Date d2 = c2.getTime();
        long mils = d1.getTime() - d2.getTime();
        int hourDiff = (int) (mils / (1000 * 60 * 60));
        return hourDiff;
    }

    private static int getMinuteDiff(Calendar c1, Calendar c2) {
        Date d1 = c1.getTime();
        Date d2 = c2.getTime();
        long mils = d1.getTime() - d2.getTime();
        int minuteFor = (int) (mils / (1000 * 60) % 60);
        return minuteFor;
    }



    public static Date stringToDate(String aDate) {
        if (aDate == null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date stringDate = simpleDateFormat.parse(aDate, pos);
        return stringDate;

    }

}

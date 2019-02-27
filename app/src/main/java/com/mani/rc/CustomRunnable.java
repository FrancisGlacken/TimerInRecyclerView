package com.mani.rc;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.mani.rc.Database.Category;

public class CustomRunnable implements Runnable {

    public long initialTime;
    public long displayMillis;
    public TextView holderTV;
    public String displayResultToLog;
    MainViewModel mainVM;

    private Handler handler;
    private FormatMillis form = new FormatMillis();
    private final static String TAG = "CustomRunnable";

    public CustomRunnable(Handler handler, TextView holderTV, long initialTime, MainViewModel mainVM) {
        this.handler = handler;
        this.holderTV = holderTV;
        this.initialTime = initialTime;
        this.mainVM = mainVM;
    }

    @Override
    public void run() {
        displayMillis = SystemClock.elapsedRealtime() - initialTime;
        holderTV.setText(form.FormatMillisIntoHMS(displayMillis));
        Log.e(TAG, "CustomRunnable--" + displayResultToLog + " DisplayTime: " + form.FormatMillisIntoHMS(SystemClock.elapsedRealtime() - initialTime));
        handler.postDelayed(this, 1000);
    }

    public long getDisplayMillis() {
        return displayMillis;
    }


}
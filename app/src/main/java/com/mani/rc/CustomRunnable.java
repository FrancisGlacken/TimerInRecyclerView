package com.mani.rc;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.mani.rc.Database.Category;

// This is unused in the app, I basically moved the whole thing back into the adapter because it was easier to work with
public class CustomRunnable implements Runnable {

    public long initialTime;
    public long displayMillis;
    public TextView holderTV;
    public String displayResultToLog;

    private Handler handler;
    private FormatMillis form = new FormatMillis();
    private final static String TAG = "CustomRunnable";

    public CustomRunnable(Handler handler, TextView holderTV, long initialTime) {
        this.handler = handler;
        this.holderTV = holderTV;
        this.initialTime = initialTime;
    }

    @Override
    public void run() {
        //android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        displayMillis = SystemClock.elapsedRealtime() - initialTime;
        holderTV.setText(form.FormatMillisIntoHMS(displayMillis));
        Log.e(TAG, "CustomRunnable--" + displayResultToLog + " DisplayTime: " + form.FormatMillisIntoHMS(SystemClock.elapsedRealtime() - initialTime));
        handler.postDelayed(this, 1000);

    }

    public long getDisplayMillis() {
        return displayMillis;
    }
}
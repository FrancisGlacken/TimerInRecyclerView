package com.mani.rc;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomRunnable implements Runnable {

    public long initialTime;
    public TextView holderTV;
    Handler handler;
    FormatMillis form = new FormatMillis();

    public CustomRunnable(Handler handler, TextView holderTV, long initialTime) {
        this.handler = handler;
        this.holderTV = holderTV;
        this.initialTime = initialTime;
    }

    @Override
    public void run() {
        holderTV.setText(form.FormatMillisIntoHMS(SystemClock.elapsedRealtime() - initialTime));
        handler.postDelayed(this, 1000);
    }

}
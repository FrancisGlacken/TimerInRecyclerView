package com.mani.rc;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private Handler handler = new Handler();

    public RecyclerViewAdapter() {
    }

    public void clearAll() {
        handler.removeCallbacksAndMessages(null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recycler_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind();
    }


    @Override
    public int getItemCount() {
        return 9001; // OVER 9000!!!
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeStamp = itemView.findViewById(R.id.category_card_timer);
        CustomRunnable customRunnable;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            customRunnable = new CustomRunnable(handler, timeStamp, SystemClock.elapsedRealtime());
        }

        public void bind() {
            handler.removeCallbacks(customRunnable);
            customRunnable.holder = timeStamp;
            customRunnable.initialTime = SystemClock.elapsedRealtime() - (10000 * getAdapterPosition());
            handler.postDelayed(customRunnable, 100);

        }
    }
}

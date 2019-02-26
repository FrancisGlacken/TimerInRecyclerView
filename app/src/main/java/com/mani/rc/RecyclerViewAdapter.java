package com.mani.rc;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mani.rc.Database.Category;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> {

    private Handler handler = new Handler();
    private List<Category> categories;

    public RecyclerViewAdapter() {
    }

    public void clearAll() {
        handler.removeCallbacksAndMessages(null);
    }

    public void setCategories(final List<Category> categories) {
        this.categories = categories;

        // Notifies the adapter that the underlying data has changed
        // Therefore causing the Adapter to refresh
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recycler_item, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        // Note the -1 to get our position(EX: 1-3) to match the array(EX: 0-2)
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        if (categories != null) {
            return categories.size();
        } else return 0;
    }




    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView timeStamp, categoryTitle;
        Button playButton;
        Button pauseButton;
        Button resetButton;
        Button commitButton;
        CustomRunnable customRunnable;


        public CustomViewHolder(View itemView) {
            super(itemView);
            timeStamp = itemView.findViewById(R.id.category_card_timer);
            customRunnable = new CustomRunnable(handler, timeStamp, SystemClock.elapsedRealtime());
            categoryTitle = itemView.findViewById(R.id.category_card_title);
            playButton = itemView.findViewById(R.id.category_card_play_button);
            pauseButton = itemView.findViewById(R.id.category_card_pause_button);
            resetButton = itemView.findViewById(R.id.category_card_reset_button);
            commitButton = itemView.findViewById(R.id.category_card_commit_button);
        }



        public void bind(int position) {

            // Stop runnable and get currentCategory
            handler.removeCallbacks(customRunnable);
            final Category currentCategory = categories.get(position);

            // If timer was running before, start a new one
            if (currentCategory.isTimerRunning()) {
                customRunnable.holderTV = timeStamp;
                customRunnable.initialTime = SystemClock.elapsedRealtime();
                handler.postDelayed(customRunnable, 100);
                StartEnabledButtons();
            } else {
                if (currentCategory.getDisplayTime() > 0) {
                    PauseEnabledButtons();
                } else {
                    DefaultEnabledButtons();
                }
            }

            categoryTitle.setText(currentCategory.getCategory());

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StartEnabledButtons();
                    currentCategory.setTimerRunning(true);
                    //UpdateCategoryAction(currentCategory);

                }
            });

            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PauseEnabledButtons();
                    currentCategory.setTimerRunning(false);
                }
            });

            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DefaultEnabledButtons();

                }
            });

            commitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DefaultEnabledButtons();
                }
            });
        } // End bind()


        /**
         * SetEnabled Timer Button Methods - Three states of buttons
         */

        public void StartEnabledButtons() {
            // timer Started
            playButton.setEnabled(false);
            pauseButton.setEnabled(true);
            resetButton.setEnabled(false);
            commitButton.setEnabled(false);
        }

        public void PauseEnabledButtons() {
            // timer Paused
            playButton.setEnabled(true);
            pauseButton.setEnabled(false);
            resetButton.setEnabled(true);
            commitButton.setEnabled(true);
        }

        public void DefaultEnabledButtons() {
            // timer Reset
            playButton.setEnabled(true);
            pauseButton.setEnabled(false);
            resetButton.setEnabled(false);
            commitButton.setEnabled(false);
        }
    }
}

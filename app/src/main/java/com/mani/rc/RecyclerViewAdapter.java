package com.mani.rc;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mani.rc.Database.Category;

import java.util.ArrayList;
import java.util.List;

//TODO: Create viewmodel references or a listener
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> {

    private Handler handler = new Handler();
    private List<Category> categories;
    private MainViewModel mainVM;
    private FormatMillis form = new FormatMillis();
    private boolean isNotifyDataSetChangedHappened;
    private static String isNotifyDataSetChangedHappenedKey = "com.mani.rc.isNotifyDataSetChangedHappenedKey";
    private Context context;

    private final static String TAG = "RecyclerViewAdapter";
    //private MainListener listener;

    public RecyclerViewAdapter(MainViewModel mainViewModel, Context mainContext) {
        this.mainVM = mainViewModel;
        categories = new ArrayList<>();
        this.context = mainContext;
    }

    public void clearAll() {
        handler.removeCallbacksAndMessages(null);
    }

    public void setCategories(final List<Category> newCategories, boolean isNotifyDataSetChangedHappened) {
        this.categories = newCategories;
        if (!isNotifyDataSetChangedHappened) {
            SharedPreferences prefs = context.getSharedPreferences("shared_preferences", context.MODE_PRIVATE);
            prefs.edit().putBoolean(isNotifyDataSetChangedHappenedKey, true).apply();
            Log.e(TAG, "notifyDataSetChanged Called");
            notifyDataSetChanged();
        }
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
        //MainViewModel mainVM = ViewModelProviders.of(context).get(MainViewModel.class);
        holder.bind(position, mainVM);
    }


    @Override
    public int getItemCount() {
        if (categories != null) {
            return categories.size();
        } else return 0;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        // TextViews, Buttons and CustomRunnable
        TextView timeStamp, categoryTitle;
        Button playButton;
        Button pauseButton;
        Button resetButton;
        Button commitButton;
        CustomRunnable customRunnable;

        // Constructor
        public CustomViewHolder(View itemView) {
            super(itemView);
            timeStamp = itemView.findViewById(R.id.category_card_timer);
            customRunnable = new CustomRunnable(handler, timeStamp, SystemClock.elapsedRealtime(), mainVM);
            categoryTitle = itemView.findViewById(R.id.category_card_title);
            playButton = itemView.findViewById(R.id.category_card_play_button);
            pauseButton = itemView.findViewById(R.id.category_card_pause_button);
            resetButton = itemView.findViewById(R.id.category_card_reset_button);
            commitButton = itemView.findViewById(R.id.category_card_commit_button);
        }

        // Method to be run in BindViewHolder,
        public void bind(final int position, final MainViewModel viewModel) {

            // Stop runnable and get currentCategory
            if (customRunnable != null) {
                handler.removeCallbacks(customRunnable);
            }

            final Category currentCategory = categories.get(position);

            categoryTitle.setText(currentCategory.getCategory());
            timeStamp.setText(form.FormatMillisIntoHMS(currentCategory.getDisplayTime()));

            // If timer was running before, start a new one
            if (currentCategory.isTimerRunning()) {
                customRunnable.holderTV = timeStamp;
                customRunnable.initialTime = SystemClock.elapsedRealtime() - currentCategory.getDisplayTime();
                customRunnable.displayResultToLog = currentCategory.getCategory();
                handler.postDelayed(customRunnable, 100);
                StartEnabledButtons();
            } else {
                if (currentCategory.getDisplayTime() > 0) {
                    PauseEnabledButtons();
                } else {
                    DefaultEnabledButtons();
                }
            }

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StartEnabledButtons();
                    currentCategory.setTimerRunning(true);
                    categories.set(position, currentCategory);
                    customRunnable.holderTV = timeStamp;
                    customRunnable.initialTime = SystemClock.elapsedRealtime() - currentCategory.getDisplayTime();
                    customRunnable.displayResultToLog = currentCategory.getCategory();
                    handler.postDelayed(customRunnable, 100);
                    viewModel.updateCategory(currentCategory);
                    notifyItemChanged(position, "payload " + position);

                }
            });

            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PauseEnabledButtons();
                    handler.removeCallbacks(customRunnable);
                    currentCategory.setTimerRunning(false);
                    categories.set(position, currentCategory);
                    currentCategory.setDisplayTime(customRunnable.getDisplayMillis());
                    viewModel.updateCategory(currentCategory);
                    notifyItemChanged(position, "payload " + position);
                }
            });

            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DefaultEnabledButtons();
                    currentCategory.setTimerRunning(false);
                    categories.set(position, currentCategory);
                    currentCategory.setDisplayTime(0);
                    viewModel.updateCategory(currentCategory);
                    notifyItemChanged(position, "payload " + position);
                }
            });

            commitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //DefaultEnabledButtons();
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

// Alternative to .notifyDataSetChanged()
//        // Get a list of the old categories and then make a new list if newCategories has value
//        final List<Category> oldCategories = new ArrayList<>(this.categories);
//        this.categories.clear();
//        if (newCategories != null) {
//            this.categories.addAll(newCategories);
//        }
//
//        DiffUtil.calculateDiff(new DiffUtil.Callback() {
//            @Override
//            public int getOldListSize() {
//                return oldCategories.size();
//            }
//
//            @Override
//            public int getNewListSize() {
//                return newCategories.size();
//            }
//
//            @Override
//            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//                return oldCategories.get(oldItemPosition).equals(newCategories.get(newItemPosition));
//            }
//
//            @Override
//            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//                return oldCategories.get(oldItemPosition).equals(newCategories.get(newItemPosition));
//            }
//        }).dispatchUpdatesTo(this);
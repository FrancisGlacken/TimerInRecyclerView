package com.mani.rc;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mani.rc.Database.Category;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainListener {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    MainViewModel mainVM;
    MainListener listener;
    SharedPreferences prefs;
    public boolean isNotifyDataSetChangedHappened;
    public static String isNotifyDataSetChangedHappenedKey = "com.mani.rc.isNotifyDataSetChangedHappenedKey";
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("shared_preferences", MODE_PRIVATE);
        mainVM = ViewModelProviders.of(this).get(MainViewModel.class);
        recyclerViewAdapter = new RecyclerViewAdapter(mainVM, this);
        recyclerView = findViewById(R.id.cardView);
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 1));
        recyclerView.setAdapter(recyclerViewAdapter);

        mainVM.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                isNotifyDataSetChangedHappened = prefs.getBoolean(isNotifyDataSetChangedHappenedKey, false);
                recyclerViewAdapter.setCategories(categories, isNotifyDataSetChangedHappened);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        recyclerViewAdapter.clearAll();
        prefs.edit().putBoolean(isNotifyDataSetChangedHappenedKey, false).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerViewAdapter.clearAll();
        prefs.edit().putBoolean(isNotifyDataSetChangedHappenedKey, false).apply();
    }

    @Override
    public void callback(Category category) {
        mainVM.updateCategory(category);
    }
}

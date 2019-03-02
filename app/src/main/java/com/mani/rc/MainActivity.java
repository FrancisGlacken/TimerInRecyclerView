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
import android.util.Log;

import com.mani.rc.Database.Category;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    MainViewModel mainVM;
    public boolean isNotifyDataSetChangedHappened;
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isNotifyDataSetChangedHappened = false;
        mainVM = ViewModelProviders.of(this).get(MainViewModel.class);
        recyclerViewAdapter = new RecyclerViewAdapter(mainVM, this);
        recyclerView = findViewById(R.id.cardView);
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 1));
        recyclerView.setAdapter(recyclerViewAdapter);

        mainVM.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                // This will call .notifyDataSetChanged to populate the recyclerView ONCE per Main onCreate
                if (!isNotifyDataSetChangedHappened) {
                    recyclerViewAdapter.setCategories(categories);
                    isNotifyDataSetChangedHappened = true;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: Called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerViewAdapter.clearAll();
        Log.e(TAG, "onDestroy: Called");
    }

}

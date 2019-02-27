package com.mani.rc;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mani.rc.Database.Category;

import java.util.List;

//TODO: Implement listener for updating categories
public class MainActivity extends AppCompatActivity implements MainListener {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    MainViewModel mainVM;
    MainListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainVM = ViewModelProviders.of(this).get(MainViewModel.class);
        recyclerViewAdapter = new RecyclerViewAdapter(mainVM);
        recyclerView = findViewById(R.id.cardView);
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 1));
        recyclerView.setAdapter(recyclerViewAdapter);

        mainVM.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                recyclerViewAdapter.setCategories(categories);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        recyclerViewAdapter.clearAll();
    }

    @Override
    public void callback(Category category) {
        mainVM.updateCategory(category);
    }
}

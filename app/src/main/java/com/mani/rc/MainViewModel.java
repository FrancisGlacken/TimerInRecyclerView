package com.mani.rc;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.mani.rc.Database.Category;
import com.mani.rc.Database.CategoryRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private CategoryRepository repository;

    // Category variables
    private LiveData<List<Category>> allCategories;
    private LiveData<List<Category>> favorites;
    private LiveData<Category> category;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new CategoryRepository(application);
        allCategories = repository.getAllCategories();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<Category> getCategoryByTitle(String title) {
        category = repository.getCategoryByTitle(title);
        return category;
    }

    public void insertCategory(Category category) {
        repository.insertCategory(category);
    }

    public void updateCategory(Category category) {
        repository.updateCategory(category);
    }

    public LiveData<List<Category>> getFavorites() {
        favorites = repository.getFavorites();
        return favorites;
    }
}

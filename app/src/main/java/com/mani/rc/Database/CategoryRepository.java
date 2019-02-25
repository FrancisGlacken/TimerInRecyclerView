package com.mani.rc.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class CategoryRepository {
    private CategoryDao categoryDao;

    private LiveData<List<Category>> allCategories;
    private LiveData<List<Category>> favorites;
    private LiveData<Category> categoryByTitle;
    private LiveData<Long> categoryTimeSum;

    /**
     * @param application
     * Constructor for the Repository
     * Initializes:
     * CategoryDao
     * TimeBankDao
     * GoalDao
     * allCategories List
     * allTimeBanks List
     */
    public CategoryRepository(Application application) {
        CategoryDatabase db = CategoryDatabase.getDatabase(application);
        categoryDao = db.categoryDao();
        allCategories = categoryDao.getAllCategories();
    }


    /** Category Methods */

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<Category> getCategoryByTitle(String title) {
        categoryByTitle = categoryDao.getCategoryByTitle(title);
        return categoryByTitle;
    }

    public void insertCategory(Category category) {
        new InsertAsyncTask(categoryDao).execute(category);
    }

    public void updateCategory(Category category) {
        new UpdateCategoryAsyncTask(categoryDao).execute(category);
    }
    public LiveData<List<Category>> getFavorites() {
        favorites = categoryDao.getFavorites();
        return favorites;
    }

    /**
     * Category Async Tasks
     */

    private static class InsertAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao asyncTaskDao;

        InsertAsyncTask(CategoryDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            asyncTaskDao.insertCategory(categories[0]);
            return null;
        }
    }

    private static class UpdateCategoryAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao asyncTaskDao;

        UpdateCategoryAsyncTask(CategoryDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            asyncTaskDao.updateCategory(categories[0]);
            return null;
        }
    }
}

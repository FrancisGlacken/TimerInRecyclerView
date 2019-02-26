package com.mani.rc.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.mani.rc.RecyclerViewAdapter;


@Database(entities = {Category.class }, version = 1, exportSchema = false)

public abstract class CategoryDatabase extends RoomDatabase {

    // Dao abstract methods
    public abstract CategoryDao categoryDao();

    // Singleton
    private static volatile CategoryDatabase INSTANCE;

    static CategoryDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (CategoryDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CategoryDatabase.class, "category_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration() //TODO: understand migrations   https://developer.android.com/training/data-storage/room/migrating-db-versions - https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final CategoryDao categoryDao;
        String[] defaultCategories = {"Work", "School", "Gym", "Yoga", "Legend of Zelda", "Final Fantasy", "Mortal Kombat", "Street Fighter"};

        PopulateDbAsync(CategoryDatabase db) {
            categoryDao = db.categoryDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // format class object
            if(categoryDao.getAnyCategory().length < 1) {
                for(int i = 0; i <= defaultCategories.length - 1; i++) {
                    Category category = new Category(defaultCategories[i], 0, 0, "00:00 AM", false, false);
                    categoryDao.insertCategory(category);
                }
            }
            return null;
        }
    }
}

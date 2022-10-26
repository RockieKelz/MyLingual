package com.example.mylingual.data.room_db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {RoomEntity.class}, version = 2)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static RoomDatabase instance;
    public abstract RoomDAO Dao();

    public static synchronized RoomDatabase getInstance(Context context) {
        //check if the instance is null
        if (instance == null) {
            // if the instance is null, create a new instance
            instance = Room.databaseBuilder(context.getApplicationContext(), RoomDatabase.class, "room_translations")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
        }
        return instance;
    }
    private static final androidx.room.RoomDatabase.Callback roomCallback = new androidx.room.RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDbAsyncTask(RoomDatabase instance) {
            RoomDAO dao = instance.Dao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}

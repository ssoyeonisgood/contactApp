package com.example.contact_management_app;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public class ContactDBInstance {
    public static ContactDatabase database;

    public static ContactDatabase getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, ContactDatabase.class, "app_database")
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return database;
    }
}

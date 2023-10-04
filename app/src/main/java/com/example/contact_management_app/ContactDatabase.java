package com.example.contact_management_app;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Contact.class},version = 2)
public abstract class ContactDatabase extends RoomDatabase {
    public abstract ContactDAO getContactDAO();

}

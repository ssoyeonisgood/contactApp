package com.example.contact_management_app;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDAO {

    @Insert
    void insert(Contact... contact);

    @Update
    void update(Contact... contact);

    @Delete
    void delete(Contact... contact);

    @Query("select * from contactInformations")
    List<Contact> getAllContact();

    @Query("select * from contactInformations where email == :email")
    Contact getContactByEmail(String email);

    @Query("select * from contactInformations where name == :name")
    Contact getContactByName(String name); 
}

package com.example.contact_management_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contactInformations")
public class Contact {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "phoneNumber")
    private String phoneNumber;
    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] profilePhotoByte;

    public Contact(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.profilePhotoByte = null;
    }

    public Contact(String name, String phoneNumber, String email, byte[] photo) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.profilePhotoByte = photo;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfilePhotoByte(byte[] profilePhotoByte) {
        this.profilePhotoByte = profilePhotoByte;
    }
    public byte[] getProfilePhotoByte() {
        return profilePhotoByte;
    }

    public Bitmap getProfilPhotoBitmap() {
        return BitmapFactory.decodeByteArray(this.profilePhotoByte, 0, this.profilePhotoByte.length);
    }
}

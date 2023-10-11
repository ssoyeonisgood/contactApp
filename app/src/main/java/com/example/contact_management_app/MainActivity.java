package com.example.contact_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ContactListFrag contactListFrag = new ContactListFrag();
    DetailFrag detailFrag = new DetailFrag();
    FragmentManager fragmentManager = getSupportFragmentManager();
    DataViewModel dataViewModel;
    private static final int REQUEST_READ_CONTACT_PERMISSION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        dataViewModel.getClicked().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (dataViewModel.getClickedValue().equals("contact")){
                    loadContactListFrag();
                }
                if (dataViewModel.getClickedValue().equals("detail")){
                    loadDetailFrag();
                }
            }
        });

    }

    private void loadContactListFrag() {
        Fragment frag = fragmentManager.findFragmentById(R.id.main);
        if (frag==null) {
            fragmentManager.beginTransaction().add(R.id.main,contactListFrag).commit();
        }
        else {
            fragmentManager.beginTransaction().replace(R.id.main,contactListFrag).commit();
        }
    }

    private void loadDetailFrag() {
        Fragment frag = fragmentManager.findFragmentById(R.id.main);
        if (frag==null) {
            fragmentManager.beginTransaction().add(R.id.main,detailFrag).commit();
        }
        else {
            fragmentManager.beginTransaction().replace(R.id.main,detailFrag).commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
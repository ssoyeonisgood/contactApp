package com.example.contact_management_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    ContactListFrag contactListFrag = new ContactListFrag();
    DetailFrag detailFrag = new DetailFrag();
    FragmentManager fragmentManager = getSupportFragmentManager();
    DataViewModel dataViewModel;

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
}
package com.example.contact_management_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactListFrag extends Fragment implements RecyclerviewAdapter.ItemClickListener {

    RecyclerView recyclerView;
    RecyclerviewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Button addBtn, getContactsBtn;
    DataViewModel dataViewModel;
    List<Contact> contactList;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        ContactDatabase database = Room.databaseBuilder(requireContext(),ContactDatabase.class, "production")
//                .allowMainThreadQueries().build();

        ContactDAO contactDAO = ContactDBInstance.getDatabase(requireContext()).getContactDAO();

        View rootView = inflater.inflate(R.layout.fragment_contact_list,container,false);
        recyclerView = rootView.findViewById(R.id.contactRV);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        contactList =  contactDAO.getAllContact();
        adapter = new RecyclerviewAdapter(requireContext(), contactList, this);
        recyclerView.setAdapter(adapter);

        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        addBtn = rootView.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataViewModel.setClickedValue("detail");
            }
        });

        getContactsBtn = rootView.findViewById(R.id.getContactsBtn);
        getContactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        return rootView;
    }

    @Override
    public void onItemClick(Contact contact) {
        dataViewModel.setUpdateContact(contact);
        dataViewModel.setClickedValue("detail");
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
    }

    private void getContactList() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, sort);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                @SuppressLint("Range") String name  = cursor.getString((cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";
                Cursor phoneCursor = getActivity().getContentResolver().query(uriPhone, null, selection, new String[]{id}, null);
                if (phoneCursor.moveToNext()){
                    @SuppressLint("Range") String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Contact contact = new Contact(name,number,null );
                    contactList.add(contact);
                    ContactDAO contactDAO = ContactDBInstance.getDatabase(requireContext()).getContactDAO();
                    contactDAO.insert(contact);
                    phoneCursor.close();
                }
            }
            cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            getContactList();
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "Permission Denied.", Toast.LENGTH_SHORT).show();
            checkPermission();
        }
    }

}
package com.example.contact_management_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class ContactListFrag extends Fragment implements RecyclerviewAdapter.ItemClickListener {

    RecyclerView recyclerView;
    RecyclerviewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Button addBtn;
    DataViewModel dataViewModel;
    List<Contact> contactList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ContactDatabase database = Room.databaseBuilder(requireContext(),ContactDatabase.class, "production")
                .allowMainThreadQueries().build();
        contactList =  database.getContactDAO().getAllContact();

        View rootView = inflater.inflate(R.layout.fragment_contact_list,container,false);
        recyclerView = rootView.findViewById(R.id.contactRV);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
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

        return rootView;
    }

    @Override
    public void onItemClick(Contact contact) {
        dataViewModel.setClickedValue("detail");
    }
}
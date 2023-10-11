package com.example.contact_management_app;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class DetailFrag extends Fragment {

    public static final String EXTRA_INFO = "default";
    private static final int REQUEST_CODE = 22;
    ImageView detailImage;
    EditText name, phoneN, email;
    Button storeBtn, cancelBtn, cameraBtn;

    DataViewModel dataViewModel;
    Boolean cameFromAnotherApp = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail,container,false);
        name = rootView.findViewById(R.id.nameEditView);
        phoneN = rootView.findViewById(R.id.phoneEditView);
        email = rootView.findViewById(R.id.emailEditView);
        cancelBtn = rootView.findViewById(R.id.cancelBtn);
        storeBtn = rootView.findViewById(R.id.storeBtn);
        cameraBtn = rootView.findViewById(R.id.cameraBtn);
        detailImage = rootView.findViewById(R.id.detailImage);

//        database = Room.databaseBuilder(requireContext(),ContactDatabase.class, "production")
//                .allowMainThreadQueries().build();


        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraInt,REQUEST_CODE);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataViewModel.setUpdateContact(null);
                dataViewModel.setClickedValue("contact");
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // existing contact
        if (dataViewModel.getUpdateContact() != null ) {
            if (!cameFromAnotherApp) {
                Contact updatingContact = dataViewModel.getUpdateContact();
                name.setText(updatingContact.getName());
                name.postInvalidate();
                phoneN.setText(updatingContact.getPhoneNumber());
                phoneN.postInvalidate();
                email.setText(updatingContact.getEmail());
                email.postInvalidate();
                if (updatingContact.getProfilPhotoBitmap() != null) {
                    detailImage.setImageBitmap(updatingContact.getProfilPhotoBitmap());
                }
                else {
                    detailImage.setImageResource(R.drawable.avatar);
                }
            }

            storeBtn.setText("Update");
            storeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String newName = name.getText().toString();
                    String newPhoneN = phoneN.getText().toString();
                    String newEmail = email.getText().toString();
                    Bitmap photo = ((BitmapDrawable) detailImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream boas = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, boas);
                    byte[] photoBytes = boas.toByteArray();
                    Contact updatedContact = dataViewModel.getUpdateContact();
                    updatedContact.setName(newName);
                    updatedContact.setPhoneNumber(newPhoneN);
                    updatedContact.setEmail(newEmail);
                    updatedContact.setProfilePhotoByte(photoBytes);

                    ContactDAO contactDAO = ContactDBInstance.getDatabase(requireContext()).getContactDAO();
                    contactDAO.update(updatedContact);
                    dataViewModel.setUpdateContact(null);
                    cameFromAnotherApp = false;
                    dataViewModel.setClickedValue("contact");
                }
            });
        }

        // new contact
        if (dataViewModel.getUpdateContact() == null) {
            // reset edit view for new contacts
            name.setText("");
            phoneN.setText("");
            email.setText("");
            storeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String newName = name.getText().toString();
                    String newPhoneN = phoneN.getText().toString();
                    String newEmail = email.getText().toString();
                    Bitmap photo = ((BitmapDrawable) detailImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream boas = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, boas);
                    byte[] photoBytes = boas.toByteArray();
                    Contact newContact = new Contact(newName,newPhoneN,newEmail, photoBytes);
                    ContactDAO contactDAO = ContactDBInstance.getDatabase(requireContext()).getContactDAO();
                    contactDAO.insert(newContact);
                    dataViewModel.setClickedValue("contact");
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK ) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            detailImage.setImageBitmap(photo);
            cameFromAnotherApp = true;
        }else {
            Toast.makeText(requireContext(),"Error",Toast.LENGTH_SHORT).show();
        }
    }
}
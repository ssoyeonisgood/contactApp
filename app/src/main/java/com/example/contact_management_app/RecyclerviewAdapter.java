package com.example.contact_management_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {
    Context context;
    List<Contact> contactList;
    private ItemClickListener clickListener;
    private int selectedPos;


    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, phoneN, email;
        Button deleteBtn;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.personImage);
            name = itemView.findViewById(R.id.nameTextView);
            phoneN = itemView.findViewById(R.id.phoneTextView);
            email = itemView.findViewById(R.id.emailTextView);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            cardView = itemView.findViewById(R.id.caedView);
        }
    }
    public RecyclerviewAdapter (Context context, List<Contact> contactList, ItemClickListener clickListener ){
        this.context = context;
        this.contactList = contactList;
        this.clickListener = clickListener;
        this.selectedPos = RecyclerView.NO_POSITION;
    }

    public void setSelectedPos(int position) {
        this.selectedPos = position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (contactList != null) {
            Contact contact = contactList.get(position);
            holder.name.setText(contact.getName());
            holder.phoneN.setText(contact.getPhoneNumber());
            holder.email.setText(contact.getEmail());

            if (contact.getProfilPhotoBitmap() == null) {
                holder.image.setImageResource(R.drawable.avatar);
            }
            else {
                holder.image.setImageBitmap(contact.getProfilPhotoBitmap());
            }

            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContactDAO contactDAO = ContactDBInstance.getDatabase(context).getContactDAO();

//                  ContactDatabase database = Room.databaseBuilder(context,ContactDatabase.class, "production")
//                            .allowMainThreadQueries().build();
                    contactDAO.delete(contact);
                    contactList.remove(position);
                    notifyDataSetChanged();
                }
            });

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.getAdapterPosition() == RecyclerView.NO_POSITION) return;
                    clickListener.onItemClick(contactList.get(holder.getLayoutPosition()));
                    notifyItemChanged(selectedPos);
                    selectedPos = holder.getAdapterPosition();
                    notifyItemChanged(selectedPos);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (contactList != null){return contactList.size();}
        else {return 0;}
    }
    public interface ItemClickListener{
        public void onItemClick(Contact contact);
    }
}

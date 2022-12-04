package io.network.voyageplus.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.network.voyageplus.R;
import io.network.voyageplus.activities.PlaceDetails;
import io.network.voyageplus.model.Photo;

public class Plus_visites_adapter extends RecyclerView.Adapter<Plus_visites_adapter.ListViewHolder> {

        private final Context context;
        private final ArrayList<Photo> inner_list;
        private static final String TAG = "Plus_visites_adapter";
        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private FirebaseDatabase mFirebaseDatabase;
        private DatabaseReference mReference;
        private StorageReference mStorageReference;

    public Plus_visites_adapter(Context context, ArrayList<Photo> inner_list) {
            this.context = context;
            this.inner_list = inner_list;
        }

        @NonNull
        @Override
        public Plus_visites_adapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            @SuppressLint("InflateParams") View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.plus_visites, null);
            return new Plus_visites_adapter.ListViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
           holder.vieweditem_name.setText(inner_list.get(position).getName());
            Picasso.get().load(inner_list.get(position).getImageUrl()).placeholder(R.drawable.gradient).into(holder.vieweditem_img);

            holder.vieweditem_card.setOnClickListener(v -> {
                Photo photo = new Photo();
                photo.setPhotoId( inner_list.get(position).getPhotoId());
                photo.setName(inner_list.get(position).getName());
                photo.setTown(inner_list.get(position).getTown());
                photo.setCategorie(inner_list.get(position).getCategorie());
                photo.setRate(inner_list.get(position).getRate());
                photo.setDescription(inner_list.get(position).getDescription());
                photo.setImageUrl(inner_list.get(position).getImageUrl());

                Intent intent = new Intent(context, PlaceDetails.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("place_details_bundle", photo);

                context.startActivity(intent);
            });


        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return inner_list.size();
        }

        public static class ListViewHolder extends RecyclerView.ViewHolder {

            View itemView;
            TextView vieweditem_name;
            ImageView vieweditem_img ;
            CardView vieweditem_card;
            Photo photo;

            public ListViewHolder(@NonNull View itemView) {
                super(itemView);
                vieweditem_card = itemView.findViewById(R.id.vieweditem_card);
                vieweditem_img = itemView.findViewById(R.id.vieweditem_img);
                vieweditem_name = itemView.findViewById(R.id.vieweditem_name);
                this.itemView = itemView;
            }
        }

    }



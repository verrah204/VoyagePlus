package io.network.voyageplus.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.network.voyageplus.MainActivity;
import io.network.voyageplus.R;
import io.network.voyageplus.activities.PlaceDetails;
import io.network.voyageplus.model.Photo;
import io.network.voyageplus.ui.favorite.FavoriteFragment;

public class Fav_fragment_adapter extends RecyclerView.Adapter<Fav_fragment_adapter.ListViewHolder> {

    private final Context context;
    private final ArrayList<Photo> inner_list;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public Fav_fragment_adapter(Context context, ArrayList<Photo> inner_list) {
        this.context = context;
        this.inner_list = inner_list;
    }


    @NonNull
    @Override
    public Fav_fragment_adapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item, null);
        return new Fav_fragment_adapter.ListViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Fav_fragment_adapter.ListViewHolder holder, int position) {
        holder.fav_place_name.setText(inner_list.get(position).getName());
        holder.fav_place_town.setText(inner_list.get(position).getTown());

        // Uri uri = Uri.parse(inner_list.get(position).getImageUrl());
        Picasso.get().load(inner_list.get(position).getImageUrl()).placeholder(R.drawable.gradient).into(holder.fav_img);

        //holder.place_item_img.setImageURI(uri);

        holder.fav_image_lay.setOnClickListener(v -> {
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

        holder.rm_fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Disliked! Refresh to apply change", Toast.LENGTH_SHORT).show();

                reference.child("photos")
                        .child(inner_list.get(position).getPhotoId())
                        .child("likes")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .removeValue();

                reference.child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("likes")
                        .child(inner_list.get(position).getPhotoId())
                        .removeValue();

            }
        });
       // holder.fav_rec.notify();

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
        TextView fav_place_name,fav_place_town;
        ImageView fav_img ;
        CardView fav_image_lay;
        ImageButton rm_fav_btn;
        RecyclerView fav_rec;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            fav_image_lay = itemView.findViewById(R.id.fav_image_lay);
            fav_img = itemView.findViewById(R.id.fav_img);
            rm_fav_btn = itemView.findViewById(R.id.rm_fav_btn);
            fav_place_name = itemView.findViewById(R.id.fav_place_name);
          //  fav_rec = itemView.findViewById(R.id.fav_rec);
            fav_place_town = itemView.findViewById(R.id.fav_place_town);
            this.itemView = itemView;
        }
    }
}

package io.network.voyageplus.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.network.voyageplus.R;
import io.network.voyageplus.activities.PlaceDetails;
import io.network.voyageplus.model.Photo;

public class Top_du_moment_adapter extends RecyclerView.Adapter<Top_du_moment_adapter.ListViewHolder> {

    private final Context context;
    private final ArrayList<Photo> inner_list;

    public Top_du_moment_adapter(Context context, ArrayList<Photo> inner_list) {
        this.context = context;
        this.inner_list = inner_list;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_moment, null);
        return new ListViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.place_item_name.setText(inner_list.get(position).getName());
        holder.place_item_categorie.setText(inner_list.get(position).getCategorie());
        holder.place_item_rate.setText(inner_list.get(position).getRate());

       // Uri uri = Uri.parse(inner_list.get(position).getImageUrl());
        Picasso.get().load(inner_list.get(position).getImageUrl()).placeholder(R.drawable.gradient).into(holder.place_item_img);

        //holder.place_item_img.setImageURI(uri);

        holder.place_item_lay.setOnClickListener(v -> {
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
        TextView place_item_name,place_item_categorie,place_item_rate;
        ImageView place_item_img ;
        RelativeLayout place_item_lay;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            place_item_lay = itemView.findViewById(R.id.place_item_lay);
            place_item_img = itemView.findViewById(R.id.place_item_img);
            place_item_name = itemView.findViewById(R.id.place_item_name);
            place_item_categorie = itemView.findViewById(R.id.place_item_categorie);
            place_item_rate = itemView.findViewById(R.id.place_item_rate);
            this.itemView = itemView;
        }
    }
}

package io.network.voyageplus.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.network.voyageplus.R;
import io.network.voyageplus.activities.PlaceDetails;
import io.network.voyageplus.model.Comments;
import io.network.voyageplus.model.Photo;

public class Comments_adapter extends RecyclerView.Adapter<Comments_adapter.ListViewHolder> {

    private final Context context;
    private final ArrayList<Comments> inner_list;

    public Comments_adapter(Context context, ArrayList<Comments> inner_list) {
        this.context = context;
        this.inner_list = inner_list;
    }


    @NonNull
    @Override
    public Comments_adapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_items, null);
        return new Comments_adapter.ListViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Comments_adapter.ListViewHolder holder, int position) {
        holder.citem_time.setText(inner_list.get(position).getDate());
        holder.citem_text.setText(inner_list.get(position).getComment_text());
        holder.citem_user.setText(inner_list.get(position).getUser_id());

        //holder.place_item_img.setImageURI(uri);

        holder.comment_lay.setOnClickListener(v -> {
            Comments comment = new Comments();
            comment.setComment_text( inner_list.get(position).getComment_text());
            comment.setUser_id( inner_list.get(position).getUser_id());
            comment.setDate( inner_list.get(position).getDate());

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
        TextView citem_time,citem_text,citem_user;
        LinearLayout comment_lay;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_lay = itemView.findViewById(R.id.comment_lay);
            citem_time = itemView.findViewById(R.id.citem_time);
            citem_text = itemView.findViewById(R.id.citem_text);
            citem_user = itemView.findViewById(R.id.citem_user);
            this.itemView = itemView;
        }
    }
}

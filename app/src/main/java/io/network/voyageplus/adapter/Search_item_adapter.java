package io.network.voyageplus.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.network.voyageplus.R;
import io.network.voyageplus.activities.PlaceDetails;
import io.network.voyageplus.model.Photo;

public class Search_item_adapter extends ArrayAdapter<Photo> {

    private static final String TAG = "Search_item_adapter";


    private LayoutInflater mInflater;
    private List<Photo> photos = null;
    private int layoutResource;
    private Context mContext;

    public Search_item_adapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Photo> objects) {
        super(context, resource, objects);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.photos = objects;
    }

    private static class ViewHolder{
        TextView search_item_name;
        TextView search_item_cat;
        ImageView search_item_img;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        final ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();

            holder.search_item_name = (TextView) convertView.findViewById(R.id.search_item_name);
            holder.search_item_cat = (TextView) convertView.findViewById(R.id.search_item_cat);

            holder.search_item_img =  convertView.findViewById(R.id.search_item_img);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        holder.search_item_name.setText(getItem(position).getName());
        holder.search_item_cat.setText(getItem(position).getCategorie());
        Picasso.get().load(getItem(position).getImageUrl()).placeholder(R.drawable.gradient).into(holder.search_item_img);


        return convertView;
    }
}
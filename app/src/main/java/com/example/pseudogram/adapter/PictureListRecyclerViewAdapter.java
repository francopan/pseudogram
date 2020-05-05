package com.example.pseudogram.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pseudogram.R;
import com.example.pseudogram.activity.EditPictureActivity;
import com.example.pseudogram.model.Picture;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PictureListRecyclerViewAdapter extends RecyclerView.Adapter<PictureListRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private Context mContext;
    private List<Picture> pictures;

    public PictureListRecyclerViewAdapter(List<Picture> pictures, Context context) {
        this.pictures = pictures;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_picture, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String title = pictures.get(position).getTitle();
        title = (title != null && !title.isEmpty()) ? pictures.get(position).getTitle() : pictures.get(position).getPath();
        holder.imageName.setText((title.length() > 20)? title.substring(0,20) + "..." : title);
        holder.parentLayout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditPictureActivity.class);
                intent.putExtra("EXTRA_PICTURE", pictures.get(position));
                mContext.startActivity(intent);
            }
        }));
        Glide.with(mContext).asBitmap().load(pictures.get(position).getPath()).into(holder.image);
    }

    /**
     * Returns the size of the list of pictures being displayed
     * @return
     */
    @Override
    public int getItemCount() {
        return pictures.size();
    }


    /**
     * Holds the Widgets (images) in memory. It's "holding" in the view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView imageName;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById((R.id.image_name));
            parentLayout  = itemView.findViewById(R.id.parent_layout);
        }
    }
}

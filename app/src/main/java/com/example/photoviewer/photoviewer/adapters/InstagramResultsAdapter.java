package com.example.photoviewer.photoviewer.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.photoviewer.photoviewer.R;
import com.example.photoviewer.photoviewer.models.InstagramPhoto;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Alexandre on 8/14/2015.
 */
public class InstagramResultsAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramResultsAdapter(Context context, List<InstagramPhoto> images) {
        super(context, android.R.layout.simple_list_item_1, images);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_popular, parent, false);
        }
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        // Clear out the last image
        ivImage.setImageResource(0);
        //populate the tile and download image url
        Picasso.with(getContext()).load(photo.imageURL).into(ivImage);
        //return the completed view
        return convertView;
    }

}

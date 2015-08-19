package com.example.photoviewer.photoviewer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.photoviewer.photoviewer.R;
import com.example.photoviewer.photoviewer.models.InstagramPhoto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Isaac on 8/17/2015.
 */
public class InstagramProfileAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramProfileAdapter(Context context, ArrayList<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_profile, parent, false);
        }
        ImageView ivProf = (ImageView) convertView.findViewById(R.id.ivProf);
        ivProf.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageURL).into(ivProf);
        // Return the completed view to be displayed
        return convertView;
    }
}
package com.example.photoviewer.photoviewer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photoviewer.photoviewer.R;
import com.example.photoviewer.photoviewer.models.InstagramPhoto;
import com.example.photoviewer.photoviewer.models.InstagramUser;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre on 8/14/2015.
 */
public class InstagramResultsAdapter extends ArrayAdapter<InstagramUser> {
    public InstagramResultsAdapter(Context context, List<InstagramUser> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramUser user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_popular, parent, false);
        }
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        tvUsername.setText("@" + user.username);
        // Clear out the last image
        ivImage.setImageResource(0);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(50)
                .oval(false)
                .build();
        //populate the tile and download image url
        Picasso.with(getContext())
                .load(user.profile_picture)
                .transform(transformation)
                .into(ivImage);
        //return the completed view
        return convertView;
    }

}

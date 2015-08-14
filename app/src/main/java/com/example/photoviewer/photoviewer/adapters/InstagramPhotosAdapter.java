package com.example.photoviewer.photoviewer.adapters;

import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photoviewer.photoviewer.R;
import com.example.photoviewer.photoviewer.models.InstagramPhoto;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;


/**
 * Created by alexandre on 8/3/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        InstagramPhoto photo = getItem(position);
        // Check if we are using a recycled view, if not we need to initiate
        if (convertView == null) {
            //create new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        // Look up the views for populating the data (image, caption, username, likes)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvCommentsCount = (TextView) convertView.findViewById(R.id.tvCommentCount);
        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        // insert the model data
        tvCaption.setText(photo.caption);
        tvUsername.setText(photo.username);
        if (photo.commentsCount > 1) {
            tvCommentsCount.setText(String.valueOf(photo.commentsCount) + " comments");
        } else if (photo.commentsCount == 1) {
            tvCommentsCount.setText(String.valueOf(photo.commentsCount) + " comment");
        } else {
            tvCommentsCount.setText("No Comments");
        }
        tvLikesCount.setText(String.valueOf(photo.likesCount) + " likes");
        // Clear out the ImageView
        ivPhoto.setImageResource(0);
        // Insert the Image using Picasso
        Picasso.with(getContext()).load(photo.imageURL).into(ivPhoto);
        // Rounded Images
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(50)
                .oval(false)
                .build();
        // Insert Profile Picture with Picasso
        Picasso.with(getContext())
                .load(photo.profilePicture)
                .transform(transformation)
                .into(ivProfile);
        //return the created item
        return convertView;
    }


}
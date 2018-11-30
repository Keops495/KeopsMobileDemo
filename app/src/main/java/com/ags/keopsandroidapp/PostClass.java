package com.ags.keopsandroidapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostClass extends ArrayAdapter<String> {

    private final ArrayList<String> tags;
    private final ArrayList<String> likedUsers;
    private final ArrayList<String> userImages;
    private final Activity context;

    public PostClass(ArrayList<String> tags, ArrayList<String> likedUsers, ArrayList<String> userImages, Activity context) {
        super(context, R.layout.layout_phototaglike, tags);
        this.tags = tags;
        this.likedUsers = likedUsers;
        this.userImages = userImages;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.layout_phototaglike, null, true);

        TextView tag = customView.findViewById(R.id.textViewTag);
        TextView liked = customView.findViewById(R.id.textViewLikeUser);
        ImageView photo = customView.findViewById(R.id.imageViewFotograf);

        tag.setText(tags.get(position));
        liked.setText(likedUsers.get(position));
        Picasso.get().load(userImages.get(position)).into(photo);

        return customView;
    }
}


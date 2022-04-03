package com.project.wecare.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.project.wecare.R;
import com.project.wecare.models.Evidence;
import com.project.wecare.models.Image;

import java.io.File;
import java.util.ArrayList;

public class ImageViewAdapter extends ArrayAdapter {
    private ArrayList<Evidence> images;

    public ImageViewAdapter(Context context, int resource, ArrayList<Evidence> objects) {
        super(context, resource, objects);
        images = objects;
    }

    @Override
    public int getCount(){
        return images.size();
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.image_grid_item, null);
        ImageView imageView = view.findViewById(R.id.image_id);

        /**
         * default image => drawable camera icon
         * captured image => resource path
         **/
        if (images.get(position).getImagePath().equals(""))
            imageView.setImageResource(R.drawable.camera);

        else {
            File imgFile = new  File(images.get(position).getImagePath());
            imageView.setImageURI(Uri.fromFile(imgFile));
        }


        return view;
    }

    public void setImages(ArrayList<Evidence> images) {
        this.images = images;
    }
}

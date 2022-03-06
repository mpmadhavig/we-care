package com.project.wecare.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.project.wecare.R;
import com.project.wecare.models.Claim;
import com.project.wecare.models.Image;

import java.util.ArrayList;

public class ImageViewAdapter extends ArrayAdapter {
    private ArrayList<Image> images = new ArrayList();

    public ImageViewAdapter(Context context, int resource, ArrayList<Image> objects) {
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
        ImageView imageView = view.findViewById(R.id.imageId);
        imageView.setImageResource(images.get(position).getSrc());
        return view;
    }
}

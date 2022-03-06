package com.project.wecare.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.wecare.R;
import com.project.wecare.models.Claim;

import java.util.ArrayList;

public class ClaimRecViewAdapter extends RecyclerView.Adapter<ClaimRecViewAdapter.ViewHolder> {

    private ArrayList<Claim> claims = new ArrayList<>();

    public ClaimRecViewAdapter() {}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.claim_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(R.drawable.icon);
    }

    @Override
    public int getItemCount() {
        return claims.size();
    }

    public void setClaims(ArrayList<Claim> claims){
        this.claims = claims;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public ViewHolder(@NonNull View view){
            super(view);
            imageView = view.findViewById(R.id.imageId);
        }
    }
}

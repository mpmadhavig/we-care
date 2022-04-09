package com.project.wecare.helpers;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.wecare.R;
import com.project.wecare.interfaces.ItemClickListener;
import com.project.wecare.models.Claim;

import java.util.ArrayList;

public class ClaimRecViewAdapter extends RecyclerView.Adapter<ClaimRecViewAdapter.ViewHolder> {

    private ArrayList<Claim> claims = new ArrayList<>();
    private ItemClickListener clickListener;

    public ClaimRecViewAdapter() {}

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.claim_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(claims.get(position).getClaimId());
    }

    @Override
    public int getItemCount() {
        return claims.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setClaims(ArrayList<Claim> claims){
        this.claims = claims;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textView;

        public ViewHolder(@NonNull View view){
            super(view);
            textView = view.findViewById(R.id.claimName);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAbsoluteAdapterPosition());
        }
    }
}

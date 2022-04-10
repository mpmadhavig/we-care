package com.project.wecare.helpers;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.wecare.R;
import com.project.wecare.interfaces.ItemClickListener;
import com.project.wecare.models.Claim;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        Date date = claims.get(position).getDate();
        holder.textView.setText(" • " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date.getTime()));
        Log.d("Wecare", "Claim state: "+ String.valueOf(claims.get(position).getState() ));
        if (claims.get(position).getState()==0){
            holder.resubmitButton.setVisibility(View.VISIBLE);
        }
        if (claims.get(position).getState()==1){
            holder.resubmitButton.setVisibility(View.GONE);
        }
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private final FloatingActionButton resubmitButton;
        private final FloatingActionButton viewButton;

        public ViewHolder(@NonNull View view){
            super(view);
            textView = view.findViewById(R.id.claimName);
            resubmitButton = view.findViewById(R.id.btn_resubmit);
            viewButton = view.findViewById(R.id.btn_view);

            resubmitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) clickListener.onButtonClick(view, getAbsoluteAdapterPosition());
                }
            });

            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) clickListener.onClick(view, getAbsoluteAdapterPosition());
                }
            });
        }

    }
}

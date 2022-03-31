package com.project.wecare.helpers;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.wecare.R;
import com.project.wecare.models.Vehicle;

import java.util.ArrayList;

public class VehicleRecViewAdapter extends RecyclerView.Adapter<VehicleRecViewAdapter.ViewHolder> {

    private ArrayList<Vehicle> vehicles = new ArrayList<>();

    public VehicleRecViewAdapter() {}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(vehicles.get(position).getSrc());
        holder.textView.setText(vehicles.get(position).getRegNumber() + " : " +
                vehicles.get(position).getModel() + " (" + vehicles.get(position).getYear() + ")");
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setVehicles(ArrayList<Vehicle> vehicles){
        this.vehicles = vehicles;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;
        private final TextView textView;

        public ViewHolder(@NonNull View view){
            super(view);
            imageView = view.findViewById(R.id.vehicleImage);
            textView = view.findViewById(R.id.vehicleName);
        }
    }
}

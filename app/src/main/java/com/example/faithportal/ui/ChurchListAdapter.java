package com.example.faithportal.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faithportal.R;
import com.example.faithportal.data.repository.PlaceResult;

import java.util.List;
import java.util.Comparator;
import java.util.Locale;

public class ChurchListAdapter extends RecyclerView.Adapter<ChurchListAdapter.ChurchViewHolder> {

    private List<PlaceResult> churchList;
    private final Context context;

    public ChurchListAdapter(Context context, List<PlaceResult> churchList) {
        this.context = context;
        this.churchList = churchList;
        sortChurchListByDistance();
    }

    @NonNull
    @Override
    public ChurchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_church, parent, false);
        return new ChurchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChurchViewHolder holder, int position) {
        PlaceResult church = churchList.get(position);
        holder.nameTextView.setText(church.getName());
        //holder.addressTextView.setText(String.format(Locale.getDefault(), "%.2f km", church.getDistance())); // Display the distance
        holder.addressTextView.setVisibility(View.VISIBLE); // Ensure the address TextView is visible

        holder.itemView.setOnClickListener(v -> {
            String uri = "http://maps.google.com/maps?daddr=" + church.getGeometry().getLocation().getLat() + "," + church.getGeometry().getLocation().getLng();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return churchList.size();
    }

    public void updateData(List<PlaceResult> newChurchList) {
        this.churchList = newChurchList;
        sortChurchListByDistance();
        notifyDataSetChanged();
    }

    private void sortChurchListByDistance() {
        churchList.sort(Comparator.comparingDouble(PlaceResult::getDistance));
    }

    public static class ChurchViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView addressTextView;

        public ChurchViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.church_name);
            addressTextView = itemView.findViewById(R.id.church_address);
        }
    }
}
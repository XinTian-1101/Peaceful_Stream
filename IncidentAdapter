package com.example.assignments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.ViewHolder> {

    private List<IncidentClusterItem> incidentList;


    public IncidentAdapter(List<IncidentClusterItem>incidentList) {
        this.incidentList = incidentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incident, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(incidentList.get(position));
    }

    @Override
    public int getItemCount() {
        return incidentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Define views for each item
        private TextView titleTextView;
        private TextView datetypeTextView;
        private TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            titleTextView = itemView.findViewById(R.id.titleTextView);
            datetypeTextView= itemView.findViewById(R.id.datetypeTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);

        }

        public void bind(IncidentClusterItem incident) {
            // Bind incident details to views
            titleTextView.setText(String.format("%s",  incident.getTitle()));
            datetypeTextView.setText(String.format("%s : %s", incident.getDate(), incident.getType()));
            descriptionTextView.setText(incident.getDescription());
        }
    }
}

package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Session;
import com.example.myapplication.R;

import java.util.List;

public class SessionRecViewAdapter extends RecyclerView.Adapter<SessionRecViewAdapter.SessionViewHolder> {
    private Context context;
    private List<Session> sessionList;

    public SessionRecViewAdapter(Context context, List<Session> sessionList) {
        this.context = context;
        this.sessionList = sessionList;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.session_item , null , false);
        SessionViewHolder viewHolder = new SessionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        Session session = sessionList.get(position);
        holder.sessionCounsellorName.setText(session.getCounsellor());
        holder.sessionDate.setText(session.getDate());
        holder.sessionTime.setText(session.getTime());
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public class SessionViewHolder extends RecyclerView.ViewHolder {
        private TextView sessionCounsellorName , sessionDate , sessionTime;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionCounsellorName = itemView.findViewById(R.id.sessionCounsellorName);
            sessionDate = itemView.findViewById(R.id.sessionDate);
            sessionTime = itemView.findViewById(R.id.sessionTime);
        }
    }
}

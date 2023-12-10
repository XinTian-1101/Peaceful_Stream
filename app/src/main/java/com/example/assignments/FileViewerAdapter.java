package com.example.assignments;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class FileViewerAdapter extends RecyclerView.Adapter <FileViewerAdapter.FileViewerViewHolder>implements OnDatabaseChangedListener{

    Context context;
    ArrayList<RecordingItem> arrayList;
    LinearLayoutManager llm;
    DBHelper dbHelper;


    public FileViewerAdapter(Context context, ArrayList<RecordingItem>arrayList, LinearLayoutManager llm){

        this.context = context;
        this.arrayList=arrayList;
        this.llm=llm;

        dbHelper=new DBHelper(context);
        dbHelper.setOnDatabaseChangedListener(this);



    }

    @Override
    public void onNewDatabaseEntryAdded(RecordingItem recordingItem){
        arrayList.add(recordingItem);
        notifyItemInserted(arrayList.size()-1);

    }

    @NonNull
    @Override
    public FileViewerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_view,viewGroup,false);
        return new FileViewerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewerViewHolder holder, int i) {

        RecordingItem recordingItem = arrayList.get(i);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(recordingItem.getLength());
        long seconds = TimeUnit.MILLISECONDS.toSeconds(recordingItem.getLength())- TimeUnit.MINUTES.toSeconds(minutes);

        holder.nameText.setText(recordingItem.getName());
        holder.lengthText.setText(String.format("%02d:%02d",minutes,seconds));
        holder.timeAdded.setText(DateUtils.formatDateTime(context,recordingItem.getTime_added(),DateUtils.FORMAT_SHOW_DATE| DateUtils.FORMAT_NUMERIC_DATE|DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_SHOW_YEAR));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class FileViewerViewHolder extends  RecyclerView.ViewHolder {

        TextView nameText;
        TextView lengthText;
        TextView timeAdded;
        View cardView;


        public FileViewerViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.file_name_text);
            lengthText = itemView.findViewById(R.id.file_length_text);
            timeAdded = itemView.findViewById(R.id.file_timeAdded_text);
            cardView = itemView.findViewById(R.id.cardview);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PlayBackFragment playBackFragment = new PlayBackFragment();
                    Bundle b = new Bundle();

                    b.putSerializable("item",arrayList.get(getAdapterPosition()));
                    playBackFragment.setArguments(b);

                    if (context instanceof FragmentActivity) {
                        FragmentActivity activity = (FragmentActivity) context;
                        playBackFragment.show(activity.getSupportFragmentManager(), "dialog_playback");
                    }
                }
            });


        }
    }

}

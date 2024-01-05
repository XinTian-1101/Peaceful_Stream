package com.example.myapplication.SafetyModule.M4_Adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.SafetyModule.M4_Database.M4_DBHelper;
import com.example.myapplication.SafetyModule.M4_Fragment.M4_PlayBackFragment;
import com.example.myapplication.SafetyModule.M4_Interface.M4_OnDatabaseChangedListener;
import com.example.myapplication.SafetyModule.M4_Item.M4_RecordingItem;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class M4_FileViewerAdapter extends RecyclerView.Adapter <M4_FileViewerAdapter.FileViewerViewHolder>implements M4_OnDatabaseChangedListener {

    Context context;
    ArrayList<M4_RecordingItem> arrayList;
    LinearLayoutManager llm;
    M4_DBHelper m4DbHelper;


    public M4_FileViewerAdapter(Context context, ArrayList<M4_RecordingItem> arrayList, LinearLayoutManager llm) {

        this.context = context;
        this.arrayList = arrayList;
        this.llm = llm;

        m4DbHelper = new M4_DBHelper(context);
        m4DbHelper.setOnDatabaseChangedListener(this);


    }


    public interface OnItemClickListener {
        void onItemLongClick(M4_RecordingItem m4RecordingItem);
    }


    @Override
    public void onNewDatabaseEntryAdded(M4_RecordingItem m4RecordingItem) {
        arrayList.add(m4RecordingItem);
        notifyItemInserted(arrayList.size() - 1);

    }

    @Override
    public void onDatabaseEntryDeleted(long id) {


        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getId() == id) { // Assuming RecordingItem has a method getId()
                arrayList.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }

    }


    @Override
    public void onDatabaseEntryRenamed(long id, String newName, String newPath) {
        // Find the index of the item with the given ID
        int indexToUpdate = -1;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getId() == id) {
                indexToUpdate = i;
                break;
            }
        }

        // If the item was found, update its name and path
        if (indexToUpdate != -1) {
            M4_RecordingItem itemToUpdate = arrayList.get(indexToUpdate);
            itemToUpdate.setName(newName);
            itemToUpdate.setPath(newPath);
            // Notify the adapter of the item change
            notifyItemChanged(indexToUpdate);
        } else {
            Log.e("FileViewerAdapter", "Unable to find recording with ID: " + id);
        }
    }



    @NonNull
    @Override
    public FileViewerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.m4_item_card_view,viewGroup,false);
        return new FileViewerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewerViewHolder holder, int i) {

        M4_RecordingItem m4RecordingItem = arrayList.get(i);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(m4RecordingItem.getLength());
        long seconds = TimeUnit.MILLISECONDS.toSeconds(m4RecordingItem.getLength())- TimeUnit.MINUTES.toSeconds(minutes);

        holder.nameText.setText(m4RecordingItem.getName());
        holder.lengthText.setText(String.format("%02d:%02d",minutes,seconds));
        holder.timeAdded.setText(DateUtils.formatDateTime(context, m4RecordingItem.getTime_added(),DateUtils.FORMAT_SHOW_DATE| DateUtils.FORMAT_NUMERIC_DATE|DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_SHOW_YEAR));


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

                    M4_PlayBackFragment m4PlayBackFragment = new M4_PlayBackFragment();
                    Bundle b = new Bundle();

                    b.putSerializable("item",arrayList.get(getAdapterPosition()));
                    m4PlayBackFragment.setArguments(b);

                    if (context instanceof FragmentActivity) {
                        FragmentActivity activity = (FragmentActivity) context;
                        m4PlayBackFragment.show(activity.getSupportFragmentManager(), "dialog_playback");
                    }
                }
            });






        }
    }

}
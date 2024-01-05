package com.example.myapplication.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Models.Module;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ModulesRecViewAdapter extends RecyclerView.Adapter<ModulesRecViewAdapter.ModulesViewHolder> {
    private ArrayList <Module> moduleList = new ArrayList<>();
    private Context context;
    private OnModuleListener onModuleListener;

    public ModulesRecViewAdapter(Context context , OnModuleListener onModuleListener) {
        this.context = context;
        this.onModuleListener = onModuleListener;
    }
    @NonNull
    @Override
    public ModulesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_item, parent , false);
        ModulesViewHolder viewHolder = new ModulesViewHolder(view , onModuleListener );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ModulesViewHolder holder, int position) {
        RelativeLayout.LayoutParams nameParams = (RelativeLayout.LayoutParams)holder.moduleName.getLayoutParams();
        RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) holder.moduleImg.getLayoutParams();
        if (position % 2 == 0) {
            nameParams.addRule(RelativeLayout.RIGHT_OF , R.id.moduleImage);
            nameParams.setMargins(15 , 0 , 0 , 0);
            holder.moduleName.setLayoutParams(nameParams);
        }
        else {
            imgParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT , RelativeLayout.TRUE);
            nameParams.addRule(RelativeLayout.LEFT_OF , R.id.moduleImage);
            nameParams.setMargins(0 ,0 ,15 ,0);
            holder.moduleImg.setLayoutParams(imgParams);
            holder.moduleName.setLayoutParams(nameParams);
        }
        holder.cardView.setCardBackgroundColor(Color.parseColor(moduleList.get(position).getColour()));
        holder.moduleName.setText(moduleList.get(position).getModuleName());
        Glide.with(context).asBitmap().load(moduleList.get(position).getImgSrc()).into(holder.moduleImg);
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    public void setModuleList(ArrayList<Module> moduleList) {
        this.moduleList = moduleList;
        notifyDataSetChanged();
    }

    public class ModulesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView moduleImg;
        private TextView moduleName;
        private CardView cardView;
        private OnModuleListener onModuleListener;
        public ModulesViewHolder(@NonNull View itemView , OnModuleListener listener) {
            super(itemView);
            moduleImg = itemView.findViewById(R.id.moduleImage);
            moduleName = itemView.findViewById(R.id.moduleName);
            cardView = itemView.findViewById(R.id.cardView);
            onModuleListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onModuleListener.onModuleClick(getAdapterPosition());
        }
    }

    public interface OnModuleListener {
        void onModuleClick (int position);
    }
}
